package com.wingsweaver.kuja.common.utils.support.spring;

import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * Bean 工具类。
 *
 * @author wingsweaver
 */
public final class BeanUtil {
    /**
     * 构造函数。
     */
    private BeanUtil() {
        // 禁止生成实例
    }

    /**
     * 初始化指定的实例。
     *
     * @param beanFactory BeanFactory
     * @param instance    已有的实例
     * @param <T>         实例的类型
     * @return 初始化结果
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <T> T autowire(AutowireCapableBeanFactory beanFactory, T instance) {
        if (beanFactory != null) {
            beanFactory.autowireBeanProperties(instance, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
            beanFactory.initializeBean(instance, ObjectUtil.originalToString(instance));
        }
        return instance;
    }

    /**
     * 创建指定类型的实例，并初始化。
     *
     * @param beanFactory BeanFactory
     * @param clazz       实例的类型
     * @param <T>         实例的类型
     * @return 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(AutowireCapableBeanFactory beanFactory, Class<T> clazz) {
        return (T) beanFactory.createBean(clazz, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
    }


    /**
     * 从 Bean 名称或者类名获得对应的实例。
     *
     * @param beanFactory     BeanFactory
     * @param beanOrClassName WorkerIdGenerator 的 Bean 名称或者类名
     * @param <T>             返回值类型
     * @return 对应的实例
     * @throws ClassNotFoundException 找不到指定的类名
     */
    @SuppressWarnings({"unchecked", "unused"})
    public static <T> T beanOrClassInstance(AutowireCapableBeanFactory beanFactory, String beanOrClassName)
            throws ClassNotFoundException {
        Object ret;

        try {
            // 先按照 Bean 的名称来尝试获取
            ret = beanFactory.getBean(beanOrClassName);
        } catch (NoSuchBeanDefinitionException ex) {
            // 没有对应的 Bean 定义的话，尝试生成类
            Class<?> clazz = Class.forName(beanOrClassName);
            ret = create(beanFactory, clazz);
        }

        // 返回
        return (T) ret;
    }
}
