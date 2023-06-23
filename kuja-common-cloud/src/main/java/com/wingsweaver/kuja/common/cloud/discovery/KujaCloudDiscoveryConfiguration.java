package com.wingsweaver.kuja.common.cloud.discovery;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * kuja-common-cloud 中服务注册相关的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DiscoveryProperties.class)
@ConditionalOnDiscoveryEnabled
@AutoConfigureAfter(name = "org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration")
public class KujaCloudDiscoveryConfiguration extends AbstractConfiguration {
    @Bean
    public DefaultDependentServiceResolver defaultDependentServiceResolver(DiscoveryProperties discoveryProperties) {
        DefaultDependentServiceResolver serviceResolver = new DefaultDependentServiceResolver();
        serviceResolver.setProperties(discoveryProperties);
        return serviceResolver;
    }

    @Bean
    public BeanDefinitionDependentServiceResolver beanDefinitionDependentServiceResolver(
            ApplicationContext applicationContext,
            ObjectProvider<BeanDefinitionServiceNameResolver> beanDefinitionServiceNameResolvers) {
        BeanDefinitionDependentServiceResolver serviceResolver = new BeanDefinitionDependentServiceResolver();
        serviceResolver.setApplicationContext(applicationContext);
        serviceResolver.setServiceNameResolvers(beanDefinitionServiceNameResolvers.orderedStream().collect(Collectors.toList()));
        return serviceResolver;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBlockingDiscoveryEnabled
    public static class BlockingConfiguration extends AbstractConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public ServiceInstanceRepo serviceInstanceRepo(ApplicationContext applicationContext,
                                                       DiscoveryProperties discoveryProperties,
                                                       DiscoveryClient discoveryClient,
                                                       ObjectProvider<ServiceInstanceCustomizer> serviceInstanceCustomizers) {
            DefaultServiceInstanceRepo instanceRepo = new DefaultServiceInstanceRepo();
            instanceRepo.setApplicationContext(applicationContext);
            instanceRepo.setDiscoveryClient(discoveryClient);
            instanceRepo.setRepoProperties(discoveryProperties.getRepo());
            instanceRepo.setServiceInstanceCustomizers(serviceInstanceCustomizers.orderedStream().collect(Collectors.toList()));
            return instanceRepo;
        }

        @Bean
        public InitServiceInstanceRepoWarmupTask initServiceInstanceRepoWarmupTask(
                ServiceInstanceRepo serviceInstanceRepo,
                ObjectProvider<DependentServiceResolver> dependentServiceResolverObjectProvider) {
            InitServiceInstanceRepoWarmupTask warmupTask = new InitServiceInstanceRepoWarmupTask();
            warmupTask.setServiceInstanceRepo(serviceInstanceRepo);
            warmupTask.setDependentServiceResolvers(dependentServiceResolverObjectProvider.orderedStream().collect(Collectors.toList()));
            return warmupTask;
        }
    }
}
