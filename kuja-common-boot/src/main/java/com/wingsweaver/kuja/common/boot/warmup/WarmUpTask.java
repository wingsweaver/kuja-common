package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 单个预热处理的任务。<br>
 * 可以指定多个预热处理的任务，每个任务都会被 {@link WarmUpTaskExecutor} 调度执行。<br>
 * 根据具体的实现，可能同步执行，也可能异步执行。<br>
 * 考虑到不阻塞主线程，建议使用异步执行的实现。
 *
 * @author wingsweaver
 */
public interface WarmUpTask extends DefaultOrdered {
    /**
     * 执行预热处理。
     */
    void warmUp();
}
