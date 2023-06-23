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

class DoubleArrayTagKeyTest {
    @Test
    void test() {
        DoubleArrayTagKey tagKey = new DoubleArrayTagKey("test");
        assertEquals("test", tagKey.getKey());
        ParameterizedType valueType = (ParameterizedType) tagKey.getValueType();
        assertSame(List.class, valueType.getRawType());
        assertArrayEquals(new Type[]{Double.class}, valueType.getActualTypeArguments());
        assertEquals("[Double]", tagKey.getTypeCode());
        assertSame(Double.class, tagKey.getItemType());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            DoubleArrayTagKey tagKey2 = new DoubleArrayTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            DoubleArrayTagKey tagKey3 = new DoubleArrayTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        assertIterableEquals(CollectionUtil.listOf(1.0), tagKey.convertToValue(1.0));
        assertIterableEquals(CollectionUtil.listOf(1.0), tagKey.convertToValue(new double[]{1.0}));
        assertIterableEquals(CollectionUtil.listOf(1.0, 2.0), tagKey.convertToValue("1,2.0"));
        assertIterableEquals(CollectionUtil.listOf(3.0, 4.0), tagKey.convertToValue(CollectionUtil.listOf(3.0, "4")));

        assertEquals("1.0,3.0", tagKey.saveAsText(CollectionUtil.listOf(1.0, 3.0)));
    }
}