package com.wingsweaver.kuja.common.utils.support.lang.reflect;

import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * 反射工具类。
 *
 * @author wingsweaver
 */
public final class ReflectUtil {
    private static final char CHAR_A = 'A';

    private static final char CHAR_Z = 'Z';

    private ReflectUtil() {
        // 禁止实例化
    }

    /**
     * 是否可以反射访问非 Public 类型的数据。
     */
    public static final boolean CAN_REFLECT_NON_PUBLIC;

    static {
        boolean canReflectNonPublic = true;
        try {
            Class<?> clazz = ClassUtil.class;
            Constructor<?> constructor = clazz.getConstructors()[0];
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (Exception ex) {
            canReflectNonPublic = false;
        }
        CAN_REFLECT_NON_PUBLIC = canReflectNonPublic;
    }

    /**
     * 前缀 get。
     */
    public static final String PREFIX_GET = "get";

    /**
     * 前缀 is。
     */
    public static final String PREFIX_IS = "is";

    /**
     * 以严格模式，从 Method 解析属性名称。<br>
     * 支持以 get 和 is 开头这两种类型的 Method。<br>
     * 注意 is 开头的话，必须是 boolean 类型的。
     *
     * @param method     方法实例
     * @param publicOnly 是否只允许 public 方法
     * @return 属性名称。如果没有合规的话，那么返回 null。
     */
    public static String resolveFieldNameStrict(Method method, boolean publicOnly) {
        Objects.requireNonNull(method, "method is null");

        // 检查是否符合要求
        int modifiers = method.getModifiers();
        if (method.getParameterCount() > 0 || Modifier.isStatic(modifiers) || method.getReturnType() == void.class) {
            return null;
        }
        if (publicOnly && !Modifier.isPublic(modifiers)) {
            return null;
        }
        return resolveFieldName(method);
    }

    /**
     * 从 Method 解析属性名称。<br>
     * 支持以 get 和 is 开头这两种类型的 Method。<br>
     * 注意 is 开头的话，必须是 boolean 类型的。
     *
     * @param method 方法实例
     * @return 属性名称。如果没有合规的话，那么返回 null。
     */
    public static String resolveFieldName(Method method) {
        Objects.requireNonNull(method, "method is null");
        String fieldName = null;

        // 从方法名中截取字段名
        String methodName = method.getName();
        if (methodName.startsWith(PREFIX_GET)) {
            fieldName = methodName.substring(PREFIX_GET.length());
        } else if (methodName.startsWith(PREFIX_IS) && Boolean.TYPE.isAssignableFrom(method.getReturnType())) {
            fieldName = methodName.substring(PREFIX_IS.length());
        }

        // 计算字段名称
        if (StringUtils.isNotEmpty(fieldName)) {
            char firstChar = fieldName.charAt(0);
            if (firstChar >= CHAR_A && firstChar <= CHAR_Z) {
                fieldName = StringUtils.uncapitalize(fieldName);
            } else {
                fieldName = null;
            }
        }

        // 返回
        return fieldName;
    }
}
