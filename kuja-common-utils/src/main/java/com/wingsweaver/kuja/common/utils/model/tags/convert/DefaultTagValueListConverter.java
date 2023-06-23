package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 默认用于解析列表的 {@link TagValueConverter} 实现。
 *
 * @author wingsweaver
 */
class DefaultTagValueListConverter implements TagValueConverter {
    public static final Tuple2<Boolean, Object> EMPTY_RESULT = Tuple2.of(true, Collections.emptyList());

    @Override
    public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
        if (!(type instanceof ParameterizedType)) {
            return UNHANDLED;
        }

        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type rawType = parameterizedType.getRawType();
        if (rawType == Collection.class || rawType == List.class) {
            Type itemType = parameterizedType.getActualTypeArguments()[0];
            return this.convertToList(source, itemType);
        }

        return UNHANDLED;
    }

    private Tuple2<Boolean, Object> convertToList(Object source, Type itemType) {
        Tuple2<Boolean, Object> tuple2 = UNHANDLED;

        if (source == null) {
            tuple2 = NULL_RESULT;
        } else if (source.getClass().isArray()) {
            tuple2 = this.convertToListFromArray(source, itemType);
        } else if (source instanceof Collection) {
            tuple2 = this.convertToListFromCollection((Collection<?>) source, itemType);
        } else if (source instanceof CharSequence) {
            tuple2 = this.convertToListFromString(source.toString(), itemType);
        } else {
            tuple2 = this.convertToListFromItem(source, itemType);
        }

        // 返回
        return tuple2;
    }

    private Tuple2<Boolean, Object> convertToListFromItem(Object source, Type itemType) {
        Object item = TagConversionService.toValue(source, itemType);
        if (item == null) {
            return EMPTY_RESULT;
        }

        List<Object> list = new ArrayList<>(1);
        list.add(item);
        return Tuple2.of(true, list);
    }

    private Tuple2<Boolean, Object> convertToListFromString(String text, Type itemType) {
        String[] segments = StringUtils.split(text, ',');
        if (segments.length < 1) {
            return EMPTY_RESULT;
        }

        List<Object> list = new ArrayList<>(segments.length);
        for (String segment : segments) {
            Object item = TagConversionService.toValue(segment, itemType);
            list.add(item);
        }

        return Tuple2.of(true, list);
    }

    private Tuple2<Boolean, Object> convertToListFromCollection(Collection<?> collection, Type itemType) {
        if (collection.isEmpty()) {
            return EMPTY_RESULT;
        }

        List<Object> list = new ArrayList<>(collection.size());
        for (Object item : collection) {
            Object realItem = TagConversionService.toValue(item, itemType);
            list.add(realItem);
        }

        return Tuple2.of(true, list);
    }

    private Tuple2<Boolean, Object> convertToListFromArray(Object array, Type itemType) {
        int length = Array.getLength(array);
        if (length < 1) {
            return EMPTY_RESULT;
        }

        List<Object> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            Object realItem = TagConversionService.toValue(item, itemType);
            list.add(realItem);
        }

        return Tuple2.of(true, list);
    }
}
