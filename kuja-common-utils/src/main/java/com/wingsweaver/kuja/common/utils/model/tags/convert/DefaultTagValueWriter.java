package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.codec.binary.BinaryCodec;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringBuilder;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * 默认的 {@link TagValueWriter} 实现。
 *
 * @author wingsweaver
 */
class DefaultTagValueWriter implements TagValueWriter {
    private static final Tuple2<Boolean, String> NULL_RESULT = new Tuple2<>(true, null);

    @Override
    public Tuple2<Boolean, String> saveAsText(Object value) {
        if (value == null) {
            return NULL_RESULT;
        }

        // 按照不同类型进行处理
        String text;
        if (value instanceof byte[]) {
            text = BinaryCodec.encodeAsNamed((byte[]) value);
        } else if (value.getClass().isArray()) {
            text = arrayToString(value);
        } else if (value instanceof Collection) {
            text = collectionToString((Collection<?>) value);
        } else if (value instanceof Date) {
            text = Long.toString(((Date) value).getTime());
        } else if (value instanceof Instant) {
            text = Long.toString(((Instant) value).toEpochMilli());
        } else if (value instanceof Calendar) {
            text = Long.toString(((Calendar) value).getTimeInMillis());
        } else {
            text = ToStringBuilder.toString(value);
        }

        // 返回
        return Tuple2.of(true, text);
    }

    private String collectionToString(Collection<?> collection) {
        if (collection.isEmpty()) {
            return "";
        }

        // 执行转换
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Object item : collection) {
            if (index > 0) {
                sb.append(',');
            }
            sb.append(TagConversionService.saveValueAsText(item));
            index++;
        }

        // 返回结果
        return sb.toString();
    }

    private String arrayToString(Object array) {
        int length = Array.getLength(array);
        if (length < 1) {
            return "";
        }

        // 执行转换
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < length; index++) {
            if (index > 0) {
                sb.append(',');
            }
            Object item = Array.get(array, index);
            sb.append(TagConversionService.saveValueAsText(item));
        }

        // 返回结果
        return sb.toString();
    }
}
