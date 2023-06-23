package com.wingsweaver.kuja.common.utils.support.idgen;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AtomicLongIdGeneratorTest {

    @Test
    void test() {
        AtomicLongIdGenerator idGenerator = new AtomicLongIdGenerator();
        assertEquals(0, idGenerator.nextId());

        int count = ThreadLocalRandom.current().nextInt(10000, 20000);
        CompletableFuture<?>[] futures = new CompletableFuture[count];
        Set<Long> ids = new HashSet<>(MapUtil.hashInitCapacity(count));
        for (int i = 0; i < count; i++) {
            CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                long id = idGenerator.nextId();
                assertFalse(ids.contains(id));
                ids.add(id);
            });
            futures[i] = future;
        }

        CompletableFuture.allOf(futures).join();
    }
}