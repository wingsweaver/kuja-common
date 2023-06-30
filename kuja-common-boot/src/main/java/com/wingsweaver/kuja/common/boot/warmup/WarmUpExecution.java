package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.utils.model.id.IdGetter;

/**
 * 单次预热执行的记录。
 *
 * @author wingsweaver
 */
public interface WarmUpExecution extends IdGetter<String> {
    /**
     * 获取创建时间（UTC）。
     *
     * @return 创建时间
     */
    long getCreationTime();

    /**
     * 获取本次执行的预热任务。
     *
     * @return 本次执行的预热任务的数组
     */
    WarmUpTask[] getTasks();
}
