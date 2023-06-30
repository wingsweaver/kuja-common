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
     * {@link com.wingsweaver.kuja.common.boot.idgen.IdGeneratorProperties}。
     */
    String PREFIX_ID_GENERATOR = PREFIX_KUJA_COMMON_BOOT + ".id-generator";

    /**
     * {@link com.wingsweaver.kuja.common.boot.idgen.IdGeneratorProperties}。
     */
    String PREFIX_ID_GENERATOR_TIMESTAMP = PREFIX_ID_GENERATOR + ".time-stamp";

    /**
     * {@link com.wingsweaver.kuja.common.boot.idgen.WorkerIdResolverProperties}。
     */
    String PREFIX_ID_GENERATOR_WORKER_ID = PREFIX_ID_GENERATOR + ".worker-id";

    /**
     * {@link com.wingsweaver.kuja.common.boot.idgen.SequenceIdGeneratorProperties}。
     */
    String PREFIX_ID_GENERATOR_SEQUENCE_ID = PREFIX_ID_GENERATOR + ".sequence-id";

    /**
     * App 详细信息。
     */
    interface App {
        /**
         * {@link com.wingsweaver.kuja.common.boot.appinfo.properties.CloudProperties}。
         */
        String CLOUD = PREFIX_APP + ".cloud";

        /**
         * {@link com.wingsweaver.kuja.common.boot.appinfo.properties.ContainerProperties}。
         */
        String CONTAINER = PREFIX_APP + ".container";

        /**
         * {@link com.wingsweaver.kuja.common.boot.appinfo.properties.DeploymentProperties}。
         */
        String DEPLOYMENT = PREFIX_APP + ".deployment";

        /**
         * {@link com.wingsweaver.kuja.common.boot.appinfo.properties.DeviceProperties}。
         */
        String DEVICE = PREFIX_APP + ".device";

        /**
         * {@link com.wingsweaver.kuja.common.boot.appinfo.properties.HostProperties}。
         */
        String HOST = PREFIX_APP + ".host";

        /**
         * {@link com.wingsweaver.kuja.common.boot.appinfo.properties.OsProperties}。
         */
        String OS = PREFIX_APP + ".os";

        /**
         * {@link com.wingsweaver.kuja.common.boot.appinfo.properties.ServiceProperties}。
         */
        String SERVICE = PREFIX_APP + ".service";
    }

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

        /**
         * AppInfo 的兜底属性源。
         */
        String APP_INFO_FALLBACK = "kuja-app-info-fallback";
    }

    /**
     * 错误上报。
     */
    interface ErrorReporting {
        /**
         * {@link com.wingsweaver.kuja.common.boot.errorreporting.ErrorReportingProperties}。
         */
        String PREFIX = PREFIX_KUJA_COMMON_BOOT + ".error-reporting";

        /**
         * {@link com.wingsweaver.kuja.common.boot.errorreporting.ErrorReportingCustomizerProperties}。
         */
        String PREFIX_CUSTOMIZER = PREFIX + ".customizer";

        /**
         * {@link com.wingsweaver.kuja.common.boot.errorreporting.AppInfoErrorRecordCustomizer}。
         */
        String PREFIX_CUSTOMIZER_APP = PREFIX_CUSTOMIZER + ".app";

        /**
         * {@link com.wingsweaver.kuja.common.boot.errorreporting.ErrorInfoErrorRecordCustomizer}。
         */
        String PREFIX_CUSTOMIZER_ERROR = PREFIX_CUSTOMIZER + ".error";

        /**
         * {@link com.wingsweaver.kuja.common.boot.errorreporting.ThreadInfoErrorRecordCustomizer}。
         */
        String PREFIX_CUSTOMIZER_THREAD = PREFIX_CUSTOMIZER + ".thread";

        /**
         * {@link com.wingsweaver.kuja.common.boot.errorreporting.ErrorReportingReporterProperties}。
         */
        String PREFIX_REPORTER = PREFIX + ".reporter";

        /**
         * {@link com.wingsweaver.kuja.common.boot.errorreporting.LoggingErrorReporterProperties}。
         */
        String PREFIX_REPORTER_LOGGING = PREFIX_REPORTER + ".logging";
    }
}
