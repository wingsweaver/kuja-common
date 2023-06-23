package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.constants.Orders;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
        ToStringConfig.Builder builder = new ToStringConfig.Builder(new HashMap<>(BufferSizes.SMALL));
        DefaultToStringConfigCustomizer.INSTANCE.customize(builder);
        SpringFactoriesLoader.loadFactories(ToStringConfigCustomizer.class, null)
                .stream()
                .sorted(Orders::compareReversed)
                .forEach(customizer -> customizer.customize(builder));
        DEFAULT_CONFIG = new AtomicReference<>(builder.build());
    }

    public static ToStringConfig getDefaultConfig() {
        return DEFAULT_CONFIG.get();
    }

    public static void setDefaultConfig(ToStringConfig config) {
        Objects.requireNonNull(config, "[config] is null");
        DEFAULT_CONFIG.set(config);
    }

    /**
     * 将指定实例转换成字符串。
     *
     * @param object  指定实例
     * @param config  转换设置，必须指定
     * @param builder 保存转换结果的 StringBuilder
     */
    public static void toStringBuilder(Object object, ToStringConfig config, StringBuilder builder) {
        // 校验参数
        AssertArgs.Named.notNull("builder", builder);
        if (config == null) {
            config = DEFAULT_CONFIG.get();
        }

        // 执行转换
        for (ToStringConverter converter : CONVERTERS) {
            if (converter.toString(object, builder, config)) {
                // 找到可以处理的 converter 并进行转换之后，中止后续的处理
                return;
            }
        }

        // 默认的处理函数
        builder.append(object);
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
    public static void toStringBuilder(Object object, Consumer<ToStringConfig.Builder> configCustomizer, StringBuilder builder) {
        AssertArgs.Named.notNull("builder", builder);

        if (configCustomizer == null) {
            toStringBuilder(object, getDefaultConfig(), builder);
        } else {
            ToStringConfig.Builder configBuilder = getDefaultConfig().mutable();
            configCustomizer.accept(configBuilder);
            toStringBuilder(object, configBuilder.build(), builder);
        }
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
    public static String toString(Object object, Consumer<ToStringConfig.Builder> configCustomizer) {
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
