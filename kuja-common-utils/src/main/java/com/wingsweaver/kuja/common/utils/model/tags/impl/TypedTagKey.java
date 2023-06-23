package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.convert.TagConversionService;
import com.wingsweaver.kuja.common.utils.model.tags.convert.TagConversionServiceConfigurer;
import com.wingsweaver.kuja.common.utils.model.tags.convert.TagValueConverter;

import java.lang.reflect.Type;

/**
 * 返回指定类型的 {@link AbstractTagKey} 实现类。<br>
 * 为了能正确转换和解析数据，无比使用 {@link TagConversionServiceConfigurer}
 * 注册 {@link TagValueConverter}
 * 到 {@link TagConversionService} 中。
 *
 * @param <T> 数据类型
 * @author wingsweaver
 */
public class TypedTagKey<T> extends AbstractTagKey<T> {
    private final Class<T> valueType;

    public TypedTagKey(String key, Class<T> valueType) {
        super(key);
        this.valueType = valueType;
    }

    @Override
    public String getTypeCode() {
        return getTypeCode(this.valueType);
    }

    @Override
    public Type getValueType() {
        return this.valueType;
    }
}
