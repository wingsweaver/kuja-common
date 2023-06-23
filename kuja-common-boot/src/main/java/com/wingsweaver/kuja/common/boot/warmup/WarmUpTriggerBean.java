package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 预热处理的执行器。
 *
 * @author wingsweaver
 */
public class WarmUpTriggerBean implements InitializingBean, SmartLifecycle, CommandLineRunner, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarmUpTriggerBean.class);

    @Getter
    @Setter
    private ApplicationContext applicationContext;

    @Getter
    @Setter
    private WarmUpProperties properties;

    @Override
    public void run(String... args) throws Exception {
        if (this.properties.isEnabled() && this.properties.getMode() == WarmUpProperties.Mode.RUNNER) {
            this.warmUp();
        }
    }

    @Override
    public void start() {
        if (this.properties.isEnabled() && this.properties.getMode() == WarmUpProperties.Mode.LIFE_CYCLE) {
            this.warmUp();
        }
    }

    /**
     * 执行预热处理。
     */
    public void warmUp() {
        // 获取所有的预热任务
        List<WarmUpTask> tasks = this.applicationContext.getBeanProvider(WarmUpTask.class)
                .orderedStream().collect(Collectors.toList());

        // 生成任务执行器
        LogUtil.trace(LOGGER, "Creating warmup task executor ...");
        WarmUpTaskExecutor executor = this.createTaskExecutor();

        // 执行任务
        LogUtil.trace(LOGGER, "Executing warmup task executor {} ...", executor);
        executor.execute(tasks);
    }

    /**
     * 创建任务执行器。
     *
     * @return 任务执行器
     */
    protected WarmUpTaskExecutor createTaskExecutor() {
        // 如果指定了 WarmUpTaskExecutor 的话，使用指定的 WarmUpTaskExecutor
        String taskExecutorName = StringUtil.trimToEmpty(this.properties.getTaskExecutorName());
        if (!taskExecutorName.isEmpty()) {
            return this.applicationContext.getBean(taskExecutorName, WarmUpTaskExecutor.class);
        }

        // 如果没有指定 WarmUpTaskExecutor 的话，那么使用默认的 WarmUpTaskExecutor
        if (this.properties.isAsync()) {
            String executorName = this.properties.getAsyncExecutorName();
            if (StringUtils.hasText(executorName)) {
                // 使用指定的 Executor 创建 AsyncWarmUpTaskExecutor 实例
                Executor executor = this.applicationContext.getBean(executorName, Executor.class);
                return new AsyncWarmUpTaskExecutor(this.applicationContext, executor);
            } else {
                // 使用默认的 Executor 创建 AsyncWarmUpTaskExecutor 实例
                return new AsyncWarmUpTaskExecutor(this.applicationContext);
            }
        } else {
            // 创建同步的 WarmUpTaskExecutor 实例
            return new SyncWarmUpTaskExecutor(this.applicationContext);
        }
    }

    @Override
    public void stop() {
        // 什么也不做
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public int getOrder() {
        Integer order = this.properties.getOrder();
        return (order != null) ? order : KujaCommonBootOrders.WARM_UP_TRIGGER;
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("applicationContext", this.getApplicationContext());
        AssertState.Named.notNull("properties", this.getProperties());
    }
}
