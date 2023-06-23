package com.wingsweaver.kuja.common.utils.support.lang;

/**
 * 带返回值的函数接口。
 *
 * @author wingsweaver
 */
public interface Functions {
    /**
     * 不带任何参数的函数接口。
     *
     * @param <R> 返回值的类型
     */
    interface Func0<R> {
        /**
         * 具体的处理。
         *
         * @return 处理结果
         */
        R invoke();
    }

    /**
     * 带 1 个参数的函数接口。
     *
     * @param <R>  返回值的类型
     * @param <T1> 第 1 个参数的类型
     */
    interface Func1<R, T1> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         * @return 处理结果
         */
        R invoke(T1 arg1);
    }

    /**
     * 带 2 个参数的函数接口。
     *
     * @param <R>  返回值的类型
     * @param <T1> 第 1 个参数的类型
     * @param <T2> 第 2 个参数的类型
     */
    interface Func2<R, T1, T2> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         * @param arg2 第 2 个参数
         * @return 处理结果
         */
        R invoke(T1 arg1, T2 arg2);
    }

    /**
     * 带 3 个参数的函数接口。
     *
     * @param <R>  返回值的类型
     * @param <T1> 第 1 个参数的类型
     * @param <T2> 第 2 个参数的类型
     * @param <T3> 第 3 个参数的类型
     */
    interface Func3<R, T1, T2, T3> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         * @param arg2 第 2 个参数
         * @param arg3 第 3 个参数
         * @return 处理结果
         */
        R invoke(T1 arg1, T2 arg2, T3 arg3);
    }

    /**
     * 带 4 个参数的函数接口。
     *
     * @param <R>  返回值的类型
     * @param <T1> 第 1 个参数的类型
     * @param <T2> 第 2 个参数的类型
     * @param <T3> 第 3 个参数的类型
     * @param <T4> 第 4 个参数的类型
     */
    interface Func4<R, T1, T2, T3, T4> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         * @param arg2 第 2 个参数
         * @param arg3 第 3 个参数
         * @param arg4 第 4 个参数
         * @return 处理结果
         */
        R invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
    }

    /**
     * 带 5 个参数的函数接口。
     *
     * @param <R>  返回值的类型
     * @param <T1> 第 1 个参数的类型
     * @param <T2> 第 2 个参数的类型
     * @param <T3> 第 3 个参数的类型
     * @param <T4> 第 4 个参数的类型
     * @param <T5> 第 5 个参数的类型
     */
    interface Func5<R, T1, T2, T3, T4, T5> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         * @param arg2 第 2 个参数
         * @param arg3 第 3 个参数
         * @param arg4 第 4 个参数
         * @param arg5 第 5 个参数
         * @return 处理结果
         */
        R invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5);
    }
}
