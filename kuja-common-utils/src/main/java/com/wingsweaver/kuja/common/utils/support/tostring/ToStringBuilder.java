package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.constants.Orders;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 将实例转换成字符串的工具类。<br>
 * 使用 {@link SpringFactoriesLoader} 加载默认的 {@link ToStringConverter} 和 {@link ToStringConfigCustomizer}。
 *
 * @author wingsweaver
 */
public final class ToStringBuilder {
    private ToStringBuilder() {
        // 禁止实例化
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ToStringBuilder.class);

    /**
     * 转换器实例的列表。
     */
    private static final List<ToStringConverter> CONVERTERS;

    static {
        // 加入 spring.factories 中定义的 转换器
        List<ToStringConverter> converters = SpringFactoriesLoader.loadFactories(ToStringConverter.class, null);
        converters.sort(Orders::compare);

        // 加入默认的转换器
        converters.add(SimpleToStringConverter.INSTANCE);
        converters.add(DateTimeToStringConverter.INSTANCE);
        converters.add(ReflectToStringConverter.INSTANCE);

        CONVERTERS = new ArrayList<>(converters);
    }

    /**
     * 默认的转换设置。
     */
    private static final AtomicReference<ToStringConfig> DEFAULT_CONFIG;

    static {
        ToStringConfig toStringConfig = new ToStringConfig(new HashMap<>(BufferSizes.TINY));
        SpringFactoriesLoader.loadFactories(ToStringConfigCustomizer.class, null)
                .stream()
                .sorted(Orders::compareReversed)
                .forEach(customizer -> customizer.customize(toStringConfig));
        DEFAULT_CONFIG = new AtomicReference<>(toStringConfig);
    }

    /**
     * 获取默认的转换设置的克隆实例。
     *
     * @return 转换设置的克隆实例
     */
    public static ToStringConfig getDefaultConfig() {
        return new ToStringConfig(DEFAULT_CONFIG.get());
    }

    /**
     * 设置默认的转换设置。
     *
     * @param config 转换设置
     */
    public static void setDefaultConfig(ToStringConfig config) {
        AssertArgs.Named.notNull("config", config);
        DEFAULT_CONFIG.set(config);
    }

    /**
     * 将指定实例转换成字符串。
     *
     * @param object  指定实例
     * @param config  转换设置，必须指定
     * @param builder 保存转换结果的 StringBuilder
     */
    @SuppressWarnings("PMD.GuardLogStatement")
    public static void toStringBuilder(Object object, ToStringConfig config, StringBuilder builder) {
        // 校验参数
        AssertArgs.Named.notNull("builder", builder);
        if (config == null) {
            config = getDefaultConfig();
        }

        // 执行转换
        for (ToStringConverter converter : CONVERTERS) {
            try {
                if (converter.toString(object, builder, config)) {
                    // 找到可以处理的 converter 并进行转换之后，中止后续的处理
                    return;
                }
            } catch (Exception ex) {
                // 忽略此处的错误，以免引起意外的宕机
                if (object == null) {
                    LogUtil.error(LOGGER, ex, "ToStringConverter {} throws an exception with null object.", converter);
                } else {
                    LogUtil.error(LOGGER, ex, "ToStringConverter {} throws an exception with object type {}.",
                            converter, object.getClass());
                }
            }
        }

        // 默认的处理函数
        if (object == config.getObjectToString()) {
            // 如果是同一个实例触发了 toString 处理的话，不直接使用 object.toString，以避免产生堆栈溢出
            builder.append(ObjectUtil.originalToString(object));
        } else {
            // 如果不是的话，使用 object.toString() 来进行转换
            builder.append(object);
        }
    }

    /**
     * 使用默认设置，将指定实例转换成字符串。
     *
     * @param object  指定实例
     * @param builder 保存转换结果的 StringBuilder
     */
    public static void toStringBuilder(Object object, StringBuilder builder) {
        AssertArgs.Named.notNull("builder", builder);
        toStringBuilder(object, getDefaultConfig(), builder);
    }

    /**
     * 基于使用默认设置，将指定实例转换成字符串。
     *
     * @param object           指定实例
     * @param configCustomizer 转换设置的定制处理（可以不指定）
     * @param builder          保存转换结果的 StringBuilder
     */
    public static void toStringBuilder(Object object, Consumer<ToStringConfig> configCustomizer, StringBuilder builder) {
        AssertArgs.Named.notNull("builder", builder);
        ToStringConfig config = getDefaultConfig();
        if (configCustomizer != null) {
            configCustomizer.accept(config);
        }
        toStringBuilder(object, config, builder);
    }

    /**
     * 将指定实例转换成字符串。
     *
     * @param object 指定实例
     * @param config 转换设置，必须指定
     * @return 转换结果
     */
    public static String toString(Object object, ToStringConfig config) {
        StringBuilder builder = new StringBuilder();
        toStringBuilder(object, config, builder);
        return builder.toString();
    }

    /**
     * 基于使用默认设置，将指定实例转换成字符串。
     *
     * @param object           指定实例
     * @param configCustomizer 转换设置的定制处理（可以不指定）
     * @return 转换结果
     */
    public static String toString(Object object, Consumer<ToStringConfig> configCustomizer) {
        StringBuilder builder = new StringBuilder();
        toStringBuilder(object, configCustomizer, builder);
        return builder.toString();
    }

    /**
     * 使用使用默认设置，将指定实例转换成字符串。
     *
     * @param object 指定实例
     * @return 转换结果
     */
    public static String toString(Object object) {
        return toString(object, getDefaultConfig());
    }
}
