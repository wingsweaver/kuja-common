package com.wingsweaver.kuja.common.cloud.serviceregistry.zookeeper;

import com.wingsweaver.kuja.common.cloud.common.ServiceInstanceUtil;
import com.wingsweaver.kuja.common.cloud.constants.KujaCommonCloudOrders;
import com.wingsweaver.kuja.common.cloud.serviceregistry.RegistrationCustomizer;
import com.wingsweaver.kuja.common.cloud.serviceregistry.ServiceRegistryProperties;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;

/**
 * ZooKeeper 版的 {@link RegistrationCustomizer} 实现类。
 *
 * @author wingsweaver
 */
public class ZooKeeperRegistrationCustomizer implements RegistrationCustomizer, InitializingBean {
    @Getter
    @Setter
    private ServiceRegistryProperties serviceRegistryProperties;

    @Getter
    @Setter
    private ZookeeperDiscoveryProperties zookeeperDiscoveryProperties;

    @Override
    public void customize(Registration registration) {
        // 直接更新 ZooKeeperDiscoveryProperties 的元数据字典
        // 此时 ZooKeeper 版的 Registration 实例实际尚未就绪，无法直接更新 Registration 中的 metadata 字段
        ServiceInstanceUtil.importMetadata(zookeeperDiscoveryProperties.getMetadata(), serviceRegistryProperties.getMetadata());
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("serviceRegistryProperties", this.getServiceRegistryProperties());
        AssertState.Named.notNull("zookeeperDiscoveryProperties", this.getZookeeperDiscoveryProperties());
    }

    @Override
    public int getOrder() {
        return KujaCommonCloudOrders.ZOO_KEEPER_REGISTRATION_CUSTOMIZER;
    }
}
