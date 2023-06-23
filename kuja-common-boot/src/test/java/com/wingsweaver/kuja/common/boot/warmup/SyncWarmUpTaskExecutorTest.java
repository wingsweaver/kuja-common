package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class SyncWarmUpTaskExecutorTest {
    @Test
    void test() {
        ApplicationEventPublisher eventPublisher = DummyApplicationEventPublisher.INSTANCE;
        SyncWarmUpTaskExecutor warmUpTaskExecutor = new SyncWarmUpTaskExecutor(eventPublisher);
        assertSame(eventPublisher, warmUpTaskExecutor.getEventPublisher());
        assertNull(warmUpTaskExecutor.getExecutionIdGenerator());

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
    void test2() throws ExecutionException, InterruptedException {
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
        SyncWarmUpTaskExecutor warmUpTaskExecutor = new SyncWarmUpTaskExecutor(eventPublisher);

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
    void test3() {
        ApplicationEventPublisher eventPublisher = DummyApplicationEventPublisher.INSTANCE;
        Supplier<Object> idGenerator = () -> UUID.randomUUID().toString();
        SyncWarmUpTaskExecutor warmUpTaskExecutor = new SyncWarmUpTaskExecutor(eventPublisher);
        warmUpTaskExecutor.setExecutionIdGenerator(idGenerator);
        assertSame(idGenerator, warmUpTaskExecutor.getExecutionIdGenerator());
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