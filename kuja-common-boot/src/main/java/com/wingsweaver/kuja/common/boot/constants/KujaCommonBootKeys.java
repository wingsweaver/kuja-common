package com.wingsweaver.kuja.common.boot.constants;

/**
 * kuja-common-boot 中的 Bean Key 定义。
 *
 * @author wingsweaver
 */
public interface KujaCommonBootKeys {
    /**
     * kuja-common-boot 模块的配置属性前缀。
     */
    String PREFIX_KUJA_COMMON_BOOT = "kuja.boot";

    /**
     * 应用程序相关信息。
     */
    String PREFIX_APP = PREFIX_KUJA_COMMON_BOOT + ".app";

    /**
     * {@link com.wingsweaver.kuja.common.boot.warmup.WarmUpProperties}。
     */
    String PREFIX_WARM_UP_PROPERTIES = PREFIX_KUJA_COMMON_BOOT + ".warmup";

    /**
     * {@link com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueProperties}。
     */
    String PREFIX_RETURN_VALUE_PROPERTIES = PREFIX_KUJA_COMMON_BOOT + ".return-value";

    /**
     * {@link com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionProperties}。
     */
    String PREFIX_ERROR_DEFINITION_PROPERTIES = PREFIX_KUJA_COMMON_BOOT + ".error-definition";

    /**
     * {@link com.wingsweaver.kuja.common.boot.env.postprocessor.InheritEnvironmentSettings}。
     */
    String PREFIX_INHERIT_ENVIRONMENT_SETTINGS = PREFIX_KUJA_COMMON_BOOT + ".env.inherited";

    /**
     * 属性名称。
     */
    interface PropertySourceNames {
        /**
         * 预设的属性源的名称。
         */
        String PRESET = "kuja-preset";

        /**
         * 兜底的属性源的名称。
         */
        String FALLBACK = "kuja-fallback";

        /**
         * 继承的属性源的名称。
         */
        String INHERITED = "kuja-spring-inherited";

        /**
         * 环境解析器的属性源的名称。
         */
        String ENVIRONMENT_RESOLVER = "kuja-env-resolver";
    }
}
