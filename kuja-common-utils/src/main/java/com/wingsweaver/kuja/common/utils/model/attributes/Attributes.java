package com.wingsweaver.kuja.common.utils.model.attributes;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 只读属性的定义。
 *
 * @param <K> 属性名称的类型
 * @author wingsweaver
 */
public interface Attributes<K> {
    /**
     * 获取所有的属性名称。
     *
     * @return 所有的属性名称
     */
    Collection<K> getKeys();

    /**
     * 检查指定的属性是否存在。
     *
     * @param key 属性名称
     * @return 如果存在则返回 true，否则返回 false
     */
    boolean hasAttribute(K key);

    /**
     * 获取指定属性的值。
     *
     * @param key 属性名称
     * @param <V> 属性的类型
     * @return 属性的值
     */
    <V> V getAttribute(K key);

    /**
     * 获取指定属性的值，如果属性不存在则返回默认值。
     *
     * @param key          属性名称
     * @param defaultValue 默认值
     * @param <V>          属性的类型
     * @return 属性的值
     */
    <V> V getAttribute(K key, V defaultValue);

    /**
     * 获取指定属性的值，如果属性不存在则返回默认值。
     *
     * @param key                  属性名称
     * @param defaultValueSupplier 默认值的计算函数
     * @param <V>                  属性的类型
     * @return 属性的值
     */
    <V> V getAttribute(K key, Function<K, V> defaultValueSupplier);

    /**
     * 导出所有的属性到一个 Map 中。
     *
     * @return 包含所有属性的 Map
     */
    Map<K, ?> asMap();

    /**
     * 生成一个可以用于修改数据的 {@link AttributesBuilder} 实例。
     *
     * @return {@link AttributesBuilder} 实例
     */
    default AttributesBuilder<K> mutate() {
        return new AttributesBuilder<>(this);
    }

    /**
     * 遍历所有的属性。
     *
     * @param consumer 属性的消费函数
     */
    void forEach(BiConsumer<K, ?> consumer);

    /**
     * 从指定字典创建一个只读属性。
     *
     * @param map 字典
     * @param <K> 属性名称的类型
     * @return 只读属性的实例
     */
    static <K> Attributes<K> of(Map<K, ?> map) {
        return new MapAttributes<>(map);
    }

    /**
     * 创建一个空的只读属性。
     *
     * @param <K> 属性名称的类型
     * @return 空的只读属性
     */
    @SuppressWarnings("unchecked")
    static <K> Attributes<K> empty() {
        return (Attributes<K>) MapAttributes.EMPTY;
    }
}
