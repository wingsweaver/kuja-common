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
        ToStringConfig subConfig = subConfig(config);

        if (ToStringTypeUtil.isSimpleType(clazz)) {
            builder.append(object);
        } else if (clazz.isArray()) {
            this.array2String(object, builder, config, subConfig);
        } else if (object instanceof Collection) {
            this.collection2String((Collection<?>) object, builder, config, subConfig);
        } else if (object instanceof Map) {
            this.map2String((Map<?, ?>) object, builder, config, subConfig);
        } else {
            handled = false;
        }

        // 返回
        return handled;
    }

    private void appendOmit(boolean shouldOmit, StringBuilder builder, int size) {
        if (shouldOmit) {
            builder.append(TOKEN_OMIT);
            builder.append(" (").append(size).append(" items)");
        }
    }

    private void array2String(Object array, StringBuilder builder, ToStringConfig config, ToStringConfig subConfig) {
        builder.append(TOKEN_ARRAY_START);
        int maxItemCount = config.getMaxItemCount();
        int length = Array.getLength(array);
        int index = 0;

        // 最多输出 maxItemCount 个元素
        for (; index < Math.min(maxItemCount, length); index++) {
            if (index > 0) {
                builder.append(TOKEN_DELIMITER);
            }

            // 处理本元素
            Object item = Array.get(array, index);
            appendObject(builder, item, config, subConfig);
        }

        // 如果还有元素没有输出的话，那么补充省略号
        this.appendOmit(index < length, builder, length);

        // 输出结尾符号
        builder.append(TOKEN_ARRAY_END);
    }

    private void collection2String(Collection<?> collection, StringBuilder builder, ToStringConfig config, ToStringConfig subConfig) {
        builder.append(TOKEN_ARRAY_START);
        int maxItemCount = config.getMaxItemCount();
        int length = collection.size();
        int index = 0;

        // 最多输出 maxItemCount 个元素
        for (Object item : collection) {
            if (index > 0) {
                builder.append(TOKEN_DELIMITER);
            }

            // 处理本元素
            appendObject(builder, item, config, subConfig);

            // 处理下一个元素
            index++;

            // 检查是否超过数量
            if (index >= maxItemCount) {
                break;
            }
        }

        // 如果还有元素没有输出的话，那么补充省略号
        this.appendOmit(index < length, builder, length);

        // 输出结尾符号
        builder.append(TOKEN_ARRAY_END);
    }

    private void map2String(Map<?, ?> map, StringBuilder builder, ToStringConfig config, ToStringConfig subConfig) {
        builder.append(TOKEN_OBJECT_START);

        int maxItemCount = config.getMaxItemCount();
        int size = map.size();
        int index = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (index > 0) {
                builder.append(TOKEN_DELIMITER);
            }

            // 处理本元素
            // Key: 不做反射处理
            // Value: 如果还可以继续反射的话，那么继续反射
            builder.append(entry.getKey());
            builder.append(TOKEN_VALUE);
            appendObject(builder, entry.getValue(), config, subConfig);

            // 处理下一个元素
            index++;

            // 检查是否超过数量
            if (index >= maxItemCount) {
                break;
            }
        }

        // 如果还有元素没有输出的话，那么补充省略号
        this.appendOmit(index < size, builder, size);

        // 输出结尾符号
        builder.append(TOKEN_OBJECT_END);
    }
}
