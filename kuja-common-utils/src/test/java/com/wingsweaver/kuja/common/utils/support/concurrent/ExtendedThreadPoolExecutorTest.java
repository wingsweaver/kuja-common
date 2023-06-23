package com.wingsweaver.kuja.common.utils.support.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExtendedThreadPoolExecutorTest {
    int corePoolSize = 3;

    int maximumPoolSize = 10;

    long keepAliveTime = 10;

    int poolCapacity = 10_0000;

    TimeUnit timeUnit = TimeUnit.SECONDS;

    RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

    ThreadFactory createThreadFactory(String threadName) {
        return new SimpleThreadFactory(threadName);
    }

    BlockingQueue<Runnable> createWorkerQueue(int capacity) {
        return new ArrayBlockingQueue<>(capacity);
    }

    @Test
    void test() throws Exception {
        BlockingQueue<Runnable> workQueue = createWorkerQueue(this.poolCapacity);
        ExtendedThreadPoolExecutor executor = new ExtendedThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize,
                this.keepAliveTime, this.timeUnit, workQueue);

        assertEquals(this.corePoolSize, executor.getCorePoolSize());
        assertEquals(this.maximumPoolSize, executor.getMaximumPoolSize());
        assertEquals(this.keepAliveTime, executor.getKeepAliveTime(this.timeUnit));
        assertEquals(workQueue, executor.getQueue());
        assertNull(executor.getName());
        assertNull(executor.getBeforeExecuteCallback());
        assertNull(executor.getAfterExecuteCallback());
        assertNotNull(executor.toString());

        // 测试 execute() 方法
        int count = ThreadLocalRandom.current().nextInt(100, 200);
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < count; i++) {
            executor.execute(counter::getAndIncrement);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(count, counter.get());
    }

    @Test
    void test2() throws Exception {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory threadFactory = createThreadFactory("test2");
        ExtendedThreadPoolExecutor executor = new ExtendedThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize,
                this.keepAliveTime, this.timeUnit, workQueue, threadFactory);
        executor.setName("test2-executor");

        assertEquals("test2-executor", executor.getName());
        assertNull(executor.getBeforeExecuteCallback());
        assertNull(executor.getAfterExecuteCallback());
        assertNotNull(executor.toString());

        // 设置 beforeExecuteCallback 和 afterExecuteCallback
        executor.setBeforeExecuteCallback((thread, runnable) -> {
            System.out.println("[" + thread + "] before execute: " + runnable);
        });
        executor.setAfterExecuteCallback((runnable, throwable) -> {
            System.out.println("[" + Thread.currentThread() + "] after execute: " + runnable + ", throwable: " + throwable);
        });

        // 测试 submit(Runnable) 方法
        int count = ThreadLocalRandom.current().nextInt(100, 200);
        Future<?>[] futures = new Future[count];
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < count; i++) {
            final int index = i;
            Runnable runnable = () -> counter.addAndGet(index);
            futures[i] = executor.submit(runnable, index);
        }
        for (Future<?> future : futures) {
            future.get();
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(count * (count - 1) / 2, counter.get());
    }

    @Test
    void test3() throws Exception {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ExtendedThreadPoolExecutor executor = new ExtendedThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize,
                this.keepAliveTime, this.timeUnit, workQueue, this.rejectedExecutionHandler);

        // 测试 submit(Runnable, T) 方法
        int count = ThreadLocalRandom.current().nextInt(100, 200);
        Future<Integer>[] futures = new Future[count];
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < count; i++) {
            final int index = i;
            Runnable runnable = () -> counter.addAndGet(index);
            futures[i] = executor.submit(runnable, index);
        }

        for (int i = 0; i < count; i++) {
            assertEquals(i, futures[i].get());
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(count * (count - 1) / 2, counter.get());
    }

    @Test
    void test4() throws Exception {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory threadFactory = new SimpleThreadFactory("test4");
        ExtendedThreadPoolExecutor executor = new ExtendedThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize,
                this.keepAliveTime, this.timeUnit, workQueue, threadFactory, this.rejectedExecutionHandler);

        // 测试 submit(Callable) 方法
        int count = ThreadLocalRandom.current().nextInt(100, 200);
        Future<Integer>[] futures = new Future[count];
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < count; i++) {
            final int index = i;
            Callable<Integer> callable = () -> {
                counter.addAndGet(index);
                return index;
            };
            futures[i] = executor.submit(callable);
        }

        for (int i = 0; i < count; i++) {
            assertEquals(i, futures[i].get());
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(count * (count - 1) / 2, counter.get());
    }
}