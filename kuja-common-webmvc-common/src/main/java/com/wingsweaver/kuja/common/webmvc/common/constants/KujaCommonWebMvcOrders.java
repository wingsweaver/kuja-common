package com.wingsweaver.kuja.common.webmvc.common.constants;

import com.wingsweaver.kuja.common.utils.constants.Orders;

/**
 * kuja-common-webmvc-jee 的常量。
 *
 * @author wingsweaver
 */
public interface KujaCommonWebMvcOrders {
    /**
     * BusinessContextServletRequestListener。
     */
    int BUSINESS_CONTEXT_SERVLET_REQUEST_LISTENER_ORDER = Orders.HIGHEST_PRECEDENCE + Orders.STEP_LARGE;

    /**
     * BusinessContextFilter。
     */
    int BUSINESS_CONTEXT_FILTER = Orders.HIGHEST_PRECEDENCE + Orders.STEP_LARGE;

    /**
     * DynamicLogContextFilter。
     */
    int DYNAMIC_LOG_CONTEXT_FILTER = Orders.HIGHEST_PRECEDENCE + Orders.STEP_MEDIUM;
}
