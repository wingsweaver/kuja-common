package com.wingsweaver.kuja.common.utils.support.tostring;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 处理简单数据类型的 {@link ToStringConverter} 实现类。
 *
 * @author wingsweaver
 */
final class SimpleToStringConverter extends AbstractToStringConverter {
    static final SimpleToStringConverter INSTANCE = new SimpleToStringConverter();

    private SimpleToStringConverter() {
        // 禁止外部生成实例
    }

    @Override
    public boolean toString(Object object, StringBuilder builder, ToStringConfig config) {
        // 检查参数
        if (object == null) {
            return false;
        }

        boolean handled = true;
        Class<?> clazz = object.getClass();

        if (ToStringTypeUtil.isSimpleType(clazz)) {
            builder.append(object);
        } else if (clazz.isArray()) {
            this.array2String(object, builder, config);
        } else if (object instanceof Collection) {
            this.collection2String((Collection<?>) object, builder, config);
        } else if (object instanceof Map) {
            this.map2String((Map<?, ?>) object, builder, config);
        } else {
            handled = false;
        }

        // 返回
        return handled;
    }

    private void array2String(Object array, StringBuilder builder, ToStringConfig config) {
        builder.append(TOKEN_ARRAY_START);
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                builder.append(TOKEN_DELIMITER);
            }
            Object item = Array.get(array, i);
            builder.append(ToStringBuilder.toString(item, config));
        }
        builder.append(TOKEN_ARRAY_END);
    }

    private void collection2String(Collection<?> collection, StringBuilder builder, ToStringConfig config) {
        builder.append(TOKEN_ARRAY_START);
        int index = 0;
        for (Object item : collection) {
            if (index > 0) {
                builder.append(TOKEN_DELIMITER);
            }
            builder.append(ToStringBuilder.toString(item, config));
            index++;
        }
        builder.append(TOKEN_ARRAY_END);
    }

    private void map2String(Map<?, ?> map, StringBuilder builder, ToStringConfig config) {
        builder.append(TOKEN_OBJECT_START);
        int index = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (index > 0) {
                builder.append(TOKEN_DELIMITER);
            }
            builder.append(ToStringBuilder.toString(entry.getKey(), config));
            builder.append(TOKEN_VALUE);
            builder.append(ToStringBuilder.toString(entry.getValue(), config));
            index++;
        }
        builder.append(TOKEN_OBJECT_END);
    }
}
