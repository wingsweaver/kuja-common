package com.wingsweaver.kuja.common.utils.exception;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 带扩展数据的异常定义。
 *
 * @param <E> 异常类型
 * @author wingsweaver
 */
public interface Extended<E extends Throwable> {
    /**
     * 获取扩展数据的键的集合。
     *
     * @return 扩展数据的键的集合
     */
    Collection<String> extendedKeys();

    /**
     * 获取扩展数据的字典。
     *
     * @return 扩展数据的字典
     */
    Map<String, Object> extendedMap();

    /**
     * 遍历扩展数据。
     *
     * @param consumer 遍历函数
     */
    void forEachExtended(BiConsumer<String, ?> consumer);

    /**
     * 获取扩展数据。
     *
     * @param key 键
     * @return 扩展数据
     */
    <T> T getExtendedAttribute(String key);

    /**
     * 获取扩展数据，如果不存在则返回默认值。
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 扩展数据
     */
    <T> T getExtendedAttribute(String key, T defaultValue);

    /**
     * 设置扩展数据。
     *
     * @param key   键
     * @param value 值
     */
    void setExtendedAttribute(String key, Object value);

    /**
     * 设置扩展数据，如果不存在则设置。
     *
     * @param key   键
     * @param value 值
     * @return 扩展数据
     */
    <T> T setExtendedAttributeIfAbsent(String key, T value);

    /**
     * 移除扩展数据。
     *
     * @param key 键
     */
    void removeExtendedAttribute(String key);

    /**
     * 设置扩展数据。
     *
     * @param key   键
     * @param value 值
     * @return 本实例
     */
    E withExtendedAttribute(String key, Object value);

    /**
     * 设置扩展数据。
     *
     * @param map 扩展数据
     * @return 本实例
     */
    E withExtendedAttribute(Map<String, ?> map);
}
