package com.wingsweaver.kuja.common.utils.support.idgen;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalLongIdGeneratorTest {
    @Test
    void test() {
        LocalLongIdGenerator idGenerator = new LocalLongIdGenerator();
        assertNotNull(idGenerator.nextId());

        int count = ThreadLocalRandom.current().nextInt(10000, 20000);
        CompletableFuture<?>[] futures = new CompletableFuture[count];
        Set<Long> ids = new HashSet<>(MapUtil.hashInitCapacity(count));
        for (int i = 0; i < count; i++) {
            CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                long id = idGenerator.nextId();
//                System.out.println(id);
                assertFalse(ids.contains(id), () -> {
                    Tuple2<Long, Long> tuple2 = idGenerator.parse(id);
                    return "id: " + id + " has been generated before, timestamp = " + tuple2.getT1() + ", sequence = " + tuple2.getT2();
                });
                ids.add(id);
            });
            futures[i] = future;
        }

        CompletableFuture.allOf(futures).join();
    }
}