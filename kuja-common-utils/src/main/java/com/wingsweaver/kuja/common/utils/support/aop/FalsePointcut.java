package com.wingsweaver.kuja.common.utils.support.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.io.Serializable;

/**
 * 恒定返回 false 的切入点。
 *
 * @author wingsweaver
 */
public final class FalsePointcut implements Pointcut, Serializable {
    /**
     * 共通实例。
     */
    public static final FalsePointcut INSTANCE = new FalsePointcut();

    /**
     * 构造函数。
     */
    private FalsePointcut() {
    }

    @Override
    public ClassFilter getClassFilter() {
        return FalseClassFilter.INSTANCE;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return FalseMethodMatcher.INSTANCE;
    }

    @Override
    public String toString() {
        return "Pointcut.FALSE";
    }
}

