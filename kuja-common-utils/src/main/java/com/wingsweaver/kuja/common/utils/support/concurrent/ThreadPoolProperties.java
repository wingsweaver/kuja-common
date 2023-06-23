package com.wingsweaver.kuja.common.utils.support.concurrent;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池 ({@link ThreadPoolExecutor}) 的基本设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ThreadPoolProperties {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * 线程池的名称（标识）。
     */
    private String threadPoolName;

    /**
     * 线程名称的前缀。
     */
    private String threadName;

    /**
     * 核心线程数。
     */
    private int coreSize = 3;

    /**
     * 最大线程数。
     */
    private int maxSize = 10;

    /**
     * 空线程存活时间。
     */
    private int keepAlive = 60;

    /**
     * 空线程存活时间的单位。
     */
    private TimeUnit keepAliveUnit = TimeUnit.SECONDS;

    /**
     * BlockingQueue 中最多可以储存的记录条数。
     */
    private int capacity = Integer.MAX_VALUE;

    /**
     * 线程超出时的处理策略。
     */
    private Class<? extends RejectedExecutionHandler> rejectHandler = ThreadPoolExecutor.DiscardPolicy.class;
}
