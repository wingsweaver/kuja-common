package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * 返回 Date 类型的 {@link AbstractTagKey} 实现类。
 *
 * @author wingsweaver
 */
public class DateTagKey extends AbstractTagKey<Date> implements TagKey.DateKey {
    private static final String TYPE_CODE = "Date";

    public DateTagKey(String key) {
        super(key);
    }

    @Override
    public Type getValueType() {
        return Date.class;
    }

    @Override
    public String getTypeCode() {
        return TYPE_CODE;
    }
}
