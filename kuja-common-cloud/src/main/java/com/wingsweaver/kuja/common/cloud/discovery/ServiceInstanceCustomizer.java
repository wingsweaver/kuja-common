package com.wingsweaver.kuja.common.cloud.discovery;

import org.springframework.cloud.client.ServiceInstance;

/**
 * {@link ServiceInstance} 的定制器接口。
 *
 * @author wingsweaver
 */
public interface ServiceInstanceCustomizer {
    /**
     * 定制指定的服务实例。
     *
     * @param serviceInstance 服务实例
     */
    void customize(ServiceInstance serviceInstance);
}
