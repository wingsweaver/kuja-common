package com.wingsweaver.kuja.common.utils.support.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 自定义集合工具类。
 *
 * @author wingsweaver
 */
public final class CollectionUtil {
    private CollectionUtil() {
        // 禁止实例化
    }

    /**
     * 从数组转换成列表。
     *
     * @param items 元素的数组
     * @param <T>   元素的类型
     * @return 列表
     */
    @SafeVarargs
    public static <T> List<T> listOf(T... items) {
        if (items == null || items.length < 1) {
            return Collections.emptyList();
        } else if (items.length == 1) {
            return Collections.singletonList(items[0]);
        } else {
            return Arrays.asList(items);
        }
    }

    /**
     * 添加所有元素。
     *
     * @param target 目标集合
     * @param source 源集合
     * @param <T>    元素的类型
     */
    public static <T> void addAll(Collection<T> target, Collection<T> source) {
        // 检查参数
        if (target == null || source == null || target == source) {
            return;
        }

        target.addAll(source);
    }

    /**
     * 获取集合的大小。
     *
     * @param collection 集合
     * @return 集合的大小
     */
    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }
}
