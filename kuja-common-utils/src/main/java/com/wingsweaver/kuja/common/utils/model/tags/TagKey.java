package com.wingsweaver.kuja.common.utils.model.tags;

import java.util.Date;
import java.util.List;

/**
 * Tag Key 的定义。
 *
 * @param <T> Tag 数据的类型
 * @author wingsweaver
 */
public interface TagKey<T> {
    /**
     * 获取 Key。
     *
     * @return Key
     */
    String getKey();

    /**
     * 获取类型的标识。<br>
     * 以便于反向生成 TagKey 实例。
     *
     * @return 类型的标识
     */
    String getTypeCode();

    /**
     * 将指定数据转换成可支持的类型。
     *
     * @param source 数据
     * @return 转换后的数据
     */
    T convertToValue(Object source);

    /**
     * 将指定数据转换成字符串。
     *
     * @param value 数据
     * @return 字符串
     */
    String saveAsText(T value);

    /**
     * 返回 String 类型的 {@link TagKey} 实现类。
     */
    interface StringKey extends TagKey<String> {
    }

    /**
     * 返回 Boolean 类型的 {@link TagKey} 实现类。
     */
    interface BooleanKey extends TagKey<Boolean> {
    }

    /**
     * 返回 Long 类型的 {@link TagKey} 实现类。
     */
    interface LongKey extends TagKey<Long> {
    }

    /**
     * 返回 Double 类型的 {@link TagKey} 实现类。
     */
    interface DoubleKey extends TagKey<Double> {
    }

    /**
     * 返回 byte[] 类型的 {@link TagKey} 实现类。
     */
    interface BytesKey extends TagKey<byte[]> {
    }

    /**
     * 返回 Date 类型的 {@link TagKey} 实现类。
     */
    interface DateKey extends TagKey<Date> {
    }

    /**
     * 返回指定枚举类型的 {@link TagKey} 实现类。
     *
     * @param <E> 枚举类型
     */
    interface EnumKey<E extends Enum<E>> extends TagKey<E> {
    }

    /**
     * 返回指定 List 类型的 {@link TagKey} 实现类。
     *
     * @param <T> 元素的数据类型
     */
    interface ArrayKey<T> extends TagKey<List<T>> {
    }

    /**
     * 返回 String 列表类型的 {@link TagKey} 实现类。
     */
    interface StringArrayKey extends ArrayKey<String> {
    }

    /**
     * 返回 Boolean 列表类型的 {@link TagKey} 实现类。
     */
    interface BooleanArrayKey extends ArrayKey<Boolean> {
    }

    /**
     * 返回 Long 列表类型的 {@link TagKey} 实现类。
     */
    interface LongArrayKey extends ArrayKey<Long> {
    }

    /**
     * 返回 Double 列表类型的 {@link TagKey} 实现类。
     */
    interface DoubleArrayKey extends ArrayKey<Double> {
    }

    /**
     * 返回 Date 列表类型的 {@link TagKey} 实现类。
     */
    interface DateArrayKey extends ArrayKey<Date> {
    }

    /**
     * 返回枚举列表类型的 {@link TagKey} 实现类。
     */
    interface EnumArrayKey<E extends Enum<E>> extends ArrayKey<E> {
    }
}
