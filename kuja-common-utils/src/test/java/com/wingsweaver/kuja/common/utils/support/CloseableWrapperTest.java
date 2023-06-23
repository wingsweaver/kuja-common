package com.wingsweaver.kuja.common.utils.support;

import com.wingsweaver.kuja.common.utils.support.lang.CloseableWrapper;
import com.wingsweaver.kuja.common.utils.support.lang.NonThrowableCloseable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CloseableWrapperTest {
    // 生成 CloseableWrapper 的测试用例
    @Test
    void getDelegate() {
        Object delegate = new Object();
        try (CloseableWrapper<Object> closeableWrapper = new CloseableWrapper<>(delegate)) {
            assertEquals(delegate, closeableWrapper.getDelegate());
        }
    }

    @SuppressWarnings("EmptyTryBlock")
    @Test
    void close() {
        int initValue = ThreadLocalRandom.current().nextInt();
        AtomicInteger value = new AtomicInteger(initValue);

        // 非 Closeable 类型
        try (CloseableWrapper<Object> closeableWrapper = new CloseableWrapper<>(value)) {
        } finally {
            assertEquals(initValue, value.get());
        }

        // Closeable 类型
        try (CloseableWrapper<Object> closeableWrapper = new CloseableWrapper<>(new NonThrowableCloseable() {
            @Override
            public void close() {
                value.decrementAndGet();
            }
        })) {
        } finally {
            assertEquals(initValue - 1, value.get());
        }

        // Closeable 类型，抛出 Exception
        try (CloseableWrapper<Object> closeableWrapper = new CloseableWrapper<>(new AutoCloseable() {
            @Override
            public void close() throws Exception {
                throw new Exception("按需抛出异常");
            }
        })) {
        } catch (RuntimeException e) {
            // 忽略异常
        } finally {
            assertEquals(initValue - 1, value.get());
        }

        // Closeable 类型，抛出 RuntimeException
        try (CloseableWrapper<Object> closeableWrapper = new CloseableWrapper<>(new NonThrowableCloseable() {
            @Override
            public void close() {
                throw new RuntimeException("按需抛出异常");
            }
        })) {
        } catch (RuntimeException e) {
            // 忽略异常
        } finally {
            assertEquals(initValue - 1, value.get());
        }
    }
}