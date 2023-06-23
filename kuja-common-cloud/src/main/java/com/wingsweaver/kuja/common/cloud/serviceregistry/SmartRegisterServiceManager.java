package com.wingsweaver.kuja.common.cloud.serviceregistry;

import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

/**
 * 延迟注册服务的管理器接口。
 *
 * @author wingsweaver
 */
public interface SmartRegisterServiceManager {
    /**
     * 检查是否可以注册指定服务。<br>
     * 如果服务已经就绪（通常是已经预热完成），那么返回 true，交由原始的 {@link ServiceRegistry#register(Registration)} 方法进行注册；<br>
     * 如果服务尚未就绪，那么需要先保存提交的注册请求，等待服务就绪后再进行注册。
     *
     * @param serviceRegistry 服务注册器
     * @param registration    服务的注册信息
     * @return 如果可以注册，则返回 true；否则返回 false。
     */
    boolean canRegister(ServiceRegistry<? extends Registration> serviceRegistry, Registration registration);
}
