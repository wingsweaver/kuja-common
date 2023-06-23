package com.wingsweaver.kuja.common.utils.model.tags;

import com.wingsweaver.kuja.common.utils.model.tags.impl.BooleanArrayTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.BooleanTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.BytesTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.DateTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.DoubleArrayTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.DoubleTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.EnumArrayTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.EnumTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.LongArrayTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.LongTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.StringArrayTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.StringTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.TypedArrayTagKey;
import com.wingsweaver.kuja.common.utils.model.tags.impl.TypedTagKey;

/**
 * 内置的 {@link TagKey} 的创建工具类。
 *
 * @author wingsweaver
 */
public interface TagKeys {
    /**
     * 生成 String 类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.StringKey ofString(String key) {
        return new StringTagKey(key);
    }

    /**
     * 生成 Boolean 类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.BooleanKey ofBoolean(String key) {
        return new BooleanTagKey(key);
    }

    /**
     * 生成 Long 类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.LongKey ofLong(String key) {
        return new LongTagKey(key);
    }

    /**
     * 生成 Double 类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.DoubleKey ofDouble(String key) {
        return new DoubleTagKey(key);
    }

    /**
     * 生成 Enum 类型的 {@link TagKey} 实例。
     *
     * @param key      TagKey 的标识
     * @param enumType 枚举类型
     * @param <E>      枚举类型
     * @return TagKey 的实例
     */
    static <E extends Enum<E>> TagKey.EnumKey<E> ofEnum(String key, Class<E> enumType) {
        return new EnumTagKey<>(key, enumType);
    }

    /**
     * 生成 byte[] 类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.BytesKey ofBytes(String key) {
        return new BytesTagKey(key);
    }

    /**
     * 生成 Date 类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.DateKey ofDate(String key) {
        return new DateTagKey(key);
    }

    /**
     * 生成指定类型的 {@link TagKey} 实例。
     *
     * @param key   TagKey 的标识
     * @param clazz 指定类型
     * @param <T>   指定类型
     * @return TagKey 的实例
     */
    static <T> TagKey<T> ofType(String key, Class<T> clazz) {
        return new TypedTagKey<>(key, clazz);
    }

    /**
     * 生成 String 数组类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.StringArrayKey ofStringArray(String key) {
        return new StringArrayTagKey(key);
    }

    /**
     * 生成 Boolean 数组类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.BooleanArrayKey ofBooleanArray(String key) {
        return new BooleanArrayTagKey(key);
    }

    /**
     * 生成 Long 数组类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.LongArrayKey ofLongArray(String key) {
        return new LongArrayTagKey(key);
    }

    /**
     * 生成 Double 数组类型的 {@link TagKey} 实例。
     *
     * @param key TagKey 的标识
     * @return TagKey 的实例
     */
    static TagKey.DoubleArrayKey ofDoubleArray(String key) {
        return new DoubleArrayTagKey(key);
    }

    /**
     * 生成 Enum 数组类型的 {@link TagKey} 实例。
     *
     * @param key      TagKey 的标识
     * @param enumType 枚举类型
     * @param <E>      枚举类型
     * @return TagKey 的实例
     */
    static <E extends Enum<E>> TagKey.EnumArrayKey<E> ofEnumArray(String key, Class<E> enumType) {
        return new EnumArrayTagKey<>(key, enumType);
    }

    /**
     * 生成指定类型的数组 {@link TagKey} 实例。
     *
     * @param key      TagKey 的标识
     * @param itemType 元素类型
     * @param <T>      元素类型
     * @return TagKey 的实例
     */
    static <T> TagKey.ArrayKey<T> ofTypedArray(String key, Class<T> itemType) {
        return new TypedArrayTagKey<>(key, itemType);
    }
}
