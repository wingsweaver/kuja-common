package com.wingsweaver.kuja.common.web.convert;

import com.wingsweaver.kuja.common.utils.model.tags.convert.AbstractTagValueConverter;
import com.wingsweaver.kuja.common.utils.model.tags.convert.TagValueConverter;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Type;

/**
 * 用于转换 {@link HttpStatus} 的 {@link TagValueConverter} 实现。
 *
 * @author wingsweaver
 */
public class HttpStatusConverter extends AbstractTagValueConverter {
    @Override
    protected Tuple2<Boolean, Object> convertToInternal(Object source, Type type) {
        if (source == null) {
            return NULL_RESULT;
        }

        // 根据 source 的类型进行处理
        HttpStatus status = null;
        if (source instanceof HttpStatus) {
            status = (HttpStatus) source;
        } else if (source instanceof Number) {
            status = fromInteger(((Number) source).intValue());
        } else if (source instanceof CharSequence) {
            String text = source.toString().trim();
            if (StringUtils.isNumeric(text)) {
                status = fromInteger(Integer.parseInt(text));
            } else {
                status = HttpStatus.valueOf(text.toUpperCase());
            }
        } else {
            return UNHANDLED;
        }

        // 返回转换结果
        return Tuple2.of(true, status);
    }

    @Override
    protected boolean support(Object source, Type type) {
        return type == HttpStatus.class;
    }

    private static HttpStatus fromInteger(int value) {
        return HttpStatus.valueOf(value);
    }

    static final HttpStatusConverter INSTANCE = new HttpStatusConverter();

    /**
     * 将指定数据转换成 {@link HttpStatus} 。
     *
     * @param object 要转换的数据
     * @return 转换结果
     */
    public static HttpStatus convert(Object object) {
        Tuple2<Boolean, Object> tuple2 = INSTANCE.convertTo(object, HttpStatus.class);
        if (Boolean.TRUE.equals(tuple2.getT1())) {
            return (HttpStatus) tuple2.getT2();
        }
        return null;
    }
}
