package com.wingsweaver.kuja.common.cloud.discovery;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 基于 {@link org.springframework.beans.factory.config.BeanDefinition} 的 {@link DependentServiceResolver} 实现类。
 *
 * @author wingsweaver
 */
public class BeanDefinitionDependentServiceResolver implements DependentServiceResolver, InitializingBean, DefaultOrdered {
    @Getter
    @Setter
    private ApplicationContext applicationContext;

    @Getter
    @Setter
    private List<BeanDefinitionServiceNameResolver> serviceNameResolvers;

    @Override
    public void resolve(Collection<String> serviceNames) {
        // 检查前置条件
        if (CollectionUtils.isEmpty(this.serviceNameResolvers)) {
            return;
        }

        // 获取 BeanDefinitionRegistry
        BeanDefinitionRegistry beanDefinitionRegistry = this.resolveBeanDefinitionRegistry();
        if (beanDefinitionRegistry == null) {
            return;
        }

        // 遍历所有的 BeanDefinition，解析对应的服务名称
        for (String beanDefinitionName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            this.resolveBeanDefinition(serviceNames, beanDefinition);
        }
    }

    /**
     * 解析 BeanDefinition 对应的服务名称，并且保存到指定集合中。
     *
     * @param serviceNames   保存服务名称的集合
     * @param beanDefinition BeanDefinition
     */
    protected void resolveBeanDefinition(Collection<String> serviceNames, BeanDefinition beanDefinition) {
        for (BeanDefinitionServiceNameResolver serviceNameResolver : this.serviceNameResolvers) {
            String serviceName = serviceNameResolver.resolveServiceName(beanDefinition);
            if (StringUtil.isNotEmpty(serviceName)) {
                serviceNames.add(serviceName);
                break;
            }
        }
    }

    /**
     * 获取 BeanDefinitionRegistry 实例。
     *
     * @return BeanDefinitionRegistry 实例
     */
    private BeanDefinitionRegistry resolveBeanDefinitionRegistry() {
        BeanDefinitionRegistry beanDefinitionRegistry = null;
        if (this.applicationContext instanceof BeanDefinitionRegistry) {
            beanDefinitionRegistry = (BeanDefinitionRegistry) this.applicationContext;
        } else {
            beanDefinitionRegistry = this.applicationContext.getBean(BeanDefinitionRegistry.class);
        }
        return beanDefinitionRegistry;
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("applicationContext", this.getApplicationContext());
        AssertState.Named.notNull("serviceNameResolvers", this.getServiceNameResolvers());
    }
}
