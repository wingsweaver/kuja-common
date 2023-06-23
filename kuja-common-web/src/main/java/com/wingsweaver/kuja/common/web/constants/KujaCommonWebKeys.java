package com.wingsweaver.kuja.common.web.constants;

/**
 * kuja-common-web 的 Key 定义。
 *
 * @author wingsweaver
 */
public interface KujaCommonWebKeys {
    /**
     * kuja-common-web。
     */
    String PREFIX_KUJA_COMMON_WEB = "kuja.web";

    /**
     * {@link com.wingsweaver.kuja.common.web.common.WebServerProperties}。
     */
    String PREFIX_SERVER_PROPERTIES = PREFIX_KUJA_COMMON_WEB + ".server";

    /**
     * {@link com.wingsweaver.kuja.common.web.errorhandling.ErrorHandlingProperties}。
     */
    String PREFIX_ERROR_HANDLING_PROPERTIES = PREFIX_KUJA_COMMON_WEB + ".error-handling";

    /**
     * {@link com.wingsweaver.kuja.common.web.errorhandling.ErrorHandlingProperties.GlobalErrorAdviceProperties}。
     */
    String PREFIX_GLOBAL_ERROR_ADVICE_PROPERTIES = PREFIX_ERROR_HANDLING_PROPERTIES + ".global-error-advice";

    /**
     * {@link com.wingsweaver.kuja.common.web.errorhandling.ErrorHandlingProperties.GlobalErrorControllerProperties}。
     */
    String PREFIX_GLOBAL_ERROR_CONTROLLER_PROPERTIES = PREFIX_ERROR_HANDLING_PROPERTIES + ".global-error-controller";
}
