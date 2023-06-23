package com.wingsweaver.kuja.common.utils.support.tostring;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Masked} 注解相应的 {@link ToStringWithConverter} 实现类。
 *
 * @author wingsweaver
 */
public class MaskedConverter extends AbstractToStringWithConverter<Masked>
        implements ConfigurableToStringConverter<MaskedConfig> {
    public MaskedConverter() {
        super(Masked.class);
    }

    @Override
    protected void toStringInternal(Object value, AnnotatedElement annotatedElement, Masked annotation,
                                    StringBuilder builder, ToStringConfig config) {
        // 计算转换设置
        MaskedConfig customConfig = new MaskedConfig();
        customConfig.setMaskChar(annotation.value());
        customConfig.setMaxLength(annotation.maxLength());
        customConfig.setRanges(Arrays.stream(annotation.ranges())
                .map(range -> new MaskedConfig.Range(range.start(), range.end()))
                .collect(Collectors.toList()));

        // 进行转换
        this.toString(value, builder, config, customConfig);
    }

    @Override
    public boolean toString(Object object, StringBuilder builder, ToStringConfig config, MaskedConfig customConfig) {
        // 预处理，得到字符串
        String text = String.valueOf(object);
        if (object == null) {
            builder.append(text);
            return true;
        }
        if (text.isEmpty()) {
            return true;
        }

        // 拆解为字符数组，按照 Mask.Range 设置进行屏蔽处理
        int length = text.length();
        int maxLength = customConfig.getMaxLength();
        if (maxLength > 0) {
            length = Math.min(length, maxLength);
        } else if (maxLength < 0) {
            length = length + maxLength;
        }
        char[] chars = text.substring(0, length).toCharArray();
        char maskChar = customConfig.getMaskChar();
        List<MaskedConfig.Range> ranges = customConfig.getRanges();
        if (CollectionUtils.isEmpty(ranges)) {
            Arrays.fill(chars, maskChar);
        } else {
            for (MaskedConfig.Range range : ranges) {
                int start = this.normalizeStartIndex(range.getStart(), length);
                int end = this.normalizeEndIndex(range.getEnd(), length);
                Arrays.fill(chars, start, end, maskChar);
            }
        }

        // 将屏蔽后的字符数组，写入 builder
        builder.append(chars);

        // 返回
        return true;
    }

    private int normalizeEndIndex(int end, int length) {
        if (end > 0) {
            return Math.min(end, length);
        } else {
            return Math.max(length + end, 0);
        }
    }

    private int normalizeStartIndex(int start, int length) {
        if (start >= 0) {
            return Math.min(start, length);
        } else {
            return Math.max(length + start, 0);
        }
    }
}
