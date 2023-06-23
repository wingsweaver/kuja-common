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

class BooleanArrayTagKeyTest {
    @Test
    void test() {
        BooleanArrayTagKey tagKey = new BooleanArrayTagKey("test");
        assertEquals("test", tagKey.getKey());
        ParameterizedType valueType = (ParameterizedType) tagKey.getValueType();
        assertSame(List.class, valueType.getRawType());
        assertArrayEquals(new Type[]{Boolean.class}, valueType.getActualTypeArguments());
        assertEquals("[Boolean]", tagKey.getTypeCode());
        assertSame(Boolean.class, tagKey.getItemType());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            BooleanArrayTagKey tagKey2 = new BooleanArrayTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            BooleanArrayTagKey tagKey3 = new BooleanArrayTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        assertIterableEquals(CollectionUtil.listOf(false), tagKey.convertToValue(false));
        assertIterableEquals(CollectionUtil.listOf(true), tagKey.convertToValue(new boolean[]{true}));
        assertIterableEquals(CollectionUtil.listOf(true, false, true), tagKey.convertToValue("true,false,true"));
        assertIterableEquals(CollectionUtil.listOf(false, true, false), tagKey.convertToValue(CollectionUtil.listOf(0, "true", false)));

        assertEquals("true,false,true", tagKey.saveAsText(CollectionUtil.listOf(true, false, true)));
    }
}