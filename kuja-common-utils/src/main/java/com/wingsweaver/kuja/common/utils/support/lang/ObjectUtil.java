package com.wingsweaver.kuja.common.utils.support.lang;

import com.wingsweaver.kuja.common.utils.support.EmptyChecker;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Object 工具类。
 *
 * @author wingsweaver
 */
public final class ObjectUtil {
    private ObjectUtil() {
        // 禁止实例化
    }

    /**
     * 将指定实例转换成目标类型。
     *
     * @param object 目标实例
     * @param clazz  目标类型
     * @param <T>    目标类型
     * @return 如果可以转换的话，返回目标类型的实例；否则返回 null。
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object, Class<T> clazz) {
        T casted = null;
        if (object != null && clazz.isAssignableFrom(object.getClass())) {
            casted = (T) object;
        }
        return casted;
    }

    /**
     * 原始的 {@link Object#toString()} 的复刻版。
     *
     * @param object 目标对象
     * @return 字符串
     */
    public static String originalToString(Object object) {
        if (object == null) {
            return null;
        } else {
            return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
        }
    }

    /**
     * 判断指定对象是否不为空。
     *
     * @param object 指定对象
     * @return 如果指定对象不为空，则返回 true；否则返回 false。
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * 判断指定对象是否为空。
     *
     * @param object 指定对象
     * @return 如果指定对象为空，则返回 true；否则返回 false。
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }

        if (object instanceof EmptyChecker) {
            return ((EmptyChecker) object).isEmpty();
        } else {
            // 尝试通过 isEmpty() 方法判断
            Boolean isEmpty = isEmptyByMethod(object);
            if (isEmpty != null) {
                return isEmpty;
            } else {
                // 没有 isEmpty 方法的话，使用默认的判定方式
                return ObjectUtils.isEmpty(object);
            }
        }
    }

    /**
     * 通过 isEmpty() 方法，判断是否为空。
     *
     * @param object 指定对象
     * @return 如果指定对象为空，则返回 true；否则返回 false。
     */
    private static Boolean isEmptyByMethod(Object object) {
        try {
            Method[] methods = object.getClass().getMethods();
            for (Method method : methods) {
                if ("isEmpty".equals(method.getName()) && method.getParameterCount() == 0
                        && method.getReturnType() == boolean.class) {
                    try {
                        return (Boolean) method.invoke(object);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        } catch (Exception ignored) {
            // 忽略此处的错误
        }

        // 默认返回没有处理
        return null;
    }

    /**
     * 检查指定的对象是否在指定的范围内。
     *
     * @param value 要检查的对象
     * @param min   最小值
     * @param max   最大值
     * @param <T>   对象类型
     * @return 如果在指定的范围内，则返回 true；否则返回 false。
     */
    public static <T extends Comparable<T>> boolean between(T value, T min, T max) {
        if (value.compareTo(min) >= 0) {
            return value.compareTo(max) <= 0;
        } else {
            return value.compareTo(max) >= 0;
        }
    }

    /**
     * 检查指定的对象是否在不指定的范围内。
     *
     * @param value 要检查的对象
     * @param min   最小值
     * @param max   最大值
     * @param <T>   对象类型
     * @return 如果在不指定的范围内，则返回 true；否则返回 false。
     */
    public static <T extends Comparable<T>> boolean notBetween(T value, T min, T max) {
        return !between(value, min, max);
    }

    /**
     * 检查指定的对象是否在指定的数组内。
     *
     * @param value   要检查的对象
     * @param targets 指定的数组
     * @return 如果在指定的数组内，则返回 true；否则返回 false。
     */
    public static boolean in(Object value, Object[] targets) {
        if (targets == null) {
            return false;
        }

        for (Object target : targets) {
            if (value.equals(target)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查指定的对象是否在指定的集合内。
     *
     * @param value   要检查的对象
     * @param targets 指定的集合
     * @return 如果在指定的集合内，则返回 true；否则返回 false。
     */
    public static boolean in(Object value, Collection<?> targets) {
        if (targets == null) {
            return false;
        }

        for (Object target : targets) {
            if (value.equals(target)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 如果指定的对象为 null，则返回默认值；否则返回指定的对象。
     *
     * @param value        指定的对象
     * @param defaultValue 默认值
     * @param <T>          对象类型
     * @return 如果指定的对象为 null，则返回默认值；否则返回指定的对象。
     */
    public static <T> T notNullOr(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * 如果指定的对象为 null，则返回默认值；否则返回指定的对象。
     *
     * @param value                指定的对象
     * @param defaultValueSupplier 默认值的计算函数
     * @param <T>                  对象类型
     * @return 如果指定的对象为 null，则返回默认值；否则返回指定的对象。
     */
    public static <T> T notNullOr(T value, Supplier<T> defaultValueSupplier) {
        return value == null ? defaultValueSupplier.get() : value;
    }
}
