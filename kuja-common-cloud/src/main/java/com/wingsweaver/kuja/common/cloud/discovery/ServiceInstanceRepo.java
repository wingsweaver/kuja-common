package com.wingsweaver.kuja.common.cloud.discovery;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * {@link ServiceInstance} 的仓库类。<br>
 * 自带缓存和刷新机制，以便于定制处理。
 *
 * @author wingsweaver
 */
public interface ServiceInstanceRepo {
    /**
     * 获取所有使用到的服务名称。
     *
     * @return 所有的服务名称。
     */
    List<String> getServices();

    /**
     * 设置所有用到的服务名称。
     *
     * @param services 所有的服务名称
     */
    void setServices(List<String> services);

    /**
     * 获取指定服务的所有实例。
     *
     * @param serviceId 服务名称
     * @return 服务实例列表
     */
    List<ServiceInstance> getInstances(String serviceId);

    /**
     * 更新指定服务的所有实例。
     *
     * @param serviceId        服务名称
     * @param serviceInstances 服务实例列表
     */
    void updateInstances(String serviceId, List<ServiceInstance> serviceInstances);

    /**
     * 获取指定服务关联的附加数据。
     *
     * @param serviceInstance 服务实例
     * @return 附加数据
     */
    Map<String, Object> getInstanceTags(ServiceInstance serviceInstance);

    /**
     * 刷新指定服务的所有实例。
     *
     * @param serviceId 服务名称
     * @return 异步结果
     */
    CompletableFuture<?> refreshInstances(String serviceId);

    /**
     * 刷新所有服务的所有实例。
     *
     * @return 异步结果
     */
    CompletableFuture<?> refreshAllInstances();
}
