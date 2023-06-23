package com.wingsweaver.kuja.common.utils.support.concurrent.threadsafe;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SynchronizedValueTest {
    @Test
    void test() {
        int startValue = 123456;
        SynchronizedValue<Integer> value = new SynchronizedValue<>(startValue);
        assertEquals(startValue, value.get());

        value.set(123400);
        assertEquals(123400, value.get());

        value.setIfAbsent(120056);
        assertEquals(123400, value.get());

        // 测试 compute
        int ubound = startValue + startValue;
        int maxValue = startValue;
        int threadCount = 100;
        CompletableFuture<?>[] futures = new CompletableFuture[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int valueToSet = ThreadLocalRandom.current().nextInt(startValue, ubound);
            if (maxValue < valueToSet) {
                maxValue = valueToSet;
            }
            CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                value.compute(v -> Math.max(v, valueToSet));
            });
            futures[i] = future;
        }

        CompletableFuture.allOf(futures).join();
        assertEquals(maxValue, value.get());
    }

    @Test
    void test2() {
        SynchronizedValue<String> value = new SynchronizedValue<>();
        assertNull(value.get());

        value.setIfAbsent("hello, world!");
        assertEquals("hello, world!", value.get());
    }
}