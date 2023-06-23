package com.wingsweaver.kuja.common.utils.support.lang;

import java.lang.reflect.Array;

/**
 * 数组工具类。
 *
 * @author wingsweaver
 */
public final class ArrayUtil {
    private ArrayUtil() {
        // 禁止实例化
    }

    /**
     * 检查指定数组是否为空。
     *
     * @param array 目标数组
     * @param <T>   元素类型
     * @return 是否为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查指定数组是否不为空。
     *
     * @param array 目标数组
     * @param <T>   元素类型
     * @return 是否不为空
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    /**
     * 检查指定数组是否为空。
     *
     * @param array 目标数组
     * @return 是否为空
     */
    public static boolean isEmpty(Object array) {
        return array == null || Array.getLength(array) == 0;
    }

    /**
     * 检查指定数组是否不为空。
     *
     * @param array 目标数组
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object array) {
        return !isEmpty(array);
    }
}
