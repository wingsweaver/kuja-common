package com.wingsweaver.kuja.common.utils.support.matcher.string;

import com.wingsweaver.kuja.common.utils.support.matcher.AbstractBlackWhiteMatcher;
import lombok.Getter;

/**
 * 字符串类型的 {@link AbstractBlackWhiteMatcher.SubMatcher} 实现类的基类。
 *
 * @author wingsweaver
 */
public abstract class AbstractStringSubMatcher implements AbstractBlackWhiteMatcher.SubMatcher<String> {
    /**
     * 是否大小写敏感。
     */
    @Getter
    private final boolean caseSensitive;

    /**
     * 构造函数。
     *
     * @param caseSensitive 是否大小写敏感
     */
    protected AbstractStringSubMatcher(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
