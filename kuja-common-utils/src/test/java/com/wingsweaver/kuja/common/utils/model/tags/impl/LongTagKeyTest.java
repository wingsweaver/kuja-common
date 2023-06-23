package com.wingsweaver.kuja.common.utils.model.tags.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LongTagKeyTest {
    @Test
    void test() {
        LongTagKey tagKey = new LongTagKey("test");
        assertEquals("test", tagKey.getKey());
        assertEquals(Long.class, tagKey.getValueType());
        assertEquals("Long", tagKey.getTypeCode());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            LongTagKey tagKey2 = new LongTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            LongTagKey tagKey3 = new LongTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        assertEquals(1L, tagKey.convertToValue("1"));
        assertEquals(1L, tagKey.convertToValue(1));
        assertEquals(1L, tagKey.convertToValue(1L));
        assertEquals(1L, tagKey.convertToValue(1.0));
        assertEquals(1L, tagKey.convertToValue(1.0f));

        assertEquals("1", tagKey.saveAsText(1L));
    }
}