package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import lombok.Getter;

import java.lang.reflect.Type;

/**
 * 返回 byte[] 类型的 {@link AbstractTagKey} 实现类。
 *
 * @author wingsweaver
 */
@Getter
public class BytesTagKey extends AbstractTagKey<byte[]> implements TagKey.BytesKey {
    public BytesTagKey(String key) {
        super(key);
    }

    @Override
    public Type getValueType() {
        return byte[].class;
    }

    @Override
    public String getTypeCode() {
        return "byte[]";
    }
}
