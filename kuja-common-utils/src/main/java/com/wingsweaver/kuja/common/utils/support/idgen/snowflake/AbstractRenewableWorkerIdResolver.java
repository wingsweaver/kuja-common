package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogSection;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.concurrent.SimpleThreadFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.DisposableBean;

import java.security.SecureRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 可以自动续约 WorkerId 的 {@link WorkerIdResolver} 实现类。<br>
 * 通过自动续约的方式，声明应用程序对 WorkerId 的使用权；进而在应用程序结束之后，自动释放 WorkerId。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractRenewableWorkerIdResolver extends AbstractWorkerIdResolver implements DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRenewableWorkerIdResolver.class);

    /**
     * 实际的 WorkerId。
     */
    @Getter(AccessLevel.NONE)
    private final AtomicInteger workerId = new AtomicInteger();

    /**
     * 是否启用自动续约。
     */
    private boolean autoRenew = true;

    /**
     * 用于定期续约 WorkerId 的 ScheduledExecutorService 实例。
     */
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 续约任务的时间单位。
     */
    private TimeUnit renewTimeUnit = TimeUnit.MILLISECONDS;

    /**
     * 续约任务的时间间隔的下限值。
     */
    private long renewIntervalMin = 1000;

    /**
     * 续约任务的时间间隔的上限值。
     */
    private long renewIntervalMax = 2000;

    /**
     * 续约任务。
     */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    private final AtomicReference<ScheduledFuture<?>> renewFutureRef = new AtomicReference<>();

    protected AbstractRenewableWorkerIdResolver(int bits) {
        super(bits);
    }

    @Override
    public int resolveWorkerId() {
        int workerId = super.resolveWorkerId();
        this.workerId.set(workerId);
        this.createRenewTask();
        return workerId;
    }

    public Integer getWorkerId() {
        return this.workerId.get();
    }

    /**
     * 开始续约任务。
     */
    protected void createRenewTask() {
        // 检查是否需要续约
        if (!this.autoRenew) {
            LogUtil.trace(LOGGER, "Auto renew is disabled, skip create renew task.");
            return;
        }

        // 注册续约任务
        SecureRandom secureRandom = new SecureRandom();
        long renewInterval = this.renewIntervalMin + secureRandom.nextInt((int) (this.renewIntervalMax - this.renewIntervalMin));
        LogUtil.trace(LOGGER, () -> "Create renew task, renew interval: " + renewInterval + " with time unit " + this.renewTimeUnit);
        ScheduledFuture<?> future = scheduledExecutorService.schedule(this::renewWorkerIdProc, renewInterval, this.renewTimeUnit);
        this.renewFutureRef.set(future);
    }

    /**
     * 续约 WorkerId 的处理。
     */
    protected void renewWorkerIdProc() {
        // 检查是否需要续约
        if (!this.autoRenew) {
            LogUtil.trace(LOGGER, "Auto renew is disabled, skip renew worker id procedure.");
            return;
        }

        // 执行续约
        LogUtil.trace(LOGGER, "Renewing worker id ...");
        this.renewWorkerId();

        // 重新启动续约任务
        if (this.autoRenew) {
            LogUtil.trace(LOGGER, "Create next renew task");
            this.createRenewTask();
        } else {
            LogUtil.debug(LOGGER, "Auto renew is disabled, skip create next renew task.");
        }
    }

    /**
     * 续约 WorkerId。
     */
    protected abstract void renewWorkerId();

    /**
     * 释放（无效化） WorkerId。
     */
    protected abstract void revokeWorkerId();

    /**
     * 取消续约任务。
     */
    protected void cancelRenewTask() {
        ScheduledFuture<?> future = this.renewFutureRef.getAndSet(null);
        if (future != null) {
            LogUtil.debug(LOGGER, () -> "Cancel renew task " + future);
            future.cancel(false);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 检查设置
        AssertState.Named.notNull("renewTimeUnit", this.renewTimeUnit);
        AssertState.Named.greaterThan("renewIntervalMin", this.renewIntervalMin, 0L);
        AssertState.Named.greaterThan("renewIntervalMax", this.renewIntervalMax, this.renewIntervalMin);

        // 初始化 scheduledExecutorService
        this.initScheduledExecutorService();
        AssertState.Named.notNull("scheduledExecutorService", this.scheduledExecutorService);
    }

    /**
     * 初始化 scheduledExecutorService。
     */
    @SuppressWarnings("PMD.ThreadPoolCreationRule")
    protected void initScheduledExecutorService() {
        if (this.scheduledExecutorService == null) {
            SimpleThreadFactory threadFactory = new SimpleThreadFactory("worker-id-renew");
            this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(threadFactory);
        }
    }

    @Override
    public void destroy() throws Exception {
        LogSection logSection = LogSection.builder(LOGGER).build();
        logSection.enter();

        try {
            this.autoRenew = false;

            logSection.log(Level.TRACE, "Cancelling renew task ...");
            this.cancelRenewTask();

            logSection.log(Level.TRACE, "Revoking worker id ...");
            this.revokeWorkerId();

            logSection.log(Level.TRACE, "Shutdown scheduled executor service.");
            this.scheduledExecutorService.shutdown();
        } catch (Exception e) {
            logSection.fail(e);
        } finally {
            logSection.leave();
        }
    }
}
