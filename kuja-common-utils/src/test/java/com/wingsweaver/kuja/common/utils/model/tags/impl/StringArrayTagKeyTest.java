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

class StringArrayTagKeyTest {
    @Test
    void test() {
        StringArrayTagKey tagKey = new StringArrayTagKey("test");
        assertEquals("test", tagKey.getKey());
        ParameterizedType valueType = (ParameterizedType) tagKey.getValueType();
        assertSame(List.class, valueType.getRawType());
        assertArrayEquals(new Type[]{String.class}, valueType.getActualTypeArguments());
        assertEquals("[String]", tagKey.getTypeCode());
        assertSame(String.class, tagKey.getItemType());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            StringArrayTagKey tagKey2 = new StringArrayTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            StringArrayTagKey tagKey3 = new StringArrayTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        assertIterableEquals(CollectionUtil.listOf("true"), tagKey.convertToValue(true));
        assertIterableEquals(CollectionUtil.listOf("1"), tagKey.convertToValue(new Object[]{1}));
        assertIterableEquals(CollectionUtil.listOf("1", "2"), tagKey.convertToValue("1,2"));
        assertIterableEquals(CollectionUtil.listOf("3", "4"), tagKey.convertToValue(CollectionUtil.listOf("3", 4)));

        assertEquals("tom,jerry", tagKey.saveAsText(CollectionUtil.listOf("tom", "jerry")));
        assertIterableEquals(CollectionUtil.listOf("tom", "jerry"), tagKey.convertToValue("tom,jerry"));
    }
}