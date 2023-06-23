package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.model.ValueWrapper;
import com.wingsweaver.kuja.common.utils.support.lang.ThrowableRunnable;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BusinessContextMethodWrapperTest {
    @Test
    void runnable() {
        Map<String, Object> map = MapUtil.from("id", 10);
        BusinessContext businessContext = BusinessContext.of(map);
        ValueWrapper<Integer> valueWrapper = new ValueWrapper<>();
        Runnable runnable = BusinessContextMethodWrapper.runnable(() -> {
            valueWrapper.setValue(BusinessContextHolder.getCurrent().getAttribute("id", 11));
        });
        Runnable runnable2 = null;

        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext)) {
            assertEquals(10, (int) BusinessContextHolder.getCurrent().getAttribute("id"));

            assertThrows(NullPointerException.class, runnable::run);

            valueWrapper.setValue(null);
            runnable2 = BusinessContextMethodWrapper.runnable(() -> {
                valueWrapper.setValue(BusinessContextHolder.getCurrent().getAttribute("id", 11));
            });
            runnable2.run();
            assertEquals(10, valueWrapper.getValue());
        }

        valueWrapper.setValue(null);
        CompletableFuture.runAsync(runnable2).join();
        assertEquals(10, valueWrapper.getValue());
    }

    @Test
    void runnable2() {
        Map<String, Object> map = MapUtil.from("id", 10);
        BusinessContext businessContext = BusinessContext.of(map);
        ValueWrapper<Integer> valueWrapper = new ValueWrapper<>();
        Runnable runnable = BusinessContextMethodWrapper.runnable(() -> {
            valueWrapper.setValue(BusinessContextHolder.getCurrent().getAttribute("id", 11));
        }, businessContext);


        Map<String, Object> map2 = MapUtil.from("id", 20);
        BusinessContext businessContext2 = BusinessContext.of(map2);
        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext2)) {
            assertEquals(20, (int) BusinessContextHolder.getCurrent().getAttribute("id"));
            runnable.run();
        }
        assertEquals(10, valueWrapper.getValue());

        valueWrapper.setValue(null);
        CompletableFuture.runAsync(runnable).join();
        assertEquals(10, valueWrapper.getValue());
    }

    @Test
    void throwableRunnable() throws Throwable {
        Map<String, Object> map = MapUtil.from("id", 10);
        BusinessContext businessContext = BusinessContext.of(map);
        ValueWrapper<Integer> valueWrapper = new ValueWrapper<>();
        ThrowableRunnable<?> runnable = BusinessContextMethodWrapper.throwableRunnable(() -> {
            valueWrapper.setValue(BusinessContextHolder.getCurrent().getAttribute("id", 11));
        });
        ThrowableRunnable<?> runnable2 = null;

        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext)) {
            assertEquals(10, (int) BusinessContextHolder.getCurrent().getAttribute("id"));

            assertThrows(NullPointerException.class, runnable::run);

            valueWrapper.setValue(null);
            runnable2 = BusinessContextMethodWrapper.throwableRunnable(() -> {
                valueWrapper.setValue(BusinessContextHolder.getCurrent().getAttribute("id", 11));
            });
            runnable2.run();
            assertEquals(10, valueWrapper.getValue());
        }

        valueWrapper.setValue(null);
        ThrowableRunnable<?> finalRunnable = runnable2;
        CompletableFuture.runAsync(() -> {
            try {
                finalRunnable.run();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }).join();
        assertEquals(10, valueWrapper.getValue());
    }

    @Test
    void throwableRunnable2() throws Throwable {
        Map<String, Object> map = MapUtil.from("id", 10);
        BusinessContext businessContext = BusinessContext.of(map);
        ValueWrapper<Integer> valueWrapper = new ValueWrapper<>();
        ThrowableRunnable<?> runnable = BusinessContextMethodWrapper.throwableRunnable(() -> {
            valueWrapper.setValue(BusinessContextHolder.getCurrent().getAttribute("id", 11));
        }, businessContext);

        Map<String, Object> map2 = MapUtil.from("id", 20);
        BusinessContext businessContext2 = BusinessContext.of(map2);
        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext2)) {
            assertEquals(20, (int) BusinessContextHolder.getCurrent().getAttribute("id"));
            runnable.run();
        }
        assertEquals(10, valueWrapper.getValue());

        valueWrapper.setValue(null);
        CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }).join();
        assertEquals(10, valueWrapper.getValue());
    }

    @Test
    void callable() throws Exception {
        Map<String, Object> map = MapUtil.from("id", 10);
        BusinessContext businessContext = BusinessContext.of(map);
        Callable<Integer> callable = BusinessContextMethodWrapper.callable(() ->
                BusinessContextHolder.getCurrent().getAttribute("id", 11));
        Callable<Integer> callable2 = null;

        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext)) {
            assertEquals(10, (int) BusinessContextHolder.getCurrent().getAttribute("id"));

            assertThrows(NullPointerException.class, callable::call);

            callable2 = BusinessContextMethodWrapper.callable(() ->
                    BusinessContextHolder.getCurrent().getAttribute("id", 11));
            assertEquals(10, callable2.call());
        }

        assertEquals(10, callable2.call());
        Callable<Integer> finalCallable = callable2;
        assertEquals(10, CompletableFuture.supplyAsync(() -> {
            try {
                return finalCallable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).get());
    }

    @Test
    void callable2() throws Throwable {
        Map<String, Object> map = MapUtil.from("id", 10);
        BusinessContext businessContext = BusinessContext.of(map);
        Callable<Integer> callable = BusinessContextMethodWrapper.callable(() ->
                BusinessContextHolder.getCurrent().getAttribute("id", 11), businessContext);

        Map<String, Object> map2 = MapUtil.from("id", 20);
        BusinessContext businessContext2 = BusinessContext.of(map2);
        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext2)) {
            assertEquals(20, (int) BusinessContextHolder.getCurrent().getAttribute("id"));
            assertEquals(10, callable.call());
        }

        assertEquals(10, callable.call());
        assertEquals(10, CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).get());
    }

    @Test
    void supplier() {
        Map<String, Object> map = MapUtil.from("id", 10);
        BusinessContext businessContext = BusinessContext.of(map);
        Supplier<Integer> supplier = BusinessContextMethodWrapper.supplier(() ->
                BusinessContextHolder.getCurrent().getAttribute("id", 11));
        Supplier<Integer> supplier2 = null;

        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext)) {
            assertEquals(10, (int) BusinessContextHolder.getCurrent().getAttribute("id"));

            assertThrows(NullPointerException.class, supplier::get);

            supplier2 = BusinessContextMethodWrapper.supplier(() ->
                    BusinessContextHolder.getCurrent().getAttribute("id", 11));
            assertEquals(10, supplier2.get());
        }

        assertEquals(10, supplier2.get());
        assertEquals(10, CompletableFuture.supplyAsync(supplier2).join());
    }

    @Test
    void supplier2() throws Throwable {
        Map<String, Object> map = MapUtil.from("id", 10);
        BusinessContext businessContext = BusinessContext.of(map);
        Supplier<Integer> supplier = BusinessContextMethodWrapper.supplier(() ->
                BusinessContextHolder.getCurrent().getAttribute("id"), businessContext);

        Map<String, Object> map2 = MapUtil.from("id", 20);
        BusinessContext businessContext2 = BusinessContext.of(map2);
        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext2)) {
            assertEquals(20, (int) BusinessContextHolder.getCurrent().getAttribute("id"));
            assertEquals(10, supplier.get());
        }

        assertEquals(10, supplier.get());
        assertEquals(10, CompletableFuture.supplyAsync(supplier).get());
    }
}