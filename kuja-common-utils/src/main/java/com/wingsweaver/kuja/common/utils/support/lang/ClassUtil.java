package com.wingsweaver.kuja.common.utils.support.lang;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;

import java.lang.reflect.Type;

/**
 * 类相关工具。
 *
 * @author wingsweaver
 */
public final class ClassUtil {
    private ClassUtil() {
        // 禁止实例化
    }

    /**
     * 检查指定的类是否存在。
     *
     * @param className   类名
     * @param classLoader 类加载器
     * @return 是否存在
     */
    public static boolean exists(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 检查指定的类是否存在。
     *
     * @param className 类名
     * @return 是否存在
     */
    public static boolean exists(String className) {
        return exists(className, Thread.currentThread().getContextClassLoader());
    }

    /**
     * 检查指定的类是否都存在。
     *
     * @param classLoader 类加载器
     * @param classNames  类名
     * @return 是否存在
     */
    public static boolean existsAll(ClassLoader classLoader, String... classNames) {
        if (classNames == null || classNames.length < 1) {
            return false;
        }

        for (String className : classNames) {
            if (!exists(className, classLoader)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查指定的类是否都存在。
     *
     * @param classNames 类名
     * @return 是否存在
     */
    public static boolean existsAll(String... classNames) {
        return existsAll(Thread.currentThread().getContextClassLoader(), classNames);
    }

    /**
     * 检查指定的类是否都不存在。
     *
     * @param classLoader 类加载器
     * @param classNames  类名
     * @return 是否存在
     */
    public static boolean existsAny(ClassLoader classLoader, String... classNames) {
        if (classNames == null || classNames.length < 1) {
            return false;
        }

        for (String className : classNames) {
            if (exists(className, classLoader)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查指定的类是否都不存在。
     *
     * @param classNames 类名
     * @return 是否存在
     */
    public static boolean existsAny(String... classNames) {
        return existsAny(Thread.currentThread().getContextClassLoader(), classNames);
    }

    /**
     * 获取指定名称的类。不存在时返回 null，而不报错。
     *
     * @param classLoader 类加载器
     * @param className   类名
     * @param <T>         类型
     * @return 类
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forName(ClassLoader classLoader, String className) {
        try {
            return (Class<T>) Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取指定名称的类。不存在时返回 null，而不报错。
     *
     * @param className 类名
     * @param <T>       类型
     * @return 类
     */
    public static <T> Class<T> forName(String className) {
        if (StringUtil.isEmpty(className)) {
            return null;
        }
        return forName(Thread.currentThread().getContextClassLoader(), className);
    }

    /**
     * 基于指定类型，计算 Key。
     *
     * @param type   类型
     * @param suffix 后缀
     * @return Key
     */
    public static String resolveKey(Type type, String suffix) {
        AssertArgs.Named.notNull("type", type);
        if (type instanceof Class) {
            return ((Class<?>) type).getName() + "#" + suffix;
        } else {
            return type.getTypeName() + "#" + suffix;
        }
    }

    /**
     * 基于指定类型，计算 Key。
     *
     * @param type 类型
     * @return Key
     */
    public static String resolveKey(Type type) {
        AssertArgs.Named.notNull("type", type);
        if (type instanceof Class) {
            return ((Class<?>) type).getName();
        } else {
            return type.getTypeName();
        }
    }
}
