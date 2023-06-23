package com.wingsweaver.kuja.common.boot.warmup;

import lombok.Getter;

/**
 * 执行特定预热处理任务的事件。
 *
 * @author wingsweaver
 */
public class WarmUpProgressEvent extends WarmUpEvent {
    @Getter
    private final WarmUpTask task;

    @Getter
    private final long startTime;

    @Getter
    private final long endTime;

    @Getter
    private final Throwable error;

    public WarmUpProgressEvent(Object source, WarmUpExecution record, WarmUpTask task, long startTime, long endTime, Throwable error) {
        super(source, record);
        this.task = task;
        this.startTime = startTime;
        this.endTime = endTime;
        this.error = error;
    }
}
