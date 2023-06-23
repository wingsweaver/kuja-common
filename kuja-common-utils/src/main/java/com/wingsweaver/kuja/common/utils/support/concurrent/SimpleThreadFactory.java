package com.wingsweaver.kuja.common.utils.support.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * 简易的 ThreadFactory 实现类。
 *
 * @author wingsweaver
 */
public class SimpleThreadFactory implements ThreadFactory {
    /**
     * 线程名称的前缀。
     */
    private final String threadName;

    /**
     * 构造函数。
     *
     * @param threadName 线程名称的前缀
     */
    public SimpleThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(this.getThreadName() + "-" + thread.getId());
        return thread;
    }

    public String getThreadName() {
        return threadName;
    }
}
