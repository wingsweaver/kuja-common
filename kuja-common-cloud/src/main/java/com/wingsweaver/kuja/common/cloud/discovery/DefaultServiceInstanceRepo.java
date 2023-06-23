package com.wingsweaver.kuja.common.cloud.discovery;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 默认的 {@link AbstractServiceInstanceRepo} 实现类。
 *
 * @author wingsweaver
 */
public class DefaultServiceInstanceRepo extends AbstractServiceInstanceRepo {
    @Getter
    @Setter
    private List<ServiceInstanceCustomizer> serviceInstanceCustomizers;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        AssertState.Named.notNull("serviceInstanceCustomizers", this.getServiceInstanceCustomizers());
    }

    @Override
    protected List<ServiceInstance> mergeInstances(List<ServiceInstance> oldInstances, List<ServiceInstance> newInstances) {
        newInstances.forEach(this::customizeServiceInstance);
        return Collections.unmodifiableList(new ArrayList<>(newInstances));
    }

    /**
     * 矫正服务实例的内容。
     *
     * @param serviceInstance 服务实例
     */
    protected void customizeServiceInstance(ServiceInstance serviceInstance) {
        if (this.serviceInstanceCustomizers != null) {
            this.serviceInstanceCustomizers.forEach(customizer -> customizer.customize(serviceInstance));
        }
    }
}
