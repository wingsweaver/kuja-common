package com.wingsweaver.kuja.common.utils.support.idgen;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UuidStringIdGeneratorTest {
    @Test
    void test() {
        UuidStringIdGenerator idGenerator = new UuidStringIdGenerator();

        {
            assertTrue(idGenerator.isWithHyphens());
            String id = idGenerator.nextId();
            assertEquals(36, id.length());
            assertTrue(id.contains("-"));
        }

        {
            idGenerator.setWithHyphens(true);
            String id = idGenerator.nextId();
            assertEquals(36, id.length());
            assertTrue(id.contains("-"));
        }

        {
            idGenerator.setWithHyphens(false);
            String id = idGenerator.nextId();
            assertEquals(32, id.length());
            assertFalse(id.contains("-"));
        }

        {
            int count = ThreadLocalRandom.current().nextInt(10000, 20000);
            CompletableFuture<?>[] futures = new CompletableFuture[count];
            Set<String> ids = new HashSet<>(MapUtil.hashInitCapacity(count));
            for (int i = 0; i < count; i++) {
                CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                    String id = idGenerator.nextId();
                    synchronized (ids) {
                        assertFalse(ids.contains(id));
                        ids.add(id);
                    }
                });
                futures[i] = future;
            }

            CompletableFuture.allOf(futures).join();
        }
    }
}