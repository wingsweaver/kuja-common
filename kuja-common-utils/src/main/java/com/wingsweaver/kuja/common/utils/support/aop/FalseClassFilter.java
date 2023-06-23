package com.wingsweaver.kuja.common.utils.support.aop;

import org.springframework.aop.ClassFilter;

import java.io.Serializable;

/**
 * 固定返回 false 的 {@link ClassFilter} 实现类。
 *
 * @author wingsweaver
 */
public final class FalseClassFilter implements ClassFilter, Serializable {
    /**
     * 默认序列化版本号。
     */
    private static final long serialVersionUID = 1L;

    /**
     * 单例实例。
     */
    public static final FalseClassFilter INSTANCE = new FalseClassFilter();

    /**
     * 构造函数。
     */
    private FalseClassFilter() {
        // 禁止外部生成实例。
    }

    /**
     * 匹配指定的类型。
     *
     * @param clazz 类型
     * @return 固定返回 false
     */
    @Override
    public boolean matches(Class<?> clazz) {
        return false;
    }

    /**
     * 用于处理 {@link Serializable} 的回调函数。
     *
     * @return 回调函数
     */
    @SuppressWarnings("SameReturnValue")
    Object readResolve() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "FalseClassFilter";
    }
}
