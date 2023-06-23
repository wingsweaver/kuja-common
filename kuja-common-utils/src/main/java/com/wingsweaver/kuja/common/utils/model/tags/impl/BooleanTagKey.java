package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;

import java.lang.reflect.Type;

/**
 * 返回 Boolean 类型的 {@link AbstractTagKey} 实现类。
 *
 * @author wingsweaver
 */
public class BooleanTagKey extends AbstractTagKey<Boolean> implements TagKey.BooleanKey {
    private static final String TYPE_CODE = "Boolean";

    public BooleanTagKey(String key) {
        super(key);
    }

    @Override
    public Type getValueType() {
        return Boolean.class;
    }

    @Override
    public String getTypeCode() {
        return TYPE_CODE;
    }
}
