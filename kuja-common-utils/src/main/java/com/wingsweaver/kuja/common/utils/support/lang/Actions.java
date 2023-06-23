package com.wingsweaver.kuja.common.utils.support.lang;

/**
 * 没有返回值的函数接口。
 *
 * @author wingsweaver
 */
public interface Actions {
    /**
     * 不带任何参数的函数接口。
     */
    interface Action0 {
        /**
         * 具体的处理。
         */
        void invoke();
    }

    /**
     * 带 1 个参数的函数接口。
     *
     * @param <T1> 第 1 个参数的类型
     */
    interface Action1<T1> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         */
        void invoke(T1 arg1);
    }

    /**
     * 带 2 个参数的函数接口。
     *
     * @param <T1> 第 1 个参数的类型
     * @param <T2> 第 2 个参数的类型
     */
    interface Action2<T1, T2> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         * @param arg2 第 2 个参数
         */
        void invoke(T1 arg1, T2 arg2);
    }

    /**
     * 带 3 个参数的函数接口。
     *
     * @param <T1> 第 1 个参数的类型
     * @param <T2> 第 2 个参数的类型
     * @param <T3> 第 3 个参数的类型
     */
    interface Action3<T1, T2, T3> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         * @param arg2 第 2 个参数
         * @param arg3 第 3 个参数
         */
        void invoke(T1 arg1, T2 arg2, T3 arg3);
    }

    /**
     * 带 4 个参数的函数接口。
     *
     * @param <T1> 第 1 个参数的类型
     * @param <T2> 第 2 个参数的类型
     * @param <T3> 第 3 个参数的类型
     * @param <T4> 第 4 个参数的类型
     */
    interface Action4<T1, T2, T3, T4> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         * @param arg2 第 2 个参数
         * @param arg3 第 3 个参数
         * @param arg4 第 4 个参数
         */
        void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
    }

    /**
     * 带 5 个参数的函数接口。
     *
     * @param <T1> 第 1 个参数的类型
     * @param <T2> 第 2 个参数的类型
     * @param <T3> 第 3 个参数的类型
     * @param <T4> 第 4 个参数的类型
     * @param <T5> 第 5 个参数的类型
     */
    interface Action5<T1, T2, T3, T4, T5> {
        /**
         * 具体的处理。
         *
         * @param arg1 第 1 个参数
         * @param arg2 第 2 个参数
         * @param arg3 第 3 个参数
         * @param arg4 第 4 个参数
         * @param arg5 第 5 个参数
         */
        void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5);
    }
}
