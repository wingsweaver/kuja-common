package com.wingsweaver.kuja.common.utils.support.matcher;

/**
 * {@link BlackWhiteMatcher} 实现类的基类。
 *
 * @param <T> 可以处理的数据类型
 * @author wingsweaver
 */
public abstract class AbstractBlackWhiteMatcher<T> implements BlackWhiteMatcher<T> {
    /**
     * 默认值。
     */
    private boolean defaultResult;

    /**
     * 黑名单判定接口。
     */
    private SubMatcher<T> blackMatcher;

    /**
     * 白名单判定接口。
     */
    private SubMatcher<T> whiteMatcher;

    @Override
    public MatchResult matchesBlackWhite(T target) {
        // 先基于黑名单进行判断
        MatchResult blackMatchResult = this.matchesBlack(target);
        if (blackMatchResult == MatchResult.MATCHED) {
            return MatchResult.UNMATCHED;
        }

        // 再基于白名单进行判断
        MatchResult matchResult = null;
        MatchResult whiteMatchResult = this.matchesWhite(target);
        if (whiteMatchResult == null || whiteMatchResult == MatchResult.UNDETERMINED) {
            matchResult = (blackMatchResult == MatchResult.UNMATCHED) ? MatchResult.MATCHED : MatchResult.UNDETERMINED;
        } else {
            matchResult = whiteMatchResult;
        }

        // 返回
        return matchResult;
    }

    protected MatchResult matchesBlack(T target) {
        if (this.getBlackMatcher() != null) {
            return this.getBlackMatcher().matches(target);
        } else {
            return MatchResult.UNDETERMINED;
        }
    }

    protected MatchResult matchesWhite(T target) {
        if (this.getWhiteMatcher() != null) {
            return this.getWhiteMatcher().matches(target);
        } else {
            return MatchResult.UNDETERMINED;
        }
    }

    @Override
    public boolean getDefaultResult() {
        return this.defaultResult;
    }

    public void setDefaultResult(boolean defaultResult) {
        this.defaultResult = defaultResult;
    }

    /**
     * 获取黑名单判定接口。
     *
     * @return 黑名单判定接口
     */
    public SubMatcher<T> getBlackMatcher() {
        return blackMatcher;
    }

    /**
     * 设置黑名单判定接口。
     *
     * @param blackMatcher 黑名单判定接口
     */
    protected void setBlackMatcher(SubMatcher<T> blackMatcher) {
        this.blackMatcher = blackMatcher;
    }

    /**
     * 获取白名单判定接口。
     *
     * @return 白名单判定接口
     */
    public SubMatcher<T> getWhiteMatcher() {
        return whiteMatcher;
    }

    /**
     * 设置白名单判定接口。
     *
     * @param whiteMatcher 白名单判定接口
     */
    protected void setWhiteMatcher(SubMatcher<T> whiteMatcher) {
        this.whiteMatcher = whiteMatcher;
    }
}
