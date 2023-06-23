package com.wingsweaver.kuja.common.utils.model.tags.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StringTagKeyTest {
    @Test
    void test() {
        StringTagKey tagKey = new StringTagKey("test");
        assertEquals("test", tagKey.getKey());
        assertEquals(String.class, tagKey.getValueType());
        assertEquals("String", tagKey.getTypeCode());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            StringTagKey tagKey2 = new StringTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            StringTagKey tagKey3 = new StringTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        assertEquals("1", tagKey.convertToValue(1));
        assertEquals("true", tagKey.convertToValue(true));
        assertEquals("tom", tagKey.convertToValue("tom"));
        assertEquals("[1, tom]", tagKey.convertToValue(new Object[]{1, "tom"}));

        assertEquals("jerry", tagKey.saveAsText("jerry"));
    }
}