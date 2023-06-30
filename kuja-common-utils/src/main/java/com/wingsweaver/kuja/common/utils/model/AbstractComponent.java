package com.wingsweaver.kuja.common.utils.model;

import com.wingsweaver.kuja.common.utils.support.spring.BeanUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.function.Supplier;

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
        return BeanUtil.getBeansOrdered(this.applicationContext, clazz);
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
    protected <T> T getBean(String beanName, Class<T> requiredType, boolean forceProvide) {
        return BeanUtil.getBean(this.applicationContext, beanName, requiredType, forceProvide);
    }

    /**
     * 获取指定的 Bean。
     *
     * @param requiredType Bean 类型
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    protected <T> T getBean(Class<T> requiredType, boolean forceProvide) {
        return this.getBean(null, requiredType, forceProvide);
    }

    /**
     * 获取指定的 Bean。
     *
     * @param requiredType Bean 类型
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    protected <T> T getBean(Class<T> requiredType) {
        return this.getBean(null, requiredType, true);
    }

    /**
     * 获取指定的 Bean。
     *
     * @param beanName     Bean 名称
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    protected <T> T getBean(String beanName, boolean forceProvide) {
        return this.getBean(beanName, null, forceProvide);
    }

    /**
     * 获取指定的 Bean。
     *
     * @param beanName Bean 名称
     * @param <T>      Bean 类型
     * @return Bean 实例
     */
    protected <T> T getBean(String beanName) {
        return this.getBean(beanName, null, true);
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param beanName     Bean 名称
     * @param requiredType Bean 类型
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    protected <T> T getBean(String beanName, Class<T> requiredType, boolean forceProvide, Supplier<T> fallbackSupplier) {
        return BeanUtil.getBean(this.applicationContext, beanName, requiredType, forceProvide, fallbackSupplier);
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param requiredType Bean 类型
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    protected <T> T getBean(Class<T> requiredType, boolean forceProvide, Supplier<T> fallbackSupplier) {
        return getBean(null, requiredType, forceProvide, fallbackSupplier);
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param requiredType Bean 类型
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    protected <T> T getBean(Class<T> requiredType, Supplier<T> fallbackSupplier) {
        return getBean(null, requiredType, true, fallbackSupplier);
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param beanName     Bean 名称
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    protected <T> T getBean(String beanName, boolean forceProvide, Supplier<T> fallbackSupplier) {
        return this.getBean(beanName, null, forceProvide, fallbackSupplier);
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param beanName Bean 名称
     * @param <T>      Bean 类型
     * @return Bean 实例
     */
    protected <T> T getBean(String beanName, Supplier<T> fallbackSupplier) {
        return this.getBean(beanName, null, true, fallbackSupplier);
    }

    /**
     * 初始化指定的实例。
     *
     * @param instance 已有的实例
     */
    public void autowireBean(Object instance) {
        BeanUtil.autowire(this.applicationContext.getAutowireCapableBeanFactory(), instance);
    }

    /**
     * 创建指定类型的实例，并初始化。
     *
     * @param clazz 实例的类型
     * @param <T>   实例的类型
     * @return 实例
     */
    public <T> T createBean(Class<T> clazz) {
        return BeanUtil.create(this.applicationContext.getAutowireCapableBeanFactory(), clazz);
    }

    /**
     * 从 Bean 名称或者类名获得对应的实例。
     *
     * @param beanOrClassName WorkerIdGenerator 的 Bean 名称或者类名
     * @param <T>             返回值类型
     * @return 对应的实例
     * @throws ClassNotFoundException 找不到指定的类名
     */
    public <T> T beanOrClassInstance(String beanOrClassName) throws ClassNotFoundException {
        return BeanUtil.beanOrClassInstance(this.applicationContext.getAutowireCapableBeanFactory(), beanOrClassName);
    }
}
