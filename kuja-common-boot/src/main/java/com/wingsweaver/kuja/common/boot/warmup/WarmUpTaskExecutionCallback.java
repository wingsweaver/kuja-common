package com.wingsweaver.kuja.common.boot.warmup;

/**
 * {@link WarmUpTaskExecutor} 执行预热任务时的回调接口。
 *
 * @author wingsweaver
 */
public interface WarmUpTaskExecutionCallback {
    /**
     * 预热任务开始执行时的回调方法。
     */
    void onStart();

    /**
     * 所有预热任务执行完成时的回调方法。
     */
    void onComplete();

    /**
     * 单个预热任务执行完成后的回调方法。
     *
     * @param warmUpTask 预热任务
     * @param error      预热任务执行过程中发生的异常（没有异常的话，值为 null）
     */
    void onProgress(WarmUpTask warmUpTask, Throwable error);
}
