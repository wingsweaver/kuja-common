package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultSequenceIdGeneratorTest {

    @Test
    void nextSequenceId() {
        DefaultSequenceIdGenerator sequenceIdGenerator = new DefaultSequenceIdGenerator(12);

        assertEquals(12, sequenceIdGenerator.bits());
        assertEquals(4095, sequenceIdGenerator.maxValue());

        long timestamp = System.currentTimeMillis();
        long lastSequenceId = -1;
        int count = 100;
        for (int i = 0; i < count; i++) {
            long sequenceId = sequenceIdGenerator.nextSequenceId(timestamp);
            assertTrue(sequenceId > lastSequenceId);
            lastSequenceId = sequenceId;
        }

        while (sequenceIdGenerator.nextSequenceId(timestamp) != SequenceIdGenerator.NO_MORE_SEQUENCE_ID) {
            // do nothing
        }
        assertNull(sequenceIdGenerator.nextSequenceId(timestamp));

        timestamp += 1;
        assertNotNull(sequenceIdGenerator.nextSequenceId(timestamp));
    }
}