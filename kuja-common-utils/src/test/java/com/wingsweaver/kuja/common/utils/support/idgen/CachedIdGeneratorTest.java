package com.wingsweaver.kuja.common.utils.support.idgen;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CachedIdGeneratorTest {
    AtomicLongIdGenerator atomicLongIdGenerator = new AtomicLongIdGenerator();

    UuidStringIdGenerator uuidStringIdGenerator = new UuidStringIdGenerator();

    FixedBackOff fixedBackOff = new FixedBackOff(10, 10);

    FixedIdGenerator<Long> fixedLongIdGenerator = new FixedIdGenerator<>(System.currentTimeMillis());

    RoundRobinIdGenerator<String> roundRobinIdGenerator = new RoundRobinIdGenerator<>("tom", "jerry", "kate", "elsa");

    @Test
    void test() {
        assertThrows(NullPointerException.class, () -> new CachedIdGenerator<>(null, 1, fixedBackOff));
        assertThrows(IllegalArgumentException.class, () -> new CachedIdGenerator<>(atomicLongIdGenerator, 0, fixedBackOff));
        assertThrows(NullPointerException.class, () -> new CachedIdGenerator<>(uuidStringIdGenerator, 1, null));
    }

    @Test
    void test2() {
        CachedIdGenerator<Long> cachedIdGenerator = new CachedIdGenerator<>(atomicLongIdGenerator, 1, fixedBackOff);
        assertSame(atomicLongIdGenerator, cachedIdGenerator.getIdGenerator());
        assertEquals(1, cachedIdGenerator.getCacheSize());
        assertSame(fixedBackOff, cachedIdGenerator.getBackOff());
        int count = ThreadLocalRandom.current().nextInt(10000, 20000);
        CompletableFuture<?>[] futures = new CompletableFuture[count];
        Set<Long> ids = new HashSet<>(MapUtil.hashInitCapacity(count));
        for (int i = 0; i < count; i++) {
            CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                long id = cachedIdGenerator.nextId();
                assertFalse(ids.contains(id));
                ids.add(id);
            });
            futures[i] = future;
        }

        CompletableFuture.allOf(futures).join();
    }

    @Test
    void test3() {
        CachedIdGenerator<String> cachedIdGenerator = new CachedIdGenerator<>(uuidStringIdGenerator, 1, fixedBackOff);
        assertSame(uuidStringIdGenerator, cachedIdGenerator.getIdGenerator());
        assertEquals(1, cachedIdGenerator.getCacheSize());
        assertSame(fixedBackOff, cachedIdGenerator.getBackOff());
        int count = ThreadLocalRandom.current().nextInt(10000, 20000);
        CompletableFuture<?>[] futures = new CompletableFuture[count];
        Set<String> ids = new HashSet<>(MapUtil.hashInitCapacity(count));
        for (int i = 0; i < count; i++) {
            CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                String id = cachedIdGenerator.nextId();
                assertFalse(ids.contains(id));
                ids.add(id);
            });
            futures[i] = future;
        }

        CompletableFuture.allOf(futures).join();
    }

    @Test
    void test4() {
        CachedIdGenerator<Long> cachedIdGenerator = new CachedIdGenerator<>(fixedLongIdGenerator, 10, fixedBackOff);
        assertSame(fixedLongIdGenerator, cachedIdGenerator.getIdGenerator());
        assertEquals(10, cachedIdGenerator.getCacheSize());
        assertSame(fixedBackOff, cachedIdGenerator.getBackOff());

        // 第一次可以成功获取到id
        Long nextId = cachedIdGenerator.nextId();
        assertEquals(fixedLongIdGenerator.getId(), nextId);

        // 之后都不能获取到id，因为不会产生新的 id
        int count = 10;
        for (int i = 0; i < 10; i++) {
            assertNull(cachedIdGenerator.nextId());
        }
    }

    @Test
    void test5() {
        CachedIdGenerator<String> cachedIdGenerator = new CachedIdGenerator<>(roundRobinIdGenerator, 10, fixedBackOff);
        assertSame(roundRobinIdGenerator, cachedIdGenerator.getIdGenerator());
        assertEquals(10, cachedIdGenerator.getCacheSize());
        assertSame(fixedBackOff, cachedIdGenerator.getBackOff());

        // 前 N 次都可以正确获取到 id
        int count = roundRobinIdGenerator.getSize();
        for (int i = 0; i < count; i++) {
            String nextId = cachedIdGenerator.nextId();
            assertEquals(roundRobinIdGenerator.getIds()[i], nextId);
        }

        // 之后都不能获取到id，因为不会产生新的 id
        count = 10;
        for (int i = 0; i < 10; i++) {
            assertNull(cachedIdGenerator.nextId());
        }
    }

    @Getter
    static class FixedIdGenerator<T> implements IdGenerator<T> {
        private final T id;

        public FixedIdGenerator(T id) {
            this.id = id;
        }

        @Override
        public T nextId() {
            return id;
        }
    }

    @Getter
    static class RoundRobinIdGenerator<T> implements IdGenerator<T> {
        private final T[] ids;
        private final int size;

        private final AtomicInteger index = new AtomicInteger();

        public RoundRobinIdGenerator(T... ids) {
            this.ids = ids;
            this.size = ids.length;
        }

        @Override
        public T nextId() {
            return ids[index.getAndIncrement() % size];
        }
    }
}