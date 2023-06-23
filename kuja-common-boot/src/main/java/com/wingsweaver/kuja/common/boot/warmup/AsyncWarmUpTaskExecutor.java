package com.wingsweaver.kuja.common.boot.warmup;

import org.springframework.context.ApplicationEventPublisher;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 异步执行预热任务的 {@link WarmUpTaskExecutor} 实现。
 *
 * @author wingsweaver
 */
public class AsyncWarmUpTaskExecutor extends AbstractWarmUpTaskExecutor {
    private final Executor executor;

    public AsyncWarmUpTaskExecutor(ApplicationEventPublisher eventPublisher, Executor executor) {
        super(eventPublisher);
        this.executor = executor;
    }

    public AsyncWarmUpTaskExecutor(ApplicationEventPublisher eventPublisher) {
        this(eventPublisher, null);
    }

    @Override
    protected void executeInternal(WarmUpExecution execution, WarmUpTaskExecutionCallback callback) {
        // 发布预热开始事件
        this.start(execution, callback);

        // 执行预热任务
        WarmUpTask[] tasks = execution.getTasks();
        if (tasks == null || tasks.length < 1) {
            // 如果没有预热任务，那么直接发布预热完成事件
            this.complete(execution, callback);
        } else if (this.executor != null) {
            // 如果指定了线程池，那么在指定线程池中执行预热事件
            this.executeWithExecutor(execution, tasks, callback);
        } else {
            // 如果没有指定线程池，那么在 CompletableFuture 的默认线程池中执行预热事件
            this.executeWithoutExecutor(execution, tasks, callback);
        }
    }

    protected void executeWithExecutor(WarmUpExecution record, WarmUpTask[] tasks, WarmUpTaskExecutionCallback callback) {
        CompletableFuture<?>[] futures = new CompletableFuture[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            WarmUpTask task = tasks[i];
            futures[i] = CompletableFuture.runAsync(() -> this.runTask(record, task, callback), this.executor);
        }
        CompletableFuture.allOf(futures).whenCompleteAsync((v, t) -> this.complete(record, callback), this.executor);
    }

    protected void executeWithoutExecutor(WarmUpExecution record, WarmUpTask[] tasks, WarmUpTaskExecutionCallback callback) {
        CompletableFuture<?>[] futures = new CompletableFuture[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            WarmUpTask task = tasks[i];
            futures[i] = CompletableFuture.runAsync(() -> this.runTask(record, task, callback));
        }
        CompletableFuture.allOf(futures).whenCompleteAsync((v, t) -> this.complete(record, callback));
    }
}
