package com.wingsweaver.kuja.common.boot.warmup;

/**
 * 默认的预热执行记录 {@link WarmUpExecution} 实现类。
 *
 * @author wingsweaver
 */
public class DefaultWarmUpExecution implements WarmUpExecution {
    private final Object executionId;

    private final WarmUpTask[] tasks;

    public DefaultWarmUpExecution(Object executionId, WarmUpTask[] tasks) {
        this.executionId = executionId;
        this.tasks = tasks;
    }

    @Override
    public Object getExecutionId() {
        return this.executionId;
    }

    @Override
    public WarmUpTask[] getTasks() {
        return this.tasks;
    }

    @Override
    public String toString() {
        return "DefaultWarmUpExecution@" + this.getExecutionId();
    }
}
