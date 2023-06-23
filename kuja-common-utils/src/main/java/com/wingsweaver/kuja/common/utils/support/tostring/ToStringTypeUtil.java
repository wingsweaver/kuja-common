package com.wingsweaver.kuja.common.utils.support.tostring;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 转换字符串过程中的类型相关工具类。
 *
 * @author wingsweaver
 */
public final class ToStringTypeUtil {
    private ToStringTypeUtil() {
        // 禁止实例化
    }

    /**
     * JDK 内部的包名。
     */
    static final String[] INTERNAL_PACKAGE_NAMES = {
            "java.",
            "com.sun.",
            "jdk.",
            "sun."
    };

    /**
     * 检查是否是 JDK 内部类型。
     *
     * @param clazz 目标类型
     * @return 是否是内部类型
     */
    public static boolean isInternalType(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();
        for (String internalPackageName : INTERNAL_PACKAGE_NAMES) {
            if (packageName.startsWith(internalPackageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 简单类型。
     */
    static final Set<Class<?>> SIMPLE_TYPES = new HashSet<>(Arrays.asList(
            Void.class,
            Void.TYPE,
            Character.class,
            Character.TYPE,
            Boolean.class,
            Boolean.TYPE,
            Byte.class,
            Byte.TYPE,
            Short.class,
            Short.TYPE,
            Integer.class,
            Integer.TYPE,
            Long.class,
            Long.TYPE,
            Float.class,
            Float.TYPE,
            Double.class,
            Double.TYPE,
            CharSequence.class,
            String.class,
            Object.class
    ));

    /**
     * 检查是否是简单类型。<br>
     * 简单类型的话，不再使用反射进一步处理内部数据。
     *
     * @param clazz 目标类型
     * @return 是否是简单类型
     */
    @SuppressWarnings("RedundantIfStatement")
    public static boolean isSimpleType(Class<?> clazz) {
        if (clazz.isPrimitive() || clazz.isEnum()
                || Number.class.isAssignableFrom(clazz) || CharSequence.class.isAssignableFrom(clazz)
                || SIMPLE_TYPES.contains(clazz)) {
            return true;
        }
        return false;
    }

}
