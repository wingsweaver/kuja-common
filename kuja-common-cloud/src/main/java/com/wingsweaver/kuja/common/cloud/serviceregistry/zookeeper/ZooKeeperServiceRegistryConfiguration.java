package com.wingsweaver.kuja.common.cloud.serviceregistry.zookeeper;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.cloud.constants.KujaCommonCloudKeys;
import com.wingsweaver.kuja.common.cloud.serviceregistry.ServiceRegistryProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 面向 ZooKeeper 的特殊配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration")
@AutoConfigureAfter(name = {"com.wingsweaver.kuja.common.cloud.serviceregistry.KujaCloudServiceRegistrationConfiguration",
        "org.springframework.cloud.zookeeper.serviceregistry.ZookeeperAutoServiceRegistrationAutoConfiguration"})
public class ZooKeeperServiceRegistryConfiguration extends AbstractConfiguration {
    @Bean
    public ZooKeeperRegistrationCustomizer zooKeeperRegistrationCustomizer(ServiceRegistryProperties serviceRegistryProperties,
                                                                           ZookeeperDiscoveryProperties zookeeperDiscoveryProperties) {
        ZooKeeperRegistrationCustomizer zooKeeperRegistrationCustomizer = new ZooKeeperRegistrationCustomizer();
        zooKeeperRegistrationCustomizer.setServiceRegistryProperties(serviceRegistryProperties);
        zooKeeperRegistrationCustomizer.setZookeeperDiscoveryProperties(zookeeperDiscoveryProperties);
        return zooKeeperRegistrationCustomizer;
    }

    @Bean
    @ConditionalOnProperty(prefix = KujaCommonCloudKeys.PREFIX_SERVICE_REGISTRY_PROPERTIES,
            name = "copy-from-app-info", havingValue = "true", matchIfMissing = true)
    public ZooKeeperAppInfoRegistrationCustomizer zooKeeperAppInfoRegistrationCustomizer() {
        return new ZooKeeperAppInfoRegistrationCustomizer();
    }
}
