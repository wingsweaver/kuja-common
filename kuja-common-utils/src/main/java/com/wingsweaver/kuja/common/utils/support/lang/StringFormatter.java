package com.wingsweaver.kuja.common.utils.support.lang;

import com.wingsweaver.kuja.common.utils.thirdparty.org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;

/**
 * 字符串格式化工具类。
 *
 * @author wingsweaver
 */
public final class StringFormatter {
    private StringFormatter() {
        // 禁止实例化
    }

    /**
     * 格式化字符串。
     *
     * @param pattern 格式化模式
     * @param args    格式化参数
     * @return 格式化后的字符串
     */
    public static String format(String pattern, Object... args) {
        // 检查参数
        if (StringUtil.isEmpty(pattern) || ArrayUtil.isEmpty(args)) {
            return pattern;
        }

        // 检查格式化模式的类型
        String result;
        PatternMode patternMode = deducePatternMode(pattern);
        switch (patternMode) {
            case STRING:
                result = String.format(pattern, args);
                break;
            case MESSAGE:
                result = MessageFormat.format(pattern, args);
                break;
            case EXTENDED:
                result = MessageFormatter.arrayFormat(pattern, args).getMessage();
                break;
            case NONE:
            default:
                result = pattern;
                break;
        }

        // 返回结果
        return result;
    }

    /**
     * 推断格式化模式。
     *
     * @param pattern 格式化模式
     * @return 格式化模式
     */
    static PatternMode deducePatternMode(String pattern) {
        int posLeftOpenBrace = -1;
        int posRightOpenBrace;

        boolean hasString = false;
        boolean hasMessage = false;
        boolean hasExtended = false;

        int[] codePoints = pattern.codePoints().toArray();
        for (int i = 0; i < codePoints.length; i++) {
            int codePoint = codePoints[i];
            if (codePoint == '%') {
                // 含有 % 的字符串，一定是 String 模式
                hasString = true;
            } else if (codePoint == '{') {
                posLeftOpenBrace = i;
            } else if (codePoint == '}') {
                posRightOpenBrace = i;
                if (posLeftOpenBrace < 0 || posLeftOpenBrace > posRightOpenBrace) {
                    posLeftOpenBrace = codePoints.length;
                    continue;
                }

                if (posRightOpenBrace == posLeftOpenBrace + 1) {
                    // 含有 {} 的字符串，一定是 Extended 模式
                    hasExtended = true;
                } else {
                    hasMessage = checkHasMessage(codePoints, posLeftOpenBrace + 1, posRightOpenBrace - 1);
                }

                // 重置 { 的位置
                posLeftOpenBrace = codePoints.length;
            }
        }

        // 返回结果
        if (hasMessage) {
            return PatternMode.MESSAGE;
        } else if (hasExtended) {
            return PatternMode.EXTENDED;
        } else if (hasString) {
            return PatternMode.STRING;
        } else {
            return PatternMode.NONE;
        }
    }

    /**
     * 检查指定范围内的字符串是否是 Message 模式。
     *
     * @param codePoints 字符串的码点数组
     * @param startPos   起始位置（inclusive）
     * @param endPos     结束位置（inclusive）
     * @return 是否是 Message 模式
     */
    private static boolean checkHasMessage(int[] codePoints, int startPos, int endPos) {
        boolean startWithNumbers = false;
        boolean hasNonNumbers = false;

        for (int i = startPos; i <= endPos; i++) {
            int codePoint = codePoints[i];
            if (codePoint >= '0' && codePoint <= '9') {
                if (!hasNonNumbers) {
                    startWithNumbers = true;
                }
            } else {
                switch (codePoint) {
                    case ' ':
                    case '\t':
                        break;
                    case ',':
                        hasNonNumbers = true;
                        break;
                    default:
                        return false;
                }
            }
        }
        return startWithNumbers;
    }

    /**
     * 格式化模式。
     */
    enum PatternMode {
        /**
         * 无格式化模式。
         */
        NONE,

        /**
         * String 模式。<br>
         * {@link String#format(String, Object...)}。
         */
        STRING,

        /**
         * Message 模式。<br>
         * {@link MessageFormat#format(String, Object...)}。
         */
        MESSAGE,

        /**
         * Extended 模式。<br>
         * {@link MessageFormatter#arrayFormat(String, Object[])} 格式。
         */
        EXTENDED,
    }
}
