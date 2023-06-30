package com.wingsweaver.kuja.common.boot.warmup;

import org.springframework.context.ApplicationContext;

/**
 * 同步执行预热任务的 {@link WarmUpTaskExecutor} 实现。
 *
 * @author wingsweaver
 */
public class SyncWarmUpTaskExecutor extends AbstractWarmUpTaskExecutor {
    public SyncWarmUpTaskExecutor(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected void executeInternal(WarmUpExecution execution, WarmUpTaskExecutionCallback callback) {
        this.start(execution, callback);
        for (WarmUpTask task : execution.getTasks()) {
            this.runTask(execution, task, callback);
        }
        this.complete(execution, callback);
    }
}
