package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;

import java.lang.reflect.Type;

/**
 * 返回 Long 类型的 {@link AbstractTagKey} 实现类。
 *
 * @author wingsweaver
 */
public class LongTagKey extends AbstractTagKey<Long> implements TagKey.LongKey {
    private static final String TYPE_CODE = "Long";

    public LongTagKey(String key) {
        super(key);
    }

    @Override
    public Type getValueType() {
        return Long.class;
    }

    @Override
    public String getTypeCode() {
        return TYPE_CODE;
    }
}
