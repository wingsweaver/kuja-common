package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.Valued;
import com.wingsweaver.kuja.common.utils.support.codec.binary.BinaryCodec;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认的 {@link TagValueConverter} 实现。
 *
 * @author wingsweaver
 */
class DefaultTagValueConverter implements TagValueConverter {
    @Override
    public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
        // 检查参数
        if (!(type instanceof Class)) {
            return UNHANDLED;
        }
        if (source == null) {
            return NULL_RESULT;
        }

        // 按照同类型转换
        Class<?> clazz = (Class<?>) type;
        if (clazz.isInstance(source)) {
            return Tuple2.of(true, source);
        }

        // 转换枚举类型
        if (clazz.isEnum()) {
            return convertToEnum(source, clazz);
        }

        // 按照特定类型进行转换
        TagValueConverter converter = TYPE_CONVERTER_MAP.get(clazz);
        if (converter != null) {
            return converter.convertTo(source, clazz);
        }

        // 无法转换，返回 null
        return UNHANDLED;
    }

    private static final Map<Class<?>, TagValueConverter> TYPE_CONVERTER_MAP;

    static {
        Map<Class<?>, TagValueConverter> typeConverterMap = new HashMap<>(BufferSizes.TINY);
        // 转换成字符串
        ToStringConverter toStringConverter = new ToStringConverter();
        typeConverterMap.put(CharSequence.class, toStringConverter);
        typeConverterMap.put(String.class, toStringConverter);

        // 转换成 Boolean
        ToBooleanConverter toBooleanConverter = new ToBooleanConverter();
        typeConverterMap.put(Boolean.class, toBooleanConverter);
        typeConverterMap.put(boolean.class, toBooleanConverter);

        // 转换成 Long
        ToLongConverter toLongConverter = new ToLongConverter();
        typeConverterMap.put(Long.class, toLongConverter);
        typeConverterMap.put(long.class, toLongConverter);
        typeConverterMap.put(Integer.class, toLongConverter);
        typeConverterMap.put(int.class, toLongConverter);

        // 转换成 Double
        ToDoubleConverter toDoubleConverter = new ToDoubleConverter();
        typeConverterMap.put(Double.class, toDoubleConverter);
        typeConverterMap.put(double.class, toDoubleConverter);

        // 转换成 byte[]
        ToBytesConverter toBytesConverter = new ToBytesConverter();
        typeConverterMap.put(byte[].class, toBytesConverter);

        // 转换成 Date
        ToDateConverter toDateConverter = new ToDateConverter();
        typeConverterMap.put(Date.class, toDateConverter);

        // 保存字典数据
        TYPE_CONVERTER_MAP = Collections.unmodifiableMap(typeConverterMap);
    }

    static class ToStringConverter implements TagValueConverter {
        @Override
        public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
            return Tuple2.of(true, ToStringBuilder.toString(source));
        }
    }

    static class ToBooleanConverter implements TagValueConverter {
        private static final String TRUE = "true";

        private static final String FALSE = "false";

        @Override
        public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
            if (source instanceof Number) {
                return Tuple2.of(true, ((Number) source).intValue() != 0);
            }
            if (source instanceof CharSequence) {
                String text = source.toString().trim();
                if (TRUE.equalsIgnoreCase(text)) {
                    return Tuple2.of(true, true);
                } else if (FALSE.equalsIgnoreCase(text)) {
                    return Tuple2.of(true, false);
                }
            }

            // 无法转换，返回 null
            return UNHANDLED;
        }
    }

    static class ToLongConverter implements TagValueConverter {
        @Override
        public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
            if (source instanceof Number) {
                return Tuple2.of(true, ((Number) source).longValue());
            }

            if (source instanceof CharSequence) {
                return Tuple2.of(true, Long.parseLong(source.toString()));
            }

            // 无法转换，返回 null
            return UNHANDLED;
        }
    }

    static class ToDoubleConverter implements TagValueConverter {
        @Override
        public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
            if (source instanceof Number) {
                return Tuple2.of(true, ((Number) source).doubleValue());
            }

            if (source instanceof CharSequence) {
                return Tuple2.of(true, Double.parseDouble(source.toString()));
            }

            // 无法转换，返回 null
            return UNHANDLED;
        }
    }

    static class ToBytesConverter implements TagValueConverter {
        @Override
        public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
            if (source instanceof CharSequence) {
                return Tuple2.of(true, BinaryCodec.parseAndDecode(source.toString()));
            }

            // 无法转换，返回 null
            return UNHANDLED;
        }
    }

    static class ToDateConverter implements TagValueConverter {
        private static final String[] DATE_FORMATS = {
                DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.getPattern(),
                DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern(),
                DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern(),
                DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.getPattern(),
        };

        @Override
        public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
            if (source instanceof Number) {
                return Tuple2.of(true, new Date(((Number) source).longValue()));
            }

            if (source instanceof Calendar) {
                return Tuple2.of(true, ((Calendar) source).getTime());
            }

            if (source instanceof Instant) {
                return Tuple2.of(true, Date.from((Instant) source));
            }

            if (source instanceof CharSequence) {
                String text = source.toString();
                if (StringUtils.isNumeric(text)) {
                    Date date = new Date(Long.parseLong(text));
                    return Tuple2.of(true, date);
                } else {
                    try {
                        Date date = DateUtils.parseDate(text, DATE_FORMATS);
                        return Tuple2.of(true, date);
                    } catch (Exception e) {
                        throw new TagConversionException("Failed to parse date: " + text, e);
                    }
                }
            }

            // 无法转换，返回 null
            return UNHANDLED;
        }
    }

    private static Tuple2<Boolean, Object> convertToEnum(Object source, Class<?> clazz) {
        String sourceString = source.toString();
        boolean isValued = Valued.class.isAssignableFrom(clazz);
        for (Object enumConstant : clazz.getEnumConstants()) {
            // 按照枚举的名称来处理
            if (enumConstant.toString().equalsIgnoreCase(sourceString)) {
                return Tuple2.of(true, enumConstant);
            }
            // 按照枚举的值来处理
            if (isValued) {
                Object enumValue = ((Valued<?>) enumConstant).getValue();
                if (enumValue != null && enumValue.toString().equals(sourceString)) {
                    return Tuple2.of(true, enumConstant);
                }
            }
        }

        // 无法转换，返回 null
        return NULL_RESULT;
    }
}
