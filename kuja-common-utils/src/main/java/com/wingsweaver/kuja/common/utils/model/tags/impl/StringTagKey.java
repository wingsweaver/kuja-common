package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;

import java.lang.reflect.Type;

/**
 * 返回 String 类型的 {@link AbstractTagKey} 实现类。
 *
 * @author wingsweaver
 */
public class StringTagKey extends AbstractTagKey<String> implements TagKey.StringKey {
    private static final String TYPE_CODE = "String";

    public StringTagKey(String key) {
        super(key);
    }

    @Override
    public Type getValueType() {
        return String.class;
    }

    @Override
    public String getTypeCode() {
        return TYPE_CODE;
    }
}
