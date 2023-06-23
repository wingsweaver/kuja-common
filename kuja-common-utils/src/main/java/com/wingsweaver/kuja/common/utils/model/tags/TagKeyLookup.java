package com.wingsweaver.kuja.common.utils.model.tags;

/**
 * 查找 {@link TagKey} 的接口定义。
 *
 * @author wingsweaver
 */
public interface TagKeyLookup {
    /**
     * 根据 key 查找 {@link TagKey}。
     *
     * @param key key
     * @param <T> Tag 数据的类型
     * @return TagKey 的实例
     */
    <T> TagKey<T> find(String key);

    /**
     * 根据 key 和 typeCode 查找 {@link TagKey}。
     *
     * @param key      key
     * @param typeCode typeCode
     * @param <T>      Tag 数据的类型
     * @return TagKey 的实例
     */
    <T> TagKey<T> find(String key, String typeCode);
}
