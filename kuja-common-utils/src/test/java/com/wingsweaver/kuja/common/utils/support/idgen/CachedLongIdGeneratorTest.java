package com.wingsweaver.kuja.common.utils.support.idgen;

import org.junit.jupiter.api.Test;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CachedLongIdGeneratorTest {
    @Test
    void test() throws Exception {
        AtomicLongIdGenerator idGenerator = new AtomicLongIdGenerator();
        BackOff backOff = new FixedBackOff(100L, 10L);
        CachedLongIdGenerator cached = new CachedLongIdGenerator(idGenerator, 10, backOff);
        cached.afterPropertiesSet();
        assertEquals(0, cached.nextId());
    }
}