package com.wingsweaver.kuja.common.utils.model.attributes;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * 内容可变的属性定义。
 *
 * @param <K> 属性名称的类型
 * @author wingsweaver
 */
public interface MutableAttributes<K> extends Attributes<K> {
    /**
     * 设置指定属性的值。
     *
     * @param key   属性名称
     * @param value 属性的值
     */
    void setAttribute(K key, Object value);

    /**
     * 更新指定属性的值。
     *
     * @param key     属性名称
     * @param updater 更新函数
     * @param <V>     属性的类型
     * @return 更新后的属性值
     */
    <V> V updateAttribute(K key, BiFunction<K, V, V> updater);

    /**
     * 设置指定属性的值，如果属性不存在则设置为默认值。
     *
     * @param key           属性名称
     * @param fallbackValue 默认值
     * @param <V>           属性的类型
     * @return 原来的属性的值
     */
    <V> V setAttributeIfAbsent(K key, V fallbackValue);

    /**
     * 删除指定属性。
     *
     * @param key 属性名称
     */
    void removeAttribute(K key);

    /**
     * 清空所有属性。
     */
    void clearAttributes();

    /**
     * 从指定字典中导入属性值。
     *
     * @param map       字典
     * @param overwrite 是否覆盖已有的属性
     */
    void importMap(Map<K, ?> map, boolean overwrite);

    /**
     * 从指定字典创建一个可写属性。
     *
     * @param map 字典
     * @param <K> 属性名称的类型
     * @return 只读属性的实例
     */
    static <K> MutableAttributes<K> of(Map<K, ?> map) {
        return new MapMutableAttributes<>(map);
    }

    /**
     * 创建一个继承自指定属性的可写属性。
     *
     * @param parent 父属性
     * @param <K>    属性名称的类型
     * @return 只读属性的实例
     */
    static <K> MutableAttributes<K> layered(Attributes<K> parent) {
        return new LayeredMutableAttributes<>(parent);
    }
}
