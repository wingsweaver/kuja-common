package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用于实现延迟注册服务的 BeanPostProcessor 实现类。
 *
 * @author wingsweaver
 */
public class SmartServiceRegistryBeanPostProcessor implements ApplicationContextAware, BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartServiceRegistryBeanPostProcessor.class);

    @Getter
    @Setter
    private ApplicationContext applicationContext;

    private final List<ServiceRegistryWrapper> serviceRegistryWrappers;

    public SmartServiceRegistryBeanPostProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.serviceRegistryWrappers = SpringFactoriesLoader
                .loadFactories(ServiceRegistryWrapperFactory.class, this.getClass().getClassLoader())
                .stream()
                .map(ServiceRegistryWrapperFactory::create)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object newBean = null;
        if (bean instanceof ServiceRegistry<?>) {
            newBean = this.wrapServiceRegistry((ServiceRegistry<?>) bean);
        } else {
            newBean = BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }

        return newBean;
    }

    private Object wrapServiceRegistry(ServiceRegistry<?> bean) {
        Object newBean = null;
        // 使用 serviceRegistryWrappers 进行转换
        for (ServiceRegistryWrapper serviceRegistryWrapper : this.serviceRegistryWrappers) {
            newBean = serviceRegistryWrapper.wrap(this.applicationContext, bean);
            if (newBean != null) {
                LogUtil.trace(LOGGER, "Wrap service registry bean {} to {}, with serviceRegistryWrapper {}",
                        bean, newBean, serviceRegistryWrapper);
                break;
            }
        }

        // 返回
        return newBean != null ? newBean : bean;
    }
}
