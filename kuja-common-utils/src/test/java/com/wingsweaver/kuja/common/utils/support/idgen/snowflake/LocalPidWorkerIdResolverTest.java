package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalPidWorkerIdResolverTest {
    @Test
    void test() {
        LocalPidWorkerIdResolver workerIdResolver = new LocalPidWorkerIdResolver(10);
        assertTrue(workerIdResolver.resolveWorkerId() > 0);
    }
}