package com.wingsweaver.kuja.common.utils.support.concurrent;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import org.springframework.objenesis.ObjenesisHelper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工厂类。
 *
 * @author wingsweaver
 */
public final class ThreadPoolExecutorFactory {
    private ThreadPoolExecutorFactory() {
        // 禁止实例化
    }

    /**
     * 生成 {@link ThreadPoolExecutor} 实例。
     *
     * @param properties 线程池设置
     * @return ThreadPoolExecutor 实例
     */
    public static ThreadPoolExecutor threadPoolExecutor(ThreadPoolProperties properties) {
        if (properties == null || !properties.isEnabled()) {
            return null;
        }

        try {
            ThreadFactory threadFactory = new SimpleThreadFactory(properties.getThreadName());
            BlockingQueue<Runnable> blockingQueue = createBlockingQueue(properties.getCapacity());
            RejectedExecutionHandler rejectedExecutionHandler = ObjenesisHelper.newInstance(properties.getRejectHandler());
            ExtendedThreadPoolExecutor executor = new ExtendedThreadPoolExecutor(properties.getCoreSize(), properties.getMaxSize(),
                    properties.getKeepAlive(), properties.getKeepAliveUnit(), blockingQueue, threadFactory, rejectedExecutionHandler);
            executor.setName(properties.getThreadPoolName());
            return executor;
        } catch (Exception ex) {
            throw new ExtendedRuntimeException("Failed to create ThreadPoolExecutor instance", ex)
                    .withExtendedAttribute("properties", properties);
        }
    }

    /**
     * 生成 {@link ScheduledThreadPoolExecutor} 实例。
     *
     * @param properties 线程池设置
     * @return ThreadPoolExecutor 实例
     */
    public static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor(ThreadPoolProperties properties) {
        if (properties == null || !properties.isEnabled()) {
            return null;
        }

        try {
            ThreadFactory threadFactory = new SimpleThreadFactory(properties.getThreadName());
            RejectedExecutionHandler rejectedExecutionHandler = ObjenesisHelper.newInstance(properties.getRejectHandler());
            ExtendedScheduledThreadPoolExecutor executor = new ExtendedScheduledThreadPoolExecutor(properties.getCoreSize(),
                    threadFactory, rejectedExecutionHandler);
            executor.setName(properties.getThreadPoolName());
            return executor;
        } catch (Exception ex) {
            throw new ExtendedRuntimeException("Failed to create ScheduledThreadPoolExecutor instance", ex)
                    .withExtendedAttribute("properties", properties);
        }
    }

    private static BlockingQueue<Runnable> createBlockingQueue(int capacity) {
        if (capacity <= 0 || capacity == Integer.MAX_VALUE) {
            return new LinkedBlockingQueue<>(capacity);
        } else {
            return new ArrayBlockingQueue<>(capacity);
        }
    }
}
