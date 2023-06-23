package com.wingsweaver.kuja.common.utils.support.tostring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@link MaskedConverter} 的设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MaskedConfig {
    public static final char DEFAULT_MASK_CHAR = '*';

    /**
     * 屏蔽用字符。
     */
    private char maskChar = DEFAULT_MASK_CHAR;

    /**
     * 屏蔽后的最大长度。
     */
    private int maxLength = 0;

    /**
     * 屏蔽范围。<br>
     * 默认屏蔽所有字符串。
     */
    List<Range> ranges;

    /**
     * 设置屏蔽范围的注解。<br>
     * 范围是一个左闭右开的区间，即 [{@code start}, {@code end})。<br>
     * start = 0, end = 0 表示屏蔽整个字符串。
     */
    @Getter
    @AllArgsConstructor
    static class Range {
        /**
         * 开始坐标。<br>
         * 小于 0 的话，从字符串末尾开始计数。<br>
         * 如: -1 表示 length - 1 的位置。
         */
        private final int start;

        /**
         * 结束坐标。<br>
         * 小于等于 0 的话，从字符串末尾开始计数。<br>
         * 如: -1 表示 length - 1 的位置。
         */
        private final int end;
    }
}
