package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.cloud.constants.KujaCommonCloudKeys;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * kuja-common-cloud 中服务注册相关的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ServiceRegistryProperties.class)
@ConditionalOnClass(name = "org.springframework.cloud.client.serviceregistry.Registration")
@ConditionalOnProperty(prefix = KujaCommonCloudKeys.PREFIX_SERVICE_REGISTRY_PROPERTIES, name = "enabled", havingValue = "true", matchIfMissing = true)
public class KujaCloudServiceRegistrationConfiguration extends AbstractConfiguration {
    @Bean
    public RegistrationCustomizationBean registrationCustomizationBean(ApplicationContext applicationContext) {
        RegistrationCustomizationBean registrationCustomizationBean = new RegistrationCustomizationBean();
        registrationCustomizationBean.setApplicationContext(applicationContext);
        return registrationCustomizationBean;
    }

    @Bean
    public DefaultRegistrationCustomizer defaultRegistrationCustomizer(ServiceRegistryProperties properties) {
        DefaultRegistrationCustomizer defaultRegistrationCustomizer = new DefaultRegistrationCustomizer();
        defaultRegistrationCustomizer.setProperties(properties);
        return defaultRegistrationCustomizer;
    }

    @Bean
    @ConditionalOnProperty(prefix = KujaCommonCloudKeys.PREFIX_SERVICE_REGISTRY_PROPERTIES,
            name = "copy-from-app-info", havingValue = "true", matchIfMissing = true)
    public AppInfoRegistrationCustomizer appInfoRegistrationCustomizer() {
        return new AppInfoRegistrationCustomizer();
    }

    @Bean
    @ConditionalOnProperty(prefix = KujaCommonCloudKeys.PREFIX_SERVICE_REGISTRY_PROPERTIES,
            name = "copy-to-app-info", havingValue = "true", matchIfMissing = true)
    public KujaCommonCloudAppInfoCustomizer kujaCommonCloudAppInfoCustomizer() {
        return new KujaCommonCloudAppInfoCustomizer();
    }

    /**
     * kuja-common-cloud 中延迟服务注册的相关自动配置。
     *
     * @author wingsweaver
     */
    @ConditionalOnProperty(prefix = KujaCommonCloudKeys.PREFIX_SERVICE_REGISTRY_PROPERTIES,
            name = "smart-register", havingValue = "true", matchIfMissing = true)
    @Configuration(proxyBeanMethods = false)
    public static class SmartRegisterConfiguration extends AbstractConfiguration {
        @Bean
        @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
        public SmartServiceRegistryBeanPostProcessor smartServiceRegistryBeanPostProcessor(ApplicationContext applicationContext) {
            return new SmartServiceRegistryBeanPostProcessor(applicationContext);
        }

        @Bean
        @ConditionalOnMissingBean
        public SmartRegisterServiceManager smartRegisterServiceManager(ApplicationContext applicationContext) {
            return new DefaultSmartRegisterServiceManager(applicationContext);
        }
    }
}
