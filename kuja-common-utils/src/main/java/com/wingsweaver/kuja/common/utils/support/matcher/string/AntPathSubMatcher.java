package com.wingsweaver.kuja.common.utils.support.matcher.string;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.matcher.BlackWhiteMatcher;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.AntPathMatcher;

import java.util.HashSet;
import java.util.Set;

/**
 * 基于 {@link AntPathMatcher} 的字符串判定工具类。
 *
 * @author wingsweaver
 */
public class AntPathSubMatcher extends AbstractStringSubMatcher {
    /**
     * 大小写敏感的 AntPathMatcher 实例。
     */
    private final AntPathMatcher matcher = new AntPathMatcher();

    /**
     * 构造函数。
     *
     * @param caseSensitive 是否区分大小写
     */
    public AntPathSubMatcher(boolean caseSensitive) {
        super(caseSensitive);
        this.matcher.setCaseSensitive(caseSensitive);
    }

    /**
     * 匹配模式。
     */
    @Getter
    @Setter
    private Set<String> patterns;

    @Override
    public BlackWhiteMatcher.MatchResult matches(String target) {
        // 检查前置条件
        if (this.patterns == null || this.patterns.isEmpty()) {
            return BlackWhiteMatcher.MatchResult.UNDETERMINED;
        }

        // 检查是否匹配
        if (this.patterns.stream().anyMatch(pattern -> this.matcher.match(pattern, target))) {
            return BlackWhiteMatcher.MatchResult.MATCHED;
        } else {
            return BlackWhiteMatcher.MatchResult.UNMATCHED;
        }
    }

    public Set<String> getPatterns(boolean createIfAbsent) {
        if (this.patterns == null && createIfAbsent) {
            this.patterns = new HashSet<>(BufferSizes.SMALL);
        }
        return this.patterns;
    }
}
