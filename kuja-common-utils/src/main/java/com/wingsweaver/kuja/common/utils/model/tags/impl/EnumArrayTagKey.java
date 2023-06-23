package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.support.lang.reflect.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 返回指定枚举类型的数组的 {@link AbstractArrayTagKey} 实现类。
 *
 * @param <E> 枚举类型
 * @author wingsweaver
 */
public class EnumArrayTagKey<E extends Enum<E>> extends AbstractArrayTagKey<E> implements TagKey.EnumArrayKey<E> {
    private final ParameterizedType valueType;

    private final Class<E> itemType;

    private final String typeCode;

    public EnumArrayTagKey(String key, Class<E> itemType) {
        super(key);
        AssertArgs.Named.notNull("itemType", itemType);
        this.valueType = new ParameterizedTypeImpl(List.class, new Type[]{itemType});
        this.itemType = itemType;
        this.typeCode = getListTypeCode(itemType);
    }

    @Override
    public Type getValueType() {
        return this.valueType;
    }

    @Override
    public Type getItemType() {
        return this.itemType;
    }

    @Override
    public String getTypeCode() {
        return this.typeCode;
    }
}
