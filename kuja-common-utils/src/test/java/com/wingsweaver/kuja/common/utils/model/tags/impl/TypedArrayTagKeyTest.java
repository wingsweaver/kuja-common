package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class TypedArrayTagKeyTest {
    @Test
    void test() {
        TypedArrayTagKey<DayOfWeek> tagKey = new TypedArrayTagKey<>("test", DayOfWeek.class);
        assertEquals("test", tagKey.getKey());
        ParameterizedType valueType = (ParameterizedType) tagKey.getValueType();
        assertSame(List.class, valueType.getRawType());
        assertArrayEquals(new Type[]{DayOfWeek.class}, valueType.getActualTypeArguments());
        assertEquals("[enum:" + DayOfWeek.class.getName() + "]", tagKey.getTypeCode());
        assertNotNull(tagKey.toString());
        assertEquals(DayOfWeek.class, tagKey.getItemType());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");
        assertNotEquals(tagKey, new TypedArrayTagKey<>("test", Month.class));

        {
            TypedArrayTagKey<DayOfWeek> tagKey2 = new TypedArrayTagKey<>("test", DayOfWeek.class);
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            TypedArrayTagKey<DayOfWeek> tagKey3 = new TypedArrayTagKey<>("Test", DayOfWeek.class);
            assertNotEquals(tagKey, tagKey3);
        }

        assertIterableEquals(CollectionUtil.listOf(DayOfWeek.MONDAY), tagKey.convertToValue(DayOfWeek.MONDAY));
        assertIterableEquals(CollectionUtil.listOf(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), tagKey.convertToValue("TUESDAY,WEDNESDAY"));
        assertIterableEquals(CollectionUtil.listOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), tagKey.convertToValue(new Object[]{DayOfWeek.THURSDAY, DayOfWeek.FRIDAY}));


        assertEquals("SATURDAY,SUNDAY", tagKey.saveAsText(CollectionUtil.listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)));
    }
}