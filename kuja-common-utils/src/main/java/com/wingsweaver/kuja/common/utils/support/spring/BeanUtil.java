package com.wingsweaver.kuja.common.utils.support.spring;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
     * 获取指定类型的所有 bean。
     *
     * @param beanFactory BeanFactory
     * @param clazz       类型
     * @param <T>         类型
     * @return 指定类型的所有 bean 的列表
     */
    public static <T> List<T> getBeansOrdered(BeanFactory beanFactory, Class<T> clazz) {
        return beanFactory.getBeanProvider(clazz).orderedStream().collect(Collectors.toList());
    }

    /**
     * 获取指定的 Bean。<br>
     * {@code beanName} 和 {@code requiredType} 至少要指定一个。
     *
     * @param beanFactory  BeanFactory
     * @param beanName     Bean 名称
     * @param requiredType Bean 类型
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(BeanFactory beanFactory, String beanName, Class<T> requiredType, boolean forceProvide) {
        // 检查参数
        boolean hasBeanName = StringUtil.isNotEmpty(beanName);
        boolean hasType = requiredType != null;
        if (!hasBeanName && !hasType) {
            throw new IllegalArgumentException("At least [beanName] or [requiredType] should be specified.");
        }

        // 强制生成相关的 Bean
        if (hasType && forceProvide) {
            beanFactory.getBeanProvider(requiredType);
        }

        // 获取所需的 Bean
        if (hasBeanName) {
            if (hasType) {
                return beanFactory.getBean(beanName, requiredType);
            } else {
                return (T) beanFactory.getBean(beanName);
            }
        } else {
            return beanFactory.getBean(requiredType);
        }
    }

    /**
     * 获取指定的 Bean。
     *
     * @param beanFactory  BeanFactory
     * @param requiredType Bean 类型
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(BeanFactory beanFactory, Class<T> requiredType, boolean forceProvide) {
        return getBean(beanFactory, null, requiredType, forceProvide);
    }

    /**
     * 获取指定的 Bean。
     *
     * @param beanFactory  BeanFactory
     * @param requiredType Bean 类型
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(BeanFactory beanFactory, Class<T> requiredType) {
        return getBean(beanFactory, null, requiredType, true);
    }

    /**
     * 获取指定的 Bean。
     *
     * @param beanFactory  BeanFactory
     * @param beanName     Bean 名称
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(BeanFactory beanFactory, String beanName, boolean forceProvide) {
        return getBean(beanFactory, beanName, null, forceProvide);
    }

    /**
     * 获取指定的 Bean。
     *
     * @param beanFactory BeanFactory
     * @param beanName    Bean 名称
     * @param <T>         Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(BeanFactory beanFactory, String beanName) {
        return getBean(beanFactory, beanName, null, true);
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param beanFactory  BeanFactory
     * @param beanName     Bean 名称
     * @param requiredType Bean 类型
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(BeanFactory beanFactory, String beanName, Class<T> requiredType, boolean forceProvide, Supplier<T> fallbackSupplier) {
        AssertArgs.Named.notNull("fallbackSupplier", fallbackSupplier);
        try {
            return getBean(beanFactory, beanName, requiredType, forceProvide);
        } catch (Exception e) {
            return fallbackSupplier.get();
        }
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param beanFactory  BeanFactory
     * @param requiredType Bean 类型
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(BeanFactory beanFactory, Class<T> requiredType, boolean forceProvide, Supplier<T> fallbackSupplier) {
        return getBean(beanFactory, null, requiredType, forceProvide, fallbackSupplier);
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param beanFactory  BeanFactory
     * @param requiredType Bean 类型
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(BeanFactory beanFactory, Class<T> requiredType, Supplier<T> fallbackSupplier) {
        return getBean(beanFactory, null, requiredType, true, fallbackSupplier);
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param beanFactory  BeanFactory
     * @param beanName     Bean 名称
     * @param forceProvide 是否强制提供
     * @param <T>          Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(BeanFactory beanFactory, String beanName, boolean forceProvide, Supplier<T> fallbackSupplier) {
        return getBean(beanFactory, beanName, null, forceProvide, fallbackSupplier);
    }

    /**
     * 获取指定的 Bean，获取失败的话返回指定的默认值。
     *
     * @param beanFactory BeanFactory
     * @param beanName    Bean 名称
     * @param <T>         Bean 类型
     * @return Bean 实例
     */
    public static <T> T getBean(BeanFactory beanFactory, String beanName, Supplier<T> fallbackSupplier) {
        return getBean(beanFactory, beanName, null, true, fallbackSupplier);
    }

    /**
     * 初始化指定的实例。
     *
     * @param beanFactory BeanFactory
     * @param instance    已有的实例
     */
    public static void autowire(AutowireCapableBeanFactory beanFactory, Object instance) {
        if (beanFactory != null) {
            beanFactory.autowireBeanProperties(instance, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
            beanFactory.initializeBean(instance, ObjectUtil.originalToString(instance));
        }
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
