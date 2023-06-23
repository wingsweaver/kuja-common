package com.wingsweaver.kuja.common.utils.support.aop;

import org.springframework.aop.MethodMatcher;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 固定返回 false 的 {@link MethodMatcher} 的实现。
 *
 * @author wingsweaver
 */
public final class FalseMethodMatcher implements MethodMatcher, Serializable {
    /**
     * 默认序列化版本号。
     */
    private static final long serialVersionUID = 1L;

    /**
     * 单例实例。
     */
    public static final FalseMethodMatcher INSTANCE = new FalseMethodMatcher();

    /**
     * 构造函数。
     */
    private FalseMethodMatcher() {
        // 禁止初始化
    }

    /**
     * 匹配指定类型的指定函数。
     *
     * @param method      目标函数
     * @param targetClass 目标类型
     * @return 固定返回 false
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return false;
    }

    /**
     * 匹配指定类型的指定函数和指定参数。
     *
     * @param method      目标函数
     * @param targetClass 目标类型
     * @param args        目标参数
     * @return 固定返回 false
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        return false;
    }

    /**
     * 检查是否是动态处理。
     *
     * @return 固定返回 false
     */
    @Override
    public boolean isRuntime() {
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
        return "FalseMethodMatcher";
    }
}
