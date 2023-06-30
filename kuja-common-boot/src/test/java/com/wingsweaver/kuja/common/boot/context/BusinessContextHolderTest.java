package com.wingsweaver.kuja.common.boot.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class BusinessContextHolderTest {
    @Test
    void test() {
        assertNull(BusinessContextHolder.getCurrent());
        assertNull(BusinessContextHolder.getCurrent(true));
        BusinessContext businessContext = new MapBusinessContext();
        BusinessContextHolder.setCurrent(businessContext);
        assertSame(businessContext, BusinessContextHolder.getCurrent());
        assertSame(businessContext, BusinessContextHolder.getCurrent(true));

        BusinessContext businessContext2 = new MapBusinessContext();
        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext2)) {
            assertSame(businessContext2, BusinessContextHolder.getCurrent());
            assertSame(businessContext2, BusinessContextHolder.getCurrent(true));
        }

        assertSame(businessContext, BusinessContextHolder.getCurrent());
        assertSame(businessContext, BusinessContextHolder.getCurrent(true));

        BusinessContext businessContext3 = new MapBusinessContext();
        BusinessContextHolder.setCurrent(businessContext3, true);
        assertNull(BusinessContextHolder.getCurrent());
        assertSame(businessContext3, BusinessContextHolder.getCurrent(true));

        BusinessContextHolder.removeCurrent();
        assertNull(BusinessContextHolder.getCurrent());
        assertNull(BusinessContextHolder.getCurrent(true));
    }

    @Test
    void test2() throws InterruptedException {
        BusinessContext businessContext = new MapBusinessContext();
        BusinessContextHolder.setCurrent(businessContext, false);
        try {
            Thread thread = new Thread(() -> {
                assertNull(BusinessContextHolder.getCurrent());
                assertNull(BusinessContextHolder.getCurrent(true));
            });
            thread.start();
            thread.join();
        } finally {
            BusinessContextHolder.removeCurrent();
        }

        BusinessContext businessContext2 = new MapBusinessContext();
        BusinessContextHolder.setCurrent(businessContext2, true);
        try {
            Thread thread = new Thread(() -> {
                assertNull(BusinessContextHolder.getCurrent());
                assertSame(businessContext2, BusinessContextHolder.getCurrent(true));
            });
            thread.start();
            thread.join();
        } finally {
            BusinessContextHolder.removeCurrent();
        }

        BusinessContext businessContext3 = new MapBusinessContext();
        Runnable runnable = BusinessContextMethodWrapper.runnable(() -> {
            assertSame(businessContext3, BusinessContextHolder.getCurrent());
            assertSame(businessContext3, BusinessContextHolder.getCurrent(true));
        }, businessContext3);
        BusinessContextHolder.setCurrent(businessContext3);
        try {
            Thread thread = new Thread(runnable);
            thread.start();
            thread.join();
        } finally {
            BusinessContextHolder.removeCurrent();
        }
    }
}