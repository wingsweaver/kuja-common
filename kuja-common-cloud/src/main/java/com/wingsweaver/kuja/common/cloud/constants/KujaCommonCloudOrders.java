package com.wingsweaver.kuja.common.cloud.constants;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import com.wingsweaver.kuja.common.utils.constants.Orders;

/**
 * kuja-common-cloud 模块的 Bean Order 定义。
 *
 * @author wingsweaver
 */
public interface KujaCommonCloudOrders {
    /**
     * {@link com.wingsweaver.kuja.common.cloud.serviceregistry.RegistrationCustomizationBean}。
     */
    int REGISTRATION_CUSTOMIZATION_BEAN = KujaCommonBootOrders.APP_INFO_CUSTOMIZATION_BEAN - Orders.STEP_LARGE;

    /**
     * {@link com.wingsweaver.kuja.common.cloud.serviceregistry.DefaultRegistrationCustomizer}。
     */
    int DEFAULT_REGISTRATION_CUSTOMIZER = Orders.DEFAULT_PRECEDENCE - Orders.STEP_LARGE;

    /**
     * {@link com.wingsweaver.kuja.common.cloud.serviceregistry.zookeeper.ZooKeeperRegistrationCustomizer}。
     */
    int ZOO_KEEPER_REGISTRATION_CUSTOMIZER = DEFAULT_REGISTRATION_CUSTOMIZER - Orders.STEP_SMALL;

    /**
     * {@link com.wingsweaver.kuja.common.cloud.serviceregistry.DefaultServiceRegistryWrapper}。
     */
    int DEFAULT_SERVICE_REGISTRY_WRAPPER = Orders.LOWEST_PRECEDENCE - Orders.STEP_LARGE;
}
