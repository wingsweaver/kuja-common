package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.support.lang.reflect.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 返回 String 数组的 {@link AbstractArrayTagKey} 实现类。
 *
 * @author wingsweaver
 */
public class StringArrayTagKey extends AbstractArrayTagKey<String> implements TagKey.StringArrayKey {
    private static final String TYPE_CODE = getListTypeCode("String");

    private static final ParameterizedType VALUE_TYPE = new ParameterizedTypeImpl(List.class, new Type[]{String.class});

    public StringArrayTagKey(String key) {
        super(key);
    }

    @Override
    public Type getValueType() {
        return VALUE_TYPE;
    }

    @Override
    public Type getItemType() {
        return String.class;
    }

    @Override
    public String getTypeCode() {
        return TYPE_CODE;
    }
}
