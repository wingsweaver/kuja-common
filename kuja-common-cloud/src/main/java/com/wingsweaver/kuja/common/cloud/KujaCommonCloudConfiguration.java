package com.wingsweaver.kuja.common.cloud;

import com.wingsweaver.kuja.common.boot.EnableKujaCommonBoot;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.cloud.circuitbreaker.EnableKujaCloudCircuitBreaker;
import com.wingsweaver.kuja.common.cloud.discovery.EnableKujaCloudDiscovery;
import com.wingsweaver.kuja.common.cloud.env.EnableKujaCommonCloudEnv;
import com.wingsweaver.kuja.common.cloud.serviceregistry.EnableKujaCloudServiceRegistration;
import org.springframework.context.annotation.Configuration;

/**
 * kuja-common-cloud 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableKujaCommonBoot
@EnableKujaCommonCloudEnv
@EnableKujaCloudServiceRegistration
@EnableKujaCloudDiscovery
@EnableKujaCloudCircuitBreaker
public class KujaCommonCloudConfiguration extends AbstractConfiguration {
}
