package com.wingsweaver.kuja.common.cloud.discovery;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * {@link ServiceInstanceRepo} 实现类的基类。
 *
 * @author wingsweaver
 */
public abstract class AbstractServiceInstanceRepo implements SmartInitializingSingleton, ServiceInstanceRepo, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceInstanceRepo.class);

    @Getter
    @Setter
    private ApplicationContext applicationContext;

    @Getter
    @Setter
    private DiscoveryClient discoveryClient;

    @Getter
    @Setter
    private DiscoveryProperties.RepoProperties repoProperties;

    private volatile List<String> serviceNames;

    private final Object serviceNamesLock = new Object();

    /**
     * 服务 ID 到服务实例列表的映射字典。
     */
    private final Map<String, ServiceInstanceListWrapper> serviceInstancesMap = new ConcurrentHashMap<>(BufferSizes.SMALL);

    @Getter
    @Setter
    private Executor executor;

    @Override
    public List<String> getServices() {
        return this.serviceNames;
    }

    @Override
    public void setServices(List<String> services) {
        LogUtil.trace(LOGGER, "Set services: {}", services);
        synchronized (this.serviceNamesLock) {
            if (services == null) {
                this.serviceNames = Collections.emptyList();
            } else {
                this.serviceNames = Collections.unmodifiableList(new ArrayList<>(services));
            }
        }
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        ServiceInstanceListWrapper serviceInstanceListWrapper = this.serviceInstancesMap.get(serviceId);
        if (serviceInstanceListWrapper == null) {
            return Collections.emptyList();
        } else {
            return serviceInstanceListWrapper.getServiceInstances();
        }
    }

    @Override
    public Map<String, Object> getInstanceTags(ServiceInstance serviceInstance) {
        return Collections.emptyMap();
    }

    @Override
    public CompletableFuture<?> refreshInstances(String serviceId) {
        LogUtil.trace(LOGGER, "Submit task to refresh instances of service: {}", serviceId);
        if (this.executor != null) {
            // 设置了 executor 的话，提交刷新请求、异步刷新
            return CompletableFuture.runAsync(() -> this.refreshInstancesInternal(serviceId), this.executor);
        } else {
            // 没有设置 executor 的话，直接同步刷新
            return CompletableFuture.runAsync(() -> this.refreshInstancesInternal(serviceId));
        }
    }

    /**
     * 刷新服务实例的列表的实际处理。
     *
     * @param serviceId 服务 ID
     */
    protected void refreshInstancesInternal(String serviceId) {
        // 调用 DiscoveryClient 获取服务实例的列表
        LogUtil.trace(LOGGER, "Calling discovery client to refresh instances of service: {} ...", serviceId);
        List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(serviceId);

        // 更新服务实例的列表
        LogUtil.trace(LOGGER, "Updating instances of service: {} ...", serviceId);
        this.updateInstances(serviceId, serviceInstances);
    }

    @Override
    public void updateInstances(String serviceId, List<ServiceInstance> serviceInstances) {
        ServiceInstanceListWrapper wrapper = this.serviceInstancesMap.computeIfAbsent(serviceId, key -> new ServiceInstanceListWrapper());
        List<ServiceInstance> newServiceInstances = this.mergeInstances(wrapper.getServiceInstances(), serviceInstances);
        synchronized (wrapper.getLock()) {
            wrapper.setServiceInstances(newServiceInstances);
        }
    }

    /**
     * 合并服务实例列表。
     *
     * @param oldInstances 旧的服务实例列表
     * @param newInstances 新的服务实例列表
     * @return 合并后的服务实例列表
     */
    protected abstract List<ServiceInstance> mergeInstances(List<ServiceInstance> oldInstances, List<ServiceInstance> newInstances);

    @Override
    public CompletableFuture<?> refreshAllInstances() {
        List<String> serviceNames = new ArrayList<>(this.getServices());

        // 如果没有需要刷新的服务，直接返回一个已完成的 CompletableFuture
        if (serviceNames.isEmpty()) {
            LogUtil.trace(LOGGER, "No services to refresh");
            return CompletableFuture.completedFuture(null);
        }

        // 否则执行各个刷新任务，并且等待所有任务完成
        CompletableFuture<?>[] futures = serviceNames.stream()
                .map(this::refreshInstances)
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("applicationContext", this.getApplicationContext());
        AssertState.Named.notNull("discoveryClient", this.getDiscoveryClient());
        AssertState.Named.notNull("repoProperties", this.getRepoProperties());
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 初始化 Executor
        String executorName = this.repoProperties.getExecutor();
        if (StringUtil.isNotEmpty(executorName)) {
            this.executor = this.applicationContext.getBean(executorName, Executor.class);
        }
    }

    /**
     * 服务实例列表包装类。
     */
    @Data
    static class ServiceInstanceListWrapper {
        /**
         * 读写锁。
         */
        private final Object lock = new Object();

        /**
         * 服务实例列表。
         */
        private volatile List<ServiceInstance> serviceInstances;
    }
}
