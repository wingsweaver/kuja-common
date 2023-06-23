package com.wingsweaver.kuja.common.cloud.discovery.feign;

import com.wingsweaver.kuja.common.cloud.discovery.BeanDefinitionServiceNameResolver;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;

/**
 * 面向 OpenFeign 的 {@link BeanDefinitionServiceNameResolver} 实现类。
 *
 * @author wingsweaver
 */
public class FeignBeanDefinitionServiceNameResolver implements BeanDefinitionServiceNameResolver, DefaultOrdered {
    @Override
    public String resolveServiceName(BeanDefinition beanDefinition) {
        Object attribute = beanDefinition.getAttribute("feignClientsRegistrarFactoryBean");
        if (attribute instanceof FeignClientFactoryBean) {
            return ((FeignClientFactoryBean) attribute).getName();
        } else {
            return null;
        }
    }
}
