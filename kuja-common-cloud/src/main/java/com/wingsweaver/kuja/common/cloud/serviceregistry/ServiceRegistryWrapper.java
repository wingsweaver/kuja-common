package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationContext;

/**
 * 用于封装 {@link ServiceRegistry} 的接口类。<br>
 * 默认情况下，使用基于 ByteBuddy 的 {@link DefaultServiceRegistryWrapper}。<br>
 * 如果不满足要求的话，请自行实现该接口，并注册响应的 Bean。
 *
 * @author wingsweaver
 */
public interface ServiceRegistryWrapper extends DefaultOrdered {
    /**
     * 封装指定的 {@link ServiceRegistry} 实例。
     *
     * @param applicationContext Spring 应用上下文
     * @param serviceRegistry    要封装的 {@link ServiceRegistry} 实例
     * @return 如果不能封装的话，返回 null；否则返回封装结果。
     */
    Object wrap(ApplicationContext applicationContext, ServiceRegistry<? extends Registration> serviceRegistry);
}
