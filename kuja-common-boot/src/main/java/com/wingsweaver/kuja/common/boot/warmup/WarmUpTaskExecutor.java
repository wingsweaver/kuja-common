package com.wingsweaver.kuja.common.boot.warmup;

import java.util.Collection;

/**
 * 预热任务的执行器接口。
 *
 * @author wingsweaver
 */
public interface WarmUpTaskExecutor {
    /**
     * 执行预热任务。
     *
     * @param tasks    预热任务的列表
     * @param callback 回调函数
     */
    void execute(Collection<WarmUpTask> tasks, WarmUpTaskExecutionCallback callback);

    /**
     * 执行预热任务。
     *
     * @param tasks 预热任务的列表
     */
    default void execute(Collection<WarmUpTask> tasks) {
        this.execute(tasks, null);
    }
}
