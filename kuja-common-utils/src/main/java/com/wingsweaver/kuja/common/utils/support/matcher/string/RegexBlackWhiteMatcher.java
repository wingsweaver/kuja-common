package com.wingsweaver.kuja.common.utils.support.matcher.string;

import com.wingsweaver.kuja.common.utils.support.matcher.BlackWhiteMatcher;

/**
 * 带正则表达式的字符串判定的 {@link BlackWhiteMatcher} 实现类。<br>
 *
 * @author wingsweaver
 */
public class RegexBlackWhiteMatcher extends AbstractStringBlackWhiteMatcher {
    private final RegexSubMatcher whiteMatcher;

    private final RegexSubMatcher blackMatcher;

    public RegexBlackWhiteMatcher(boolean caseSensitive) {
        super(caseSensitive);
        this.whiteMatcher = new RegexSubMatcher(caseSensitive);
        this.blackMatcher = new RegexSubMatcher(caseSensitive);
        this.setWhiteMatcher(this.whiteMatcher);
        this.setBlackMatcher(this.blackMatcher);
    }

    @Override
    public RegexSubMatcher getWhiteMatcher() {
        return whiteMatcher;
    }

    @Override
    public RegexSubMatcher getBlackMatcher() {
        return blackMatcher;
    }
}
