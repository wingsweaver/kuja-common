package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;

import java.lang.reflect.Type;

/**
 * 返回 Double 类型的 {@link AbstractTagKey} 实现类。
 *
 * @author wingsweaver
 */
public class DoubleTagKey extends AbstractTagKey<Double> implements TagKey.DoubleKey {
    private static final String TYPE_CODE = "Double";

    public DoubleTagKey(String key) {
        super(key);
    }

    @Override
    public Type getValueType() {
        return Double.class;
    }

    @Override
    public String getTypeCode() {
        return TYPE_CODE;
    }
}
