package com.wingsweaver.kuja.common.cloud.discovery;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 从 {@link BeanDefinition} 中解析服务名称的接口。
 *
 * @author wingsweaver
 */
public interface BeanDefinitionServiceNameResolver {
    /**
     * 解析服务名称。
     *
     * @param beanDefinition BeanDefinition 实例
     * @return 服务名称
     */
    String resolveServiceName(BeanDefinition beanDefinition);
}
