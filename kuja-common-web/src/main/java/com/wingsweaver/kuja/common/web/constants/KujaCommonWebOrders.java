package com.wingsweaver.kuja.common.web.constants;

import com.wingsweaver.kuja.common.utils.constants.Orders;

/**
 * kuja-common-web 中的 Order 定义。
 *
 * @author wingsweaver
 */
public interface KujaCommonWebOrders {
    /**
     * {@link com.wingsweaver.kuja.common.web.errorhandling.ResponseDataErrorHandler}。
     */
    int RESPONSE_DATA_ERROR_HANDLER = Orders.HIGHEST_PRECEDENCE + Orders.STEP_LARGE;
}
