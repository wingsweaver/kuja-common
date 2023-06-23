package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.convert.TagConversionService;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * {@link TagKey} 实现类的基类。
 *
 * @param <T> 标签值类型
 * @author wingsweaver
 */
public abstract class AbstractTagKey<T> implements TagKey<T> {
    /**
     * Tag Key。
     */
    private final String key;

    /**
     * 构造方法。
     *
     * @param key Tag Key
     */
    protected AbstractTagKey(String key) {
        AssertArgs.Named.notEmpty("key", key);
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.getKey() + "@" + getTypeCode(getValueType());
    }

    @Override
    public T convertToValue(Object source) {
        return TagConversionService.toValue(source, getValueType());
    }

    /**
     * 获取值的类型。
     *
     * @return 值的类型
     */
    public abstract Type getValueType();

    @Override
    public String saveAsText(T value) {
        return TagConversionService.saveValueAsText(value);
    }

    /**
     * 获取类型的标识。
     *
     * @param type 类型
     * @return 类型的标识
     */
    protected static String getTypeCode(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isEnum()) {
                return "enum:" + clazz.getName();
            } else {
                return clazz.getName();
            }
        }
        return type.getTypeName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractTagKey)) {
            return false;
        }
        AbstractTagKey<?> that = (AbstractTagKey<?>) o;
        return Objects.equals(this.getKey(), that.getKey())
                && Objects.equals(this.getValueType(), that.getValueType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey(), this.getValueType());
    }
}
