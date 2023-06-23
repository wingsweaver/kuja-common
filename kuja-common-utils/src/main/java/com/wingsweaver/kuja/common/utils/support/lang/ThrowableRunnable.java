package com.wingsweaver.kuja.common.utils.support.lang;

/**
 * 可抛出异常的 {@link Runnable}。
 *
 * @param <E> 异常类型
 * @author wingsweaver
 */
public interface ThrowableRunnable<E extends Throwable> {
    /**
     * 执行处理。
     *
     * @throws E 发生异常
     */
    void run() throws E;
}
