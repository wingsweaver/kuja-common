package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * {@link WarmUpTaskExecutor} 的抽象实现。
 *
 * @author wingsweaver
 */
public abstract class AbstractWarmUpTaskExecutor implements WarmUpTaskExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWarmUpTaskExecutor.class);

    private final ApplicationEventPublisher eventPublisher;

    private Supplier<Object> executionIdGenerator;

    public AbstractWarmUpTaskExecutor(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(Collection<WarmUpTask> tasks, WarmUpTaskExecutionCallback callback) {
        LogUtil.trace(LOGGER, "creating warmup execution...");
        WarmUpExecution execution = this.createExecution(tasks);

        LogUtil.trace(LOGGER, "executing warmup execution {} ...", execution);
        this.executeInternal(execution, callback);
    }

    /**
     * 实际的执行操作。
     *
     * @param execution 预热执行记录
     * @param callback  回调函数
     */
    protected abstract void executeInternal(WarmUpExecution execution, WarmUpTaskExecutionCallback callback);

    private static final WarmUpTask[] EMPTY_TASKS = new WarmUpTask[0];

    /**
     * 创建预热执行记录。
     *
     * @param tasks 预热任务的列表
     * @return 预热执行记录
     */
    protected WarmUpExecution createExecution(Collection<WarmUpTask> tasks) {
        Object recordId = this.generateExecutionId();
        WarmUpTask[] taskArray = CollectionUtils.isEmpty(tasks) ? EMPTY_TASKS : tasks.toArray(new WarmUpTask[0]);
        return new DefaultWarmUpExecution(recordId, taskArray);
    }

    /**
     * 生成预热执行记录的 ID。
     *
     * @return 预热执行记录的 ID
     */
    protected Object generateExecutionId() {
        if (this.executionIdGenerator != null) {
            return this.executionIdGenerator.get();
        } else {
            return UUID.randomUUID().toString();
        }
    }

    /**
     * 开始预热任务。
     *
     * @param record   预热执行记录
     * @param callback 回调函数
     */
    protected void start(WarmUpExecution record, WarmUpTaskExecutionCallback callback) {
        LogUtil.trace(LOGGER, "Starting WarmUpExecution {} ...", record);
        this.eventPublisher.publishEvent(new WarmUpStartEvent(this, record));
        if (callback != null) {
            callback.onStart();
        }
    }

    /**
     * 完成预热任务。
     *
     * @param record   预热执行记录
     * @param callback 回调函数
     */
    protected void complete(WarmUpExecution record, WarmUpTaskExecutionCallback callback) {
        LogUtil.trace(LOGGER, "Complete WarmUpExecution {}", record);
        this.eventPublisher.publishEvent(new WarmUpCompleteEvent(this, record));
        if (callback != null) {
            callback.onComplete();
        }
    }

    /**
     * 执行特定的预热处理任务。
     *
     * @param record   预热执行记录
     * @param task     预热任务
     * @param callback 回调函数
     */
    protected void runTask(WarmUpExecution record, WarmUpTask task, WarmUpTaskExecutionCallback callback) {
        long startTime = System.currentTimeMillis();
        Throwable error = null;
        try {
            task.warmUp();
        } catch (Throwable t) {
            error = t;
        }

        // 发布预热任务执行事件
        long endTime = System.currentTimeMillis();
        this.eventPublisher.publishEvent(new WarmUpProgressEvent(this, record, task, startTime, endTime, error));
        if (callback != null) {
            callback.onProgress(task, error);
        }
    }

    public ApplicationEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    public Supplier<Object> getExecutionIdGenerator() {
        return executionIdGenerator;
    }

    public void setExecutionIdGenerator(Supplier<Object> executionIdGenerator) {
        this.executionIdGenerator = executionIdGenerator;
    }
}
