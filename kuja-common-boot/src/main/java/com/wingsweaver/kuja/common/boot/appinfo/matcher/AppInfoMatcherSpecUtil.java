package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import com.wingsweaver.kuja.common.utils.support.json.JsonCodecUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * {@link AppInfoMatcherSpec} 的辅助工具类。<br>
 * 主要是序列化和反序列化。
 *
 * @author wingsweaver
 */
public final class AppInfoMatcherSpecUtil {
    private AppInfoMatcherSpecUtil() {
        // 禁止实例化
    }

    /**
     * 将 {@link AppInfoMatcherSpec} 转换成 JSON 字符串。
     *
     * @param spec AppInfoMatcherSpec 实例
     * @return JSON 字符串
     */
    public static String toJsonString(AppInfoMatcherSpec spec) {
        if (spec == null) {
            return null;
        }

        Map<String, String> config = new HashMap<>(BufferSizes.SMALL);
        spec.exportConfig(config);

        SerializationData specData = new SerializationData();
        specData.setSpecType(spec.getSpecType());
        specData.setConfig(config);

        return JsonCodecUtil.ensureJsonCodec().toJsonString(specData);
    }

    /**
     * 从 JSON 字符串，解析出对应的 {@link AppInfoMatcherSpec} 实例。
     *
     * @param json JSON 字符串
     * @return AppInfoMatcherSpec 实例
     */
    public static AppInfoMatcherSpec fromJsonString(String json) {
        if (StringUtil.isEmpty(json)) {
            return null;
        }

        SerializationData specData = (SerializationData) JsonCodecUtil.ensureJsonCodec().parseObject(json, SerializationData.class);
        AssertArgs.isTrue(StringUtil.isNotEmpty(specData.getSpecType()), "AppInfoMatcherSpec type must not be empty");
        AppInfoMatcherSpec spec = createAppInfoMatcherSpec(specData.getSpecType());
        if (spec != null) {
            spec.loadConfig(specData.getConfig());
        }
        return spec;
    }

    /**
     * 遇到未知编码的 Spec 时的处理策略的接口定义。
     */
    public interface UnknownSpecTypeHandler {
        /**
         * 处理未知编码的 Spec。
         *
         * @param specType 未知编码的 Spec 类型
         * @return AppInfoMatcherSpec 实例
         */
        AppInfoMatcherSpec handle(String specType);
    }

    /**
     * 遇到未知编码的 Spec 时抛出异常的处理策略。
     */
    public static final UnknownSpecTypeHandler FAIL_ON_UNKNOWN_SPEC_TYPE = specType -> {
        throw new ExtendedRuntimeException("Unknown AppInfoMatcherSpec type " + specType)
                .withExtendedAttribute("specType", specType);
    };

    /**
     * 忽略未知编码的 Spec 的处理策略。
     */
    public static final UnknownSpecTypeHandler IGNORE_UNKNOWN_SPEC_TYPE = specType -> null;

    /**
     * 遇到未知编码的 Spec 时按照 “匹配” 进行处理的策略。
     */
    public static final UnknownSpecTypeHandler ACCEPT_UNKNOWN_SPEC_TYPE = specType -> {
        DummyAppInfoMatcherSpec spec = new DummyAppInfoMatcherSpec();
        spec.setSpecType(specType);
        spec.setMatched(true);
        return spec;
    };

    /**
     * 遇到未知编码的 Spec 时按照 “不匹配” 进行处理的策略。<br>
     * 也是默认的处理策略。
     */
    public static final UnknownSpecTypeHandler REJECT_UNKNOWN_SPEC_TYPE = specType -> {
        DummyAppInfoMatcherSpec spec = new DummyAppInfoMatcherSpec();
        spec.setSpecType(specType);
        spec.setMatched(false);
        return spec;
    };

    /**
     * {@link UnknownSpecTypeHandler} 的保存容器。
     */
    private static final AtomicReference<UnknownSpecTypeHandler> UNKNOWN_SPEC_TYPE_HANDLER =
            new AtomicReference<>(REJECT_UNKNOWN_SPEC_TYPE);

