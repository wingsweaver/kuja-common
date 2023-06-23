package com.wingsweaver.kuja.common.boot.warmup;

/**
 * 单次预热执行的记录。
 *
 * @author wingsweaver
 */
public interface WarmUpExecution {
    /**
     * 获取执行记录的 ID。
     *
     * @return 执行记录的 ID
     */
    Object getExecutionId();

    /**
     * 获取本次执行的预热任务。
     *
     * @return 本次执行的预热任务的数组
     */
    WarmUpTask[] getTasks();
}
