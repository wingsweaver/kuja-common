package com.wingsweaver.kuja.common.utils.support.concurrent;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadPoolExecutorFactoryTest {

    @Test
    void threadPoolExecutor() throws Exception {
        {
            ThreadPoolProperties properties = new ThreadPoolProperties();
            properties.setEnabled(false);
            assertNull(ThreadPoolExecutorFactory.threadPoolExecutor(properties));
        }
        {
            ThreadPoolProperties properties = new ThreadPoolProperties();
            properties.setCoreSize(-1);
            assertThrows(ExtendedRuntimeException.class, () -> ThreadPoolExecutorFactory.threadPoolExecutor(properties));
        }
        {
            ThreadPoolProperties properties = new ThreadPoolProperties();
            properties.setCoreSize(3);
            properties.setMaxSize(10);
            properties.setThreadPoolName("test1-thread-pool");
            properties.setThreadName("test1");
            ExtendedThreadPoolExecutor executor = (ExtendedThreadPoolExecutor) ThreadPoolExecutorFactory.threadPoolExecutor(properties);
            assertNotNull(executor);
            assertEquals("test1-thread-pool", executor.getName());
            assertTrue(executor.toString().contains("test1-thread-pool"));
            assertTrue(executor.getQueue() instanceof LinkedBlockingQueue);
        }

        {
            ThreadPoolProperties properties = new ThreadPoolProperties();
            properties.setCoreSize(3);
            properties.setCapacity(100);
            properties.setThreadPoolName("test1b-thread-pool");
            ExtendedThreadPoolExecutor executor = (ExtendedThreadPoolExecutor) ThreadPoolExecutorFactory.threadPoolExecutor(properties);
            assertNotNull(executor);
            assertEquals("test1b-thread-pool", executor.getName());
            assertTrue(executor.toString().contains("test1b-thread-pool"));
            assertTrue(executor.getQueue() instanceof ArrayBlockingQueue);
        }
    }

    @Test
    void scheduledThreadPoolExecutor() {
        {
            ThreadPoolProperties properties = new ThreadPoolProperties();
            properties.setEnabled(false);
            assertNull(ThreadPoolExecutorFactory.scheduledThreadPoolExecutor(properties));
        }
        {
            ThreadPoolProperties properties = new ThreadPoolProperties();
            properties.setCoreSize(-1);
            assertThrows(ExtendedRuntimeException.class, () -> ThreadPoolExecutorFactory.scheduledThreadPoolExecutor(properties));
        }

        {
            // 生成 ThreadPoolExecutorFactory#threadPoolExecutor() 的测试用例
            ThreadPoolProperties properties = new ThreadPoolProperties();
            properties.setCoreSize(3);
            properties.setMaxSize(10);
            properties.setThreadPoolName("test1-thread-pool");
            properties.setThreadName("test1");
            ExtendedScheduledThreadPoolExecutor executor = (ExtendedScheduledThreadPoolExecutor)
                    ThreadPoolExecutorFactory.scheduledThreadPoolExecutor(properties);
            assertNotNull(executor);
            assertEquals("test1-thread-pool", executor.getName());
            assertTrue(executor.toString().contains("test1-thread-pool"));
        }
    }
}