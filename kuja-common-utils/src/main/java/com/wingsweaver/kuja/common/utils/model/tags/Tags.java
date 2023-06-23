package com.wingsweaver.kuja.common.utils.model.tags;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Tags 定义。<br>
 * Tags 类似于 OTEL 的 Tags 定义，主要是为了跨进程无损传输数据。<br>
 * <p>
 * Tags 是一组 Key-Value 对，其中 Key 是 TagKey，Value 是任意类型的数据。<br>
 * TagKey 除了指定 Key 的名称之外，还指定 Value 的类型。<br>
 * Value 也可以通过 TagKey 被转换成字符串，或者从字符串进行解析。
 *
 * @author wingsweaver
 */
public interface Tags {
    /**
     * 判断是否为空。
     *
     * @return 是否为空
     */
    boolean isEmpty();

    /**
     * 获取 Tag 数量。
     *
     * @return Tag 数量
     */
    int size();

    /**
     * 获取所有的 Key 的列表。
     *
     * @return Key 的列表
     */
    Collection<TagKey<?>> getTagKeys();

    /**
     * 查找第一个符合条件的 TagKey。
     *
     * @param predicate 条件
     * @return 符合条件的 TagKey。如果不存在的话，返回 null。
     */
    TagKey<?> findTagKey(Predicate<TagKey<?>> predicate);

    /**
     * 查找所有符合条件的 TagKey。
     *
     * @param predicate 条件
     * @return 符合条件的 TagKey 列表
     */
    Collection<TagKey<?>> findTagKeys(Predicate<TagKey<?>> predicate);

    /**
     * 判读是否包含指定 Tag。
     *
     * @param key Tag 的 Key
     * @return 是否包含
     */
    boolean hasTag(TagKey<?> key);

    /**
     * 获取指定 Tag 的数据。
     *
     * @param key Tag 的 Key
     * @param <T> Tag 数据的类型
     * @return Tag 数据。如果不存在的话，返回 null。
     */
    <T> T getTag(TagKey<T> key);

    /**
     * 获取指定 Tag 的数据。<br>
     * 如果不存在的话，返回指定的默认值。
     *
     * @param key          Tag 的 Key
     * @param defaultValue 默认值
     * @param <T>          Tag 数据的类型
     * @return Tag 数据
     */
    <T> T getTag(TagKey<T> key, T defaultValue);

    /**
     * 设置指定 Tag 的数据。<br>
     * 会自动将指定的数据，通过 {@link TagKey#convertToValue(Object)} 转换成 TagKey 支持的类型。
     *
     * @param key   Tag 的 Key
     * @param value Tag 数据。会自动转换成 TagKey 支持的类型。
     * @param <T>   Tag 数据的类型
     */
    <T> void setTag(TagKey<T> key, Object value);

    /**
     * 删除指定 Tag 的数据。
     *
     * @param key Tag 的 Key
     */
    void removeTag(TagKey<?> key);

    /**
     * 清除所有的 Tag。
     */
    void clearTags();

    /**
     * 映射成 TagKey 和 Tag 数据的字典。
     *
     * @return 字典
     */
    Map<TagKey<?>, Object> asMap();

    /**
     * 遍历所有的 TagKey 和 Tag 数据。
     *
     * @param consumer 遍历函数
     */
    void forEach(BiConsumer<TagKey<?>, Object> consumer);

    /**
     * 空的 Tags 实现类的实例。
     */
    Tags EMPTY = EmptyTags.INSTANCE;

    /**
     * 生成指定初始大小的 Tags 实例。
     *
     * @param initialCapacity 初始大小
     * @return Tags 实例
     */
    static Tags of(int initialCapacity) {
        return new DefaultTags(initialCapacity);
    }

    /**
     * 基于指定的 TagKey 和 Tag 数据的字典生成 Tags 实例。
     *
     * @param map 字典
     * @return Tags 实例
     */
    static Tags of(Map<TagKey<?>, Object> map) {
        return new DefaultTags(map);
    }

    /**
     * 生成指定 Tags 实例的克隆实例。
     *
     * @param tags Tags 实例
     * @return 克隆实例
     */
    static Tags clone(Tags tags) {
        return new DefaultTags(tags);
    }

    /**
     * 基于指定的 Tags 生成层级 Tags 实例。
     *
     * @param parent 父级 Tags
     * @return Tags 实例
     */
    static Tags layered(Tags parent) {
        return new LayeredTags(parent);
    }
}
