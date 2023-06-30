package com.wingsweaver.kuja.common.utils.support.idgen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeStampLongIdGeneratorTest {
    @Test
    void test() {
        LocalLongIdGenerator idGenerator = LocalLongIdGenerator.INSTANCE;
        assertEquals(LocalLongIdGenerator.DEFAULT_SEQUENCE_BITS, idGenerator.getSequenceBits());

        int count = 100;
        for (int i = 0; i < count; i++) {

        }
    }
}