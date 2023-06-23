package com.wingsweaver.kuja.common.utils.support.lang;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadUtilTest {
    @Test
    void teset() {
        assertTrue(ThreadUtil.sleep(TimeUnit.MILLISECONDS, 100));
        assertTrue(ThreadUtil.sleep(100));

        AtomicReference<Thread> sleepThread = new AtomicReference<>();
        CompletableFuture<?>[] futures = new CompletableFuture[2];
        futures[0] = CompletableFuture.runAsync(() -> {
            System.out.println("Start long sleep in thread " + Thread.currentThread() + " ...");
            sleepThread.set(Thread.currentThread());
            assertFalse(ThreadUtil.sleep(TimeUnit.MINUTES, 1));
        });
        futures[1] = CompletableFuture.runAsync(() -> {
            ThreadUtil.sleep(100);
            while (true) {
                if (sleepThread.get() != null) {
                    System.out.println("Interrupt long sleep from thread " + Thread.currentThread() + " ...");
                    sleepThread.get().interrupt();
                    break;
                }
            }
        });
        CompletableFuture.allOf(futures).join();
    }
}