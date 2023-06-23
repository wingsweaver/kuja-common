package com.wingsweaver.kuja.common.utils.support.matcher;

/**
 * 基于黑白名单的 {@link Matcher} 实现类。
 *
 * @param <T> 可以处理的数据类型
 * @author wingsweaver
 */
public interface BlackWhiteMatcher<T> extends Matcher<T> {
    /**
     * 按照黑白名单进行校验。
     *
     * @param target 要校验的数据
     * @return 校验结果
     */
    MatchResult matchesBlackWhite(T target);

    /**
     * 获取默认的判定结果。
     *
     * @return 默认的判定结果
     */
    boolean getDefaultResult();

    /**
     * 检查指定数据是否符合要求。
     *
     * @param target 目标数据
     * @return 是否符合要求
     */
    @Override
    default boolean matches(T target) {
        boolean result;

        // 先按照黑白名单进行校验，再分析校验结果
        switch (this.matchesBlackWhite(target)) {
            case MATCHED:
                result = true;
                break;
            case UNMATCHED:
                result = false;
                break;
            case UNDETERMINED:
            default:
                result = this.getDefaultResult();
                break;
        }

        // 返回
        return result;
    }

    /**
     * 判定结果的定义。
     */
    enum MatchResult {
        /**
         * 确定匹配。
         */
        MATCHED,

        /**
         * 确定不匹配。
         */
        UNMATCHED,

        /**
         * 没有明确的结果（使用默认值）。
         */
        UNDETERMINED
    }

    /**
     * 实际使用的黑名单或者白名单的判定接口。
     *
     * @param <V> 可以处理的数据类型
     */
    interface SubMatcher<V> {
        /**
         * 判定指定数据是否符合要求。
         *
         * @param target 目标数据
         * @return 是否符合要求
         */
        MatchResult matches(V target);
    }
}
