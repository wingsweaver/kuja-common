package com.wingsweaver.kuja.common.utils.support.matcher;

/**
 * 通用的匹配处理接口。
 *
 * @param <T> 可以处理的数据类型
 * @author wingsweaver
 */
public interface Matcher<T> {
    /**
     * 检查指定数据是否符合要求。
     *
     * @param target 目标数据
     * @return 是否符合要求
     */
    boolean matches(T target);
}
