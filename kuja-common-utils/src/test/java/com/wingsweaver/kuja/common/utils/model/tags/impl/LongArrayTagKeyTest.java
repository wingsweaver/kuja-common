package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class LongArrayTagKeyTest {
    @Test
    void test() {
        LongArrayTagKey tagKey = new LongArrayTagKey("test");
        assertEquals("test", tagKey.getKey());
        ParameterizedType valueType = (ParameterizedType) tagKey.getValueType();
        assertSame(List.class, valueType.getRawType());
        assertArrayEquals(new Type[]{Long.class}, valueType.getActualTypeArguments());
        assertEquals("[Long]", tagKey.getTypeCode());
        assertSame(Long.class, tagKey.getItemType());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            LongArrayTagKey tagKey2 = new LongArrayTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            LongArrayTagKey tagKey3 = new LongArrayTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        assertIterableEquals(CollectionUtil.listOf(1L), tagKey.convertToValue("1"));
        assertIterableEquals(CollectionUtil.listOf(1L), tagKey.convertToValue(new long[]{1}));
        assertIterableEquals(CollectionUtil.listOf(1L, 2L), tagKey.convertToValue("1,2"));
        assertIterableEquals(CollectionUtil.listOf(3L, 4L), tagKey.convertToValue(CollectionUtil.listOf("3", 4)));

        assertEquals("5,7", tagKey.saveAsText(CollectionUtil.listOf(5L, 7L)));
    }
}