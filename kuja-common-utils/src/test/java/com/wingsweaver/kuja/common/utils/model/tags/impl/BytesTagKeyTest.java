package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.support.codec.binary.BinaryCodec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BytesTagKeyTest {
    @Test
    void test() {
        BytesTagKey tagKey = new BytesTagKey("test");
        assertEquals("test", tagKey.getKey());
        assertEquals(byte[].class, tagKey.getValueType());
        assertEquals("byte[]", tagKey.getTypeCode());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            BytesTagKey tagKey2 = new BytesTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            BytesTagKey tagKey3 = new BytesTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        byte[] bytes = "hello, world".getBytes();
        assertArrayEquals(bytes, tagKey.convertToValue(bytes));
        assertArrayEquals(bytes, tagKey.convertToValue(BinaryCodec.encodeAsNamed(bytes)));

        assertEquals(BinaryCodec.encodeAsNamed(bytes), tagKey.saveAsText(bytes));
    }
}