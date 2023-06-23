package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;

import java.lang.reflect.Type;

/**
 * 返回指定枚举类型的 {@link AbstractTagKey} 实现类。
 *
 * @author wingsweaver
 */
public class EnumTagKey<E extends Enum<E>> extends AbstractTagKey<E> implements TagKey.EnumKey<E> {
    private final Class<E> valueType;

    private final String typeCode;

    public EnumTagKey(String key, Class<E> enumType) {
        super(key);
        this.valueType = enumType;
        this.typeCode = getTypeCode(enumType);
    }

    @Override
    public Type getValueType() {
        return valueType;
    }

    @Override
    public String getTypeCode() {
        return this.typeCode;
    }
}
