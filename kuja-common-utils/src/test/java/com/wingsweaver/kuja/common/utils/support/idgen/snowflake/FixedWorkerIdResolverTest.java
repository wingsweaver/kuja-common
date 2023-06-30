package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FixedWorkerIdResolverTest {
    @Test
    void test() {
        FixedWorkerIdResolver workerIdResolver = new FixedWorkerIdResolver(10);
        workerIdResolver.setWorkerId(987);

        assertEquals(10, workerIdResolver.bits());
        assertEquals(1023, workerIdResolver.maxValue());

        assertEquals(987, workerIdResolver.getWorkerId());
        assertEquals(987, workerIdResolver.resolveWorkerId());
    }

    @Test
    void test2() {
        FixedWorkerIdResolver workerIdResolver = new FixedWorkerIdResolver(10);
        workerIdResolver.setTrimToMaskValue(false);
        assertFalse(workerIdResolver.isTrimToMaskValue());
        workerIdResolver.setWorkerId(5678);
        assertThrows(IllegalStateException.class, workerIdResolver::resolveWorkerId);
    }

    @Test
    void test3() {
        FixedWorkerIdResolver workerIdResolver = new FixedWorkerIdResolver(10);
        workerIdResolver.setTrimToMaskValue(true);
        assertTrue(workerIdResolver.isTrimToMaskValue());
        workerIdResolver.setWorkerId(5678);
        assertEquals(5678 & 1023, workerIdResolver.resolveWorkerId());
    }
}