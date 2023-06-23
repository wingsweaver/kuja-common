package com.wingsweaver.kuja.common.cloud.discovery.feign;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 面向 OpenFeign 的配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.springframework.cloud.openfeign.FeignAutoConfiguration")
public class DiscoveryFeignConfiguration extends AbstractConfiguration {
    @Bean
    public FeignBeanDefinitionServiceNameResolver feignBeanDefinitionServiceNameResolver() {
        return new FeignBeanDefinitionServiceNameResolver();
    }
}
