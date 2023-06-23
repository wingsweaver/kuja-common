package com.wingsweaver.kuja.common.boot.model;

import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于 Spring 的组件的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractComponent implements ApplicationContextAware, InitializingBean {
    /**
     * 应用上下文。
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 暂时什么也不做
    }

    /**
     * 获取指定类型的所有 bean。
     *
     * @param clazz 类型
     * @param <T>   类型
     * @return 指定类型的所有 bean 的列表
     */
    protected <T> List<T> getBeansOrdered(Class<T> clazz) {
        return this.applicationContext.getBeanProvider(clazz).orderedStream().collect(Collectors.toList());
    }

    /**
     * 获取指定的 Bean。<br>
     * {@code beanName} 和 {@code requiredType} 至少要指定一个。
     *
     * @param beanName     Bean 名称
     * @param requiredType Bean 类型
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    @SuppressWarnings("unchecked")
    protected <T> T getBean(String beanName, Class<T> requiredType, boolean forceProvide) {
        // 检查参数
        boolean hasBeanName = StringUtil.isNotEmpty(beanName);
        boolean hasType = requiredType != null;
        if (!hasBeanName && !hasType) {
            throw new IllegalArgumentException("At least [beanName] or [requiredType] should be specified.");
        }

        // 强制生成相关的 Bean
        if (hasType && forceProvide) {
            this.applicationContext.getBeanProvider(requiredType);
        }

        // 获取所需的 Bean
        if (hasBeanName) {
            if (hasType) {
                return this.applicationContext.getBean(beanName, requiredType);
            } else {
                return (T) this.applicationContext.getBean(beanName);
            }
        } else {
            return this.applicationContext.getBean(requiredType);
        }
    }
}
