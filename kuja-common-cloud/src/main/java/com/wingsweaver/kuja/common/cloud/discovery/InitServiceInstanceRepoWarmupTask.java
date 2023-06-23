package com.wingsweaver.kuja.common.cloud.discovery;

import com.wingsweaver.kuja.common.boot.warmup.WarmUpTask;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 用于初始化 {@link ServiceInstanceRepo} 的预热任务。
 *
 * @author wingsweaver
 */
public class InitServiceInstanceRepoWarmupTask implements WarmUpTask, InitializingBean {
    @Getter
    @Setter
    private ServiceInstanceRepo serviceInstanceRepo;

    @Getter
    @Setter
    private List<DependentServiceResolver> dependentServiceResolvers;

    @Override
    public void warmUp() {
        // 获取所有的服务名称
        List<String> serviceNames = this.resolveServiceNames();

        // 更新服务列表
        this.serviceInstanceRepo.setServices(serviceNames);

        // 更新所有的服务实例，并且等待更新完成
        try {
            CompletableFuture<?> future = this.serviceInstanceRepo.refreshAllInstances();
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有的服务名称。
     *
     * @return 所有的服务名称。
     */
    private List<String> resolveServiceNames() {
        List<String> serviceNames = new ArrayList<>(BufferSizes.SMALL);
        for (DependentServiceResolver dependentServiceResolver : this.dependentServiceResolvers) {
            dependentServiceResolver.resolve(serviceNames);
        }
        return serviceNames;
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("serviceInstanceRepo", this.getServiceInstanceRepo());
        AssertState.Named.notNull("dependentServiceResolvers", this.getDependentServiceResolvers());
    }
}
