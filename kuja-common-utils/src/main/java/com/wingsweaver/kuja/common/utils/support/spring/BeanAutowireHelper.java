package com.wingsweaver.kuja.common.utils.support.spring;

import org.springframework.beans.factory.BeanFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 用于 Bean Autowire 的工具类。
 *
 * @author wingsweaver
 */
public final class BeanAutowireHelper {
    private BeanAutowireHelper() {
        // 禁止实例化
    }

    /**
     * autowire 指定的数据，如果其尚未被 autowire 的话。
     *
     * @param getter   数据的获取函数
     * @param setter   数据的设置函数
     * @param supplier 数据的生成函数
     * @param <T>      数据的类型
     */
    @SuppressWarnings("unused")
    public static <T> void autowire(Supplier<T> getter, Consumer<T> setter, Supplier<T> supplier) {
        T bean = getter.get();
        if (bean == null) {
            bean = supplier.get();
            setter.accept(bean);
        }
    }

    /**
     * autowire 指定的数据，如果其尚未被 autowire 的话。
     *
     * @param getter      数据的获取函数
     * @param setter      数据的设置函数
     * @param beanFactory BeanFactory 实例（用于获取数据）
     * @param beanName    Bean 的名称
     * @param <T>         数据的类型
     */
    @SuppressWarnings({"unchecked", "unused"})
    public static <T> void autowire(Supplier<T> getter, Consumer<T> setter, BeanFactory beanFactory, String beanName) {
        T bean = getter.get();
        if (bean == null) {
            bean = (T) beanFactory.getBean(beanName);
            setter.accept(bean);
        }
    }

    /**
     * autowire 指定的数据，如果其尚未被 autowire 的话。
     *
     * @param getter      数据的获取函数
     * @param setter      数据的设置函数
     * @param beanFactory BeanFactory 实例（用于获取数据）
     * @param clazz       数据的类型
     * @param <T>         数据的类型
     */
    public static <T> void autowire(Supplier<T> getter, Consumer<T> setter, BeanFactory beanFactory, Class<T> clazz) {
        T bean = getter.get();
        if (bean == null) {
            bean = beanFactory.getBean(clazz);
            setter.accept(bean);
        }
    }

    /**
     * autowire 指定的数据，如果其尚未被 autowire 的话。
     *
     * @param getter      数据的获取函数
     * @param setter      数据的设置函数
     * @param beanFactory BeanFactory 实例（用于获取数据）
     * @param beanName    Bean 的名称
     * @param <T>         数据的类型
     */
    @SuppressWarnings({"unchecked", "unused"})
    public static <T> void autowireNoThrow(Supplier<T> getter, Consumer<T> setter, BeanFactory beanFactory, String beanName) {
        T bean = getter.get();
        if (bean == null) {
            try {
                bean = (T) beanFactory.getBean(beanName);
                setter.accept(bean);
            } catch (Exception ex) {
                // 忽略此处的错误
            }
        }
    }

    /**
     * autowire 指定的数据，如果其尚未被 autowire 的话。
     *
     * @param getter      数据的获取函数
     * @param setter      数据的设置函数
     * @param beanFactory BeanFactory 实例（用于获取数据）
     * @param clazz       数据的类型
     * @param <T>         数据的类型
     */
    public static <T> void autowireNoThrow(Supplier<T> getter, Consumer<T> setter, BeanFactory beanFactory, Class<T> clazz) {
        T bean = getter.get();
        if (bean == null) {
            try {
                bean = beanFactory.getBean(clazz);
                setter.accept(bean);
            } catch (Exception ex) {
                // 忽略此处的错误
            }
        }
    }

    /**
     * autowire 指定的集合，如果其尚未被 autowire 的话。
     *
     * @param getter      集合的获取函数
     * @param setter      集合的设置函数
     * @param beanFactory BeanFactory 实例（用于获取数据）
     * @param clazz       数据的类型
     * @param predicate   数据是否符合条件的判定函数
     * @param <T>         数据的类型
     */
    public static <T> void autowireCollection(Supplier<? extends Collection<T>> getter, Consumer<? super Collection<T>> setter,
                                              BeanFactory beanFactory, Class<T> clazz, Predicate<T> predicate) {
        Collection<T> collection = getter.get();
        if (collection == null && setter != null) {
            collection = new LinkedList<>();
            setter.accept(collection);
        }
        if (collection != null && collection.isEmpty()) {
            if (predicate != null) {
                beanFactory.getBeanProvider(clazz).orderedStream().filter(predicate).forEach(collection::add);
            } else {
                beanFactory.getBeanProvider(clazz).orderedStream().forEach(collection::add);
            }
        }
    }

    /**
     * autowire 指定的集合，如果其尚未被 autowire 的话。
     *
     * @param getter      集合的获取函数
     * @param setter      集合的设置函数
     * @param beanFactory BeanFactory 实例（用于获取数据）
     * @param clazz       数据的类型
     * @param <T>         数据的类型
     */
    public static <T> void autowireCollection(Supplier<? extends Collection<T>> getter, Consumer<? super Collection<T>> setter,
                                              BeanFactory beanFactory, Class<T> clazz) {
        autowireCollection(getter, setter, beanFactory, clazz, null);
    }

    /**
     * autowire 指定的集合，如果其尚未被 autowire 的话。
     *
     * @param getter      集合的获取函数
     * @param beanFactory BeanFactory 实例（用于获取数据）
     * @param clazz       数据的类型
     * @param predicate   数据是否符合条件的判定函数
     * @param <T>         数据的类型
     */
    public static <T> void autowireCollection(Supplier<? extends Collection<T>> getter, BeanFactory beanFactory,
                                              Class<T> clazz, Predicate<T> predicate) {
        autowireCollection(getter, null, beanFactory, clazz, predicate);
    }

    /**
     * autowire 指定的集合，如果其尚未被 autowire 的话。
     *
     * @param getter      集合的获取函数
     * @param beanFactory BeanFactory 实例（用于获取数据）
     * @param clazz       数据的类型
     * @param <T>         数据的类型
     */
    public static <T> void autowireCollection(Supplier<? extends Collection<T>> getter, BeanFactory beanFactory, Class<T> clazz) {
        autowireCollection(getter, null, beanFactory, clazz);
    }
}
