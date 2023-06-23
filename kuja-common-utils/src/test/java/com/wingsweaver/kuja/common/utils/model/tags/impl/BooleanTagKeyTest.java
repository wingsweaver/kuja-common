package com.wingsweaver.kuja.common.utils.model.tags.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BooleanTagKeyTest {
    @Test
    void test() {
        BooleanTagKey tagKey = new BooleanTagKey("test");
        assertEquals("test", tagKey.getKey());
        assertEquals(Boolean.class, tagKey.getValueType());
        assertEquals("Boolean", tagKey.getTypeCode());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            BooleanTagKey tagKey2 = new BooleanTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            BooleanTagKey tagKey3 = new BooleanTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        assertEquals(true, tagKey.convertToValue("true"));
        assertEquals(false, tagKey.convertToValue(false));
        assertEquals(true, tagKey.convertToValue(1));

        assertEquals("true", tagKey.saveAsText(true));
        assertEquals("true", tagKey.saveAsText(Boolean.TRUE));
    }
}