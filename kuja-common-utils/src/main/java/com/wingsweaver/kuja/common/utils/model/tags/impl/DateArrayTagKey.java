package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.support.lang.reflect.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * 返回 Date 数组的 {@link AbstractArrayTagKey} 实现类。
 *
 * @author wingsweaver
 */
public class DateArrayTagKey extends AbstractArrayTagKey<Date> implements TagKey.DateArrayKey {
    private static final String TYPE_CODE = getListTypeCode("Date");

    private static final ParameterizedType VALUE_TYPE = new ParameterizedTypeImpl(List.class, new Type[]{Date.class});

    public DateArrayTagKey(String key) {
        super(key);
    }

    @Override
    public Type getValueType() {
        return VALUE_TYPE;
    }

    @Override
    public Type getItemType() {
        return Date.class;
    }

    @Override
    public String getTypeCode() {
        return TYPE_CODE;
    }
}
