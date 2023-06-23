package com.wingsweaver.kuja.common.utils.model.tags.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DoubleTagKeyTest {
    @Test
    void test() {
        DoubleTagKey tagKey = new DoubleTagKey("test");
        assertEquals("test", tagKey.getKey());
        assertEquals(Double.class, tagKey.getValueType());
        assertEquals("Double", tagKey.getTypeCode());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            DoubleTagKey tagKey2 = new DoubleTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            DoubleTagKey tagKey3 = new DoubleTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        assertEquals(1.0, tagKey.convertToValue("1"));
        assertEquals(1.0, tagKey.convertToValue(1));
        assertEquals(1.0, tagKey.convertToValue(1.0));
        assertEquals(1.0, tagKey.convertToValue(1.0f));

        assertEquals("1.0", tagKey.saveAsText(1.0));
    }
}