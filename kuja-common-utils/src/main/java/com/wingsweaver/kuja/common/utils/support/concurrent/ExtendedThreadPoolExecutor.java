package com.wingsweaver.kuja.common.utils.support.concurrent;

import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 自定义 {@link ThreadPoolExecutor} 扩展类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ExtendedThreadPoolExecutor extends ThreadPoolExecutor {
    /**
     * 线程池名称。
     */
    private String name;

    private BiConsumer<Thread, Runnable> beforeExecuteCallback;

    private BiConsumer<Runnable, Throwable> afterExecuteCallback;

    public ExtendedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ExtendedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                      ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ExtendedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                      RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ExtendedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                      ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
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
