package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.convert.TagConversionService;
import com.wingsweaver.kuja.common.utils.model.tags.convert.TagConversionServiceConfigurer;
import com.wingsweaver.kuja.common.utils.model.tags.convert.TagValueConverter;
import com.wingsweaver.kuja.common.utils.support.lang.reflect.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 返回指定类型数据的数组的 {@link AbstractArrayTagKey} 实现类。
 * 为了能正确转换和解析数据，无比使用 {@link TagConversionServiceConfigurer}
 * 注册 {@link TagValueConverter}
 * 到 {@link TagConversionService} 中。
 *
 * @param <T> 元素的数据类型
 * @author wingsweaver
 */
public class TypedArrayTagKey<T> extends AbstractArrayTagKey<T> implements TagKey.ArrayKey<T> {
    private final Class<T> itemType;

    private final String typeCode;

    private final ParameterizedType valueType;

    public TypedArrayTagKey(String key, Class<T> itemType) {
        super(key);
        this.itemType = itemType;
        this.valueType = new ParameterizedTypeImpl(List.class, new Type[]{itemType});
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
