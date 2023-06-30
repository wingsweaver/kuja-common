package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

/**
 * {@link WarmUpTaskExecutor} 的抽象实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractWarmUpTaskExecutor extends AbstractComponent implements WarmUpTaskExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWarmUpTaskExecutor.class);

    /**
     * ApplicationEventPublisher 实例。
     */
    private ApplicationEventPublisher eventPublisher;

    /**
     * WarmUpExecution 的 ID 生成器。
     */
    private StringIdGenerator idGenerator;

    /**
     * 构造函数。
     *
     * @param applicationContext 应用上下文
     */
    public AbstractWarmUpTaskExecutor(ApplicationContext applicationContext) {
        this.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 eventPublisher
        this.initEventPublisher();

        // 初始化 idGenerator
        this.initIdGenerator();
    }

    /**
     * 初始化 eventPublisher。
     */
    protected void initEventPublisher() {
        if (this.eventPublisher == null) {
            this.eventPublisher = this.getBean(ApplicationEventPublisher.class, this::getApplicationContext);
        }
    }

    /**
     * 初始化 idGenerator。
     */
    protected void initIdGenerator() {
        if (this.idGenerator == null) {
            this.idGenerator = this.getBean(StringIdGenerator.class, () -> StringIdGenerator.FALLBACK);
        }
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

    /**
     * 创建预热执行记录。
     *
     * @param tasks 预热任务的列表
     * @return 预热执行记录
     */
    protected WarmUpExecution createExecution(Collection<WarmUpTask> tasks) {
        String id = this.idGenerator.nextId();
        return new DefaultWarmUpExecution(id, tasks);
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
}
