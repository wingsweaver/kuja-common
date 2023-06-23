package com.wingsweaver.kuja.common.utils.support.concurrent;

import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.function.BiConsumer;

/**
 * 自定义 {@link ScheduledThreadPoolExecutor} 扩展类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ExtendedScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {
    /**
     * 线程池名称。
     */
    private String name;

    private BiConsumer<Thread, Runnable> beforeExecuteCallback;

    private BiConsumer<Runnable, Throwable> afterExecuteCallback;

    public ExtendedScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public ExtendedScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public ExtendedScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public ExtendedScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        if (this.getBeforeExecuteCallback() != null) {
            this.getBeforeExecuteCallback().accept(t, r);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (this.getAfterExecuteCallback() != null) {
            this.getAfterExecuteCallback().accept(r, t);
        }
        super.afterExecute(r, t);
    }

    @Override
    public String toString() {
        if (StringUtil.isEmpty(this.getName())) {
            return super.toString();
        } else {
            return this.getClass().getName() + "#" + this.getName();
        }
    }
}
