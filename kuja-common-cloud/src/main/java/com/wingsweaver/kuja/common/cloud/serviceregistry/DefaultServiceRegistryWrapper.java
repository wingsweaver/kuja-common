package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.cloud.constants.KujaCommonCloudOrders;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * 默认的 {@link ServiceRegistryWrapper} 实现类。<br>
 * 基于 ByteBuddy 对 {@link ServiceRegistry} 进行封装，替换其中的 {@link ServiceRegistry#register(Registration)} 方法，
 *
 * @author wingsweaver
 */
public class DefaultServiceRegistryWrapper implements ServiceRegistryWrapper {
    @Override
    public Object wrap(ApplicationContext applicationContext, ServiceRegistry<? extends Registration> serviceRegistry) {
        ByteBuddyServiceRegistryHelper helper = new ByteBuddyServiceRegistryHelper(applicationContext, serviceRegistry);
        try {
            return helper.wrapServiceRegistry(
                    helper::wrapDefaultConstructor,
                    helper::wrapMethods);
        } catch (ReflectiveOperationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrder() {
        return KujaCommonCloudOrders.DEFAULT_SERVICE_REGISTRY_WRAPPER;
    }
}
