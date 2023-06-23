package com.wingsweaver.kuja.common.utils.support.matcher.string;

import com.wingsweaver.kuja.common.utils.support.matcher.AbstractBlackWhiteMatcher;
import com.wingsweaver.kuja.common.utils.support.matcher.StringMatcher;
import lombok.Getter;

/**
 * {@link AbstractBlackWhiteMatcher} 实现类的基类。
 *
 * @author wingsweaver
 */
public abstract class AbstractStringBlackWhiteMatcher extends AbstractBlackWhiteMatcher<String> implements StringMatcher {
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
    protected AbstractStringBlackWhiteMatcher(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
