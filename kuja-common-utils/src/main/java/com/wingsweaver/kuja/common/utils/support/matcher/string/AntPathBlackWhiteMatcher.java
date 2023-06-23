package com.wingsweaver.kuja.common.utils.support.matcher.string;

import com.wingsweaver.kuja.common.utils.support.matcher.AbstractBlackWhiteMatcher;

/**
 * 基于 {@link AntPathSubMatcher} 的 {@link AbstractBlackWhiteMatcher} 实现类。
 *
 * @author wingsweaver
 */
public class AntPathBlackWhiteMatcher extends AbstractStringBlackWhiteMatcher {
    private final AntPathSubMatcher whiteMatcher;

    private final AntPathSubMatcher blackMatcher;

    /**
     * 构造函数。
     *
     * @param caseSensitive 是否区分大小写
     */
    public AntPathBlackWhiteMatcher(boolean caseSensitive) {
        super(caseSensitive);
        this.whiteMatcher = new AntPathSubMatcher(caseSensitive);
        this.blackMatcher = new AntPathSubMatcher(caseSensitive);
        this.setWhiteMatcher(this.whiteMatcher);
        this.setBlackMatcher(this.blackMatcher);
    }

    @Override
    public AntPathSubMatcher getWhiteMatcher() {
        return whiteMatcher;
    }

    @Override
    public AntPathSubMatcher getBlackMatcher() {
        return blackMatcher;
    }
}
