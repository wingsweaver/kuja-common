package com.wingsweaver.kuja.common.webflux.constants;

import com.wingsweaver.kuja.common.utils.constants.Orders;
import com.wingsweaver.kuja.common.webflux.filter.DynamicLogContextWebFilter;

/**
 * kuja-common-webflux 的常量。
 *
 * @author wingsweaver
 */
public interface KujaCommonWebFluxOrders {
    /**
     * {@link com.wingsweaver.kuja.common.webflux.context.WebFluxContextAccessor}。
     */
    int BUSINESS_CONTEXT_WEB_FILTER = Orders.HIGHEST_PRECEDENCE + Orders.STEP_LARGE;

    /**
     * {@link DynamicLogContextWebFilter}。
     */
    int LOG_CONTEXT_WEB_FILTER = Orders.HIGHEST_PRECEDENCE + Orders.STEP_MEDIUM;

    /**
     * {@link com.wingsweaver.kuja.common.webflux.errorhandling.GlobalErrorWebExceptionHandler}。
     */
    int GLOBAL_ERROR_WEB_EXCEPTION_HANDLER = Orders.DEFAULT_PRECEDENCE - Orders.STEP_LARGE;
}
