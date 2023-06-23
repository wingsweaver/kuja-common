package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.constants.Orders;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Tags 相关的转换工具类。
 *
 * @author wingsweaver
 */
public final class TagConversionService {
    private TagConversionService() {
        // 禁止实例化
    }

    private static final TagValueConverter[] CONVERTERS;

    private static final TagValueWriter[] WRITERS;

    static {
        List<TagValueConverter> converters = new LinkedList<>();
        List<TagValueWriter> writers = new LinkedList<>();

        // 加载所有的 TagConversionServiceConfigurer 实现
        SpringFactoriesLoader.loadFactories(TagConversionServiceConfigurer.class, null)
                .forEach(register -> {
                    register.registerConverters(converters);
                    register.registerWriters(writers);
                });

        // 构造 CONVERTERS
        converters.sort(Orders::compare);
        converters.add(new DefaultTagValueListConverter());
        converters.add(new DefaultTagValueConverter());
        CONVERTERS = converters.toArray(new TagValueConverter[0]);

        // 构造 WRITERS
        writers.sort(Orders::compare);
        writers.add(new DefaultTagValueWriter());
        WRITERS = writers.toArray(new TagValueWriter[0]);
    }

    /**
     * 将指定数据转换成可支持的类型。
     *
     * @param source 数据
     * @param type   类型
     * @param <T>    转换后的类型
     * @return 转换后的数据
     * @throws TagConversionException 转换失败时抛出
     */
    @SuppressWarnings("unchecked")
    public static <T> T toValue(Object source, Type type) throws TagConversionException {
        AssertArgs.Named.notNull("type", type);

        for (TagValueConverter converter : CONVERTERS) {
            try {
                Tuple2<Boolean, Object> result = converter.convertTo(source, type);
                if (result.getT1()) {
                    return (T) result.getT2();
                }
            } catch (Exception ex) {
                String message = MessageFormat.format("Failed to convert source [{0}] to type [{1}] with converter [{2}].",
                        source, type, converter);
                throw new TagConversionException(message, ex)
                        .withExtendedAttribute("source", source)
                        .withExtendedAttribute("type", type)
                        .withExtendedAttribute("converter", converter);
            }
        }

        String message = MessageFormat.format("Failed to convert source [{0}] to type [{1}], no compatible converter found.",
                source, type);
        throw new TagConversionException(message)
                .withExtendedAttribute("source", source)
                .withExtendedAttribute("type", type);
    }

    /**
     * 将指定的对象转换为指定类型的数组。
     *
     * @param value 源对象
     * @return 转换后的数组
     * @throws TagConversionException 转换失败时抛出
     */
    public static String saveValueAsText(Object value) throws TagConversionException {
        if (value == null) {
            return null;
        }

        for (TagValueWriter writer : WRITERS) {
            try {
                Tuple2<Boolean, String> result = writer.saveAsText(value);
                if (result.getT1()) {
                    return result.getT2();
                }
            } catch (Exception ex) {
                String message = MessageFormat.format("Failed to convert value [{0}] to text with writer [{1}].",
                        value, writer);
                throw new TagConversionException(message, ex)
                        .withExtendedAttribute("value", value)
                        .withExtendedAttribute("converter", writer);
            }
        }

        String message = MessageFormat.format("Failed to convert value [{0}] to text, no compatible writer found.",
                value);
        throw new TagConversionException(message)
                .withExtendedAttribute("value", value);
    }
}
