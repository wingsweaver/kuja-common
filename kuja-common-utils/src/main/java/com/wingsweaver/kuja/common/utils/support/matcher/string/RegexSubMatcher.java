package com.wingsweaver.kuja.common.utils.support.matcher.string;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.RegexUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ArrayUtil;
import com.wingsweaver.kuja.common.utils.support.matcher.BlackWhiteMatcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 带正则表达式判定的字符串判定工具类。
 *
 * @author wingsweaver
 */
public class RegexSubMatcher extends AbstractStringSubMatcher {
    private final Set<String> simpleStrings = new HashSet<>(BufferSizes.SMALL);

    private final List<Pattern> patterns = new ArrayList<>(BufferSizes.SMALL);

    private final int patternFlag;

    public RegexSubMatcher(boolean caseSensitive) {
        super(caseSensitive);
        this.patternFlag = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
    }

    @Override
    public BlackWhiteMatcher.MatchResult matches(String target) {
        // 检查前置条件
        if (this.simpleStrings.isEmpty() && this.patterns.isEmpty()) {
            return BlackWhiteMatcher.MatchResult.UNDETERMINED;
        }

        // 检查是否匹配
        boolean matched = this.matchesSimpleString(target) || this.matchesPattern(target);

        // 返回
        return matched ? BlackWhiteMatcher.MatchResult.MATCHED : BlackWhiteMatcher.MatchResult.UNMATCHED;
    }

    private boolean matchesPattern(String target) {
        if (this.patterns.isEmpty() || target == null) {
            return false;
        }

        return this.patterns.stream().anyMatch(pattern -> pattern.matcher(target).matches());
    }

    private boolean matchesSimpleString(String target) {
        if (this.simpleStrings.isEmpty()) {
            return false;
        }

        if (this.isCaseSensitive()) {
            return this.simpleStrings.stream().anyMatch(text -> text.equals(target));
        } else {
            return this.simpleStrings.stream().anyMatch(text -> text.equalsIgnoreCase(target));
        }
    }

    /**
     * 添加一组字符串或者正则表达式。
     *
     * @param stringOrPatterns 字符串或者正则表达式
     */
    public void addStrings(String... stringOrPatterns) {
        if (ArrayUtil.isNotEmpty(stringOrPatterns)) {
            for (String stringOrPattern : stringOrPatterns) {
                this.addString(stringOrPattern);
            }
        }
    }

    /**
     * 添加一组字符串或者正则表达式。
     *
     * @param stringOrPatterns 字符串或者正则表达式
     */
    public void addStrings(Iterable<String> stringOrPatterns) {
        if (stringOrPatterns != null) {
            for (String stringOrPattern : stringOrPatterns) {
                this.addString(stringOrPattern);
            }
        }
    }

    /**
     * 添加一个字符串或者正则表达式。
     *
     * @param stringOrPattern 字符串或者正则表达式
     */
    protected void addString(String stringOrPattern) {
        if (RegexUtil.isRegexPattern(stringOrPattern)) {
            this.patterns.add(Pattern.compile(stringOrPattern, this.patternFlag));
        } else {
            this.simpleStrings.add(stringOrPattern);
        }
    }

    /**
     * 删除一组字符串或者正则表达式。
     *
     * @param stringOrPatterns 字符串或者正则表达式
     */
    public void removeStrings(String... stringOrPatterns) {
        if (ArrayUtil.isNotEmpty(stringOrPatterns)) {
            for (String stringOrPattern : stringOrPatterns) {
                this.removeString(stringOrPattern);
            }
        }
    }

    /**
     * 删除一组字符串或者正则表达式。
     *
     * @param stringOrPatterns 字符串或者正则表达式
     */
    public void removeStrings(Iterable<String> stringOrPatterns) {
        if (stringOrPatterns != null) {
            for (String stringOrPattern : stringOrPatterns) {
                this.removeString(stringOrPattern);
            }
        }
    }

    /**
     * 删除一个字符串或者正则表达式。
     *
     * @param stringOrPattern 字符串或者正则表达式
     */
    protected void removeString(String stringOrPattern) {
        this.simpleStrings.remove(stringOrPattern);
        this.patterns.removeIf(pattern -> pattern.pattern().equals(stringOrPattern));
    }
}
