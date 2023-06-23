package com.wingsweaver.kuja.common.utils.support;

import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;

/**
 * 正则表达式相关工具类。
 *
 * @author wingsweaver
 */
public final class RegexUtil {
    private RegexUtil() {
        // 禁止实例化
    }

    /**
     * 检查指定字符串是否是正则表达式。
     *
     * @param text 指定字符串
     * @return 是否是正则表达式
     */
    public static boolean isRegexPattern(String text) {
        // 检查参数
        if (StringUtil.isEmpty(text)) {
            return false;
        }

        boolean hasLeftParentheses = false;
        boolean hasLeftBracket = false;
        char[] chars = text.toCharArray();
        for (char ch : chars) {
            switch (ch) {
                case '(':
                    hasLeftParentheses = true;
                    break;
                case ')':
                    if (hasLeftParentheses) {
                        return true;
                    }
                    break;
                case '[':
                    hasLeftBracket = true;
                    break;
                case ']':
                    if (hasLeftBracket) {
                        return true;
                    }
                    break;
                case '*':
                    return true;
                default:
                    break;
            }
        }
        return false;
    }
}