    /**
     * 获取当前的 {@link UnknownSpecTypeHandler}。
     *
     * @return 当前的 {@link UnknownSpecTypeHandler}
     */
    public static UnknownSpecTypeHandler getUnknownSpecTypeHandler() {
        return UNKNOWN_SPEC_TYPE_HANDLER.get();
    }

    /**
     * 设置当前的 {@link UnknownSpecTypeHandler}。
     *
     * @param handler {@link UnknownSpecTypeHandler} 实例
     */
    public static void setUnknownSpecTypeHandler(UnknownSpecTypeHandler handler) {
        AssertArgs.Named.notNull("handler", handler);
        UNKNOWN_SPEC_TYPE_HANDLER.set(handler);
    }

    /**
     * 规格编码到规格生成函数的映射字典。
     */
    private static final Map<String, Supplier<? extends AppInfoMatcherSpec>> TYPE_SPEC_SUPPLIER_MAP = new ConcurrentHashMap<>(BufferSizes.SMALL);

    static {
        registerAppInfoMatcherSpec(AllAppInfoMatcherSpec.SPEC_TYPE, () -> AllAppInfoMatcherSpec.INSTANCE);
        registerAppInfoMatcherSpec(AppInfoValueEqualsMatcherSpec.SPEC_TYPE, AppInfoValueEqualsMatcherSpec::new);
        registerAppInfoMatcherSpec(AppInfoValueRegexMatcherSpec.SPEC_TYPE, AppInfoValueRegexMatcherSpec::new);
        registerAppInfoMatcherSpec(AppInfoValueVersionMatcherSpec.SPEC_TYPE, AppInfoValueVersionMatcherSpec::new);
        registerAppInfoMatcherSpec(CompositeAppInfoMatcherSpec.SPEC_TYPE, CompositeAppInfoMatcherSpec::new);
        registerAppInfoMatcherSpec(HasAppInfoValueMatcherSpec.SPEC_TYPE, HasAppInfoValueMatcherSpec::new);
    }

    /**
     * 通过规格编码创建规格实例。
     *
     * @param specType 规格编码
     * @return 规格实例
     */
    private static AppInfoMatcherSpec createAppInfoMatcherSpec(String specType) {
        Supplier<? extends AppInfoMatcherSpec> supplier = TYPE_SPEC_SUPPLIER_MAP.get(specType);
        if (supplier == null) {
            Class<? extends AppInfoMatcherSpec> clazz = ClassUtil.forName(specType);
            if (clazz == null) {
                supplier = () -> getUnknownSpecTypeHandler().handle(specType);
            } else {
                supplier = () -> createInstance(clazz);
            }
            TYPE_SPEC_SUPPLIER_MAP.putIfAbsent(specType, supplier);
        }
        return supplier.get();
    }

    /**
     * 通过类创建实例。
     *
     * @param clazz 类
     * @param <T>   类型
     * @return 实例
     */
    @SuppressWarnings("unchecked")
    static <T> T createInstance(Class<?> clazz) {
        try {
            return (T) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ExtendedRuntimeException("Cannot create instance for class " + clazz, e)
                    .withExtendedAttribute("class", clazz);
        }
    }

    /**
     * 注册规格的编码和生成函数。
     *
     * @param type     规格的编码
     * @param supplier 实例的生成函数
     */
    public static void registerAppInfoMatcherSpec(String type, Supplier<? extends AppInfoMatcherSpec> supplier) {
        AssertArgs.Named.notEmpty("type", type);
        AssertArgs.Named.notNull("supplier", supplier);
        TYPE_SPEC_SUPPLIER_MAP.put(type, supplier);
    }

    /**
     * 序列化的数据定义。
     */
    @Getter
    @Setter
    static class SerializationData extends AbstractPojo {
        /**
         * 规格的编码。
         */
        private String specType;

        /**
         * 规格的配置。
         */
        private Map<String, String> config;
    }
}
