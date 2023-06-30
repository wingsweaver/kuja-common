package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.StaticApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class SyncWarmUpTaskExecutorTest {
    @Test
    void test() throws Exception {
        ApplicationContext applicationContext = new StaticApplicationContext();
        SyncWarmUpTaskExecutor warmUpTaskExecutor = new SyncWarmUpTaskExecutor(applicationContext);
        ApplicationEventPublisher eventPublisher = DummyApplicationEventPublisher.INSTANCE;
        warmUpTaskExecutor.setEventPublisher(eventPublisher);
        assertSame(eventPublisher, warmUpTaskExecutor.getEventPublisher());
        assertNull(warmUpTaskExecutor.getIdGenerator());
        warmUpTaskExecutor.afterPropertiesSet();

        int count = 100;
        List<WarmUpTask> tasks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            tasks.add(new CustomWarmUpTask(i));
        }
        warmUpTaskExecutor.execute(tasks);
    }

    static class CustomWarmUpTask implements WarmUpTask {
        private final int index;

        CustomWarmUpTask(int index) {
            this.index = index;
        }

        @Override
        public void warmUp() {
            System.out.println("[" + Thread.currentThread() + "] CustomWarmUpTask.warmUp: " + index);
        }
    }

    @Test
    void test2() throws Exception {
        ApplicationEventPublisher eventPublisher = DummyApplicationEventPublisher.INSTANCE;

        CompletableFuture<Tuple2<Integer, Integer>> warmUpExecutionFuture = new CompletableFuture<>();
        WarmUpTaskExecutionCallback callback = new WarmUpTaskExecutionCallback() {
            private int totalCount = 0;

            private int failedCount = 0;

            @Override
            public void onStart() {
                System.out.println("Start callback");
            }

            @Override
            public void onComplete() {
                System.out.println("Complete callback");
                warmUpExecutionFuture.complete(Tuple2.of(this.totalCount, this.failedCount));
            }

            @Override
            public void onProgress(WarmUpTask warmUpTask, Throwable error) {
                this.totalCount++;

                if (error == null) {
                    System.out.println(warmUpTask + " completed successfully");
                } else {
                    System.out.println(warmUpTask + " failed: " + error.getMessage());
                    this.failedCount++;
                }
            }
        };
        ApplicationContext applicationContext = new StaticApplicationContext();
        SyncWarmUpTaskExecutor warmUpTaskExecutor = new SyncWarmUpTaskExecutor(applicationContext);
        warmUpTaskExecutor.setEventPublisher(eventPublisher);
        warmUpTaskExecutor.afterPropertiesSet();

        int count = 100;
        List<WarmUpTask> tasks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            tasks.add(new CustomWarmUpTask2(i));
        }
        warmUpTaskExecutor.execute(tasks, callback);

        // 等待结束
        Tuple2<Integer, Integer> result = warmUpExecutionFuture.get();
        assertEquals(100, result.getT1());
        assertEquals(10, result.getT2());
    }

    static class CustomWarmUpTask2 implements WarmUpTask {
        private final int index;

        CustomWarmUpTask2(int index) {
            this.index = index;
        }

        @Override
        public void warmUp() {
            System.out.println("[" + Thread.currentThread() + "] CustomWarmUpTask2.warmUp: " + index);
            if (this.index % 10 == 1) {
                System.out.println("[" + Thread.currentThread() + "] error happened: " + this);
                throw new RuntimeException("error happened in " + this);
            }
        }

        @Override
        public String toString() {
            return "CustomWarmUpTask2#" + index;
        }
    }

    @Test
    void test3() throws Exception {
        ApplicationContext applicationContext = new StaticApplicationContext();
        SyncWarmUpTaskExecutor warmUpTaskExecutor = new SyncWarmUpTaskExecutor(applicationContext);
        ApplicationEventPublisher eventPublisher = DummyApplicationEventPublisher.INSTANCE;
        warmUpTaskExecutor.setEventPublisher(eventPublisher);
        StringIdGenerator idGenerator = StringIdGenerator.FALLBACK;
        warmUpTaskExecutor.setIdGenerator(idGenerator);
        warmUpTaskExecutor.afterPropertiesSet();
        assertSame(idGenerator, warmUpTaskExecutor.getIdGenerator());
        warmUpTaskExecutor.execute(null);
    }

    static class DummyApplicationEventPublisher implements ApplicationEventPublisher {
        private static final DummyApplicationEventPublisher INSTANCE = new DummyApplicationEventPublisher();

        @Override
        public void publishEvent(ApplicationEvent event) {
            System.out.println("publish application event: " + event);
        }

        @Override
        public void publishEvent(Object event) {
            System.out.println("publish payload event: " + event);
        }
    }
}