package com.wingsweaver.kuja.common.utils.model.tags.impl;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TypedTagKeyTest {
    @Test
    void test() {
        TypedTagKey<DayOfWeek> tagKey = new TypedTagKey<>("test", DayOfWeek.class);
        assertEquals("test", tagKey.getKey());
        assertEquals(DayOfWeek.class, tagKey.getValueType());
        assertEquals("enum:" + DayOfWeek.class.getName(), tagKey.getTypeCode());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");
        assertEquals(tagKey, new EnumTagKey<>("test", DayOfWeek.class));

        {
            TypedTagKey<DayOfWeek> tagKey2 = new TypedTagKey<>("test", DayOfWeek.class);
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            TypedTagKey<DayOfWeek> tagKey3 = new TypedTagKey<>("Test", DayOfWeek.class);
            assertNotEquals(tagKey, tagKey3);
        }

        assertEquals(DayOfWeek.MONDAY, tagKey.convertToValue(DayOfWeek.MONDAY));
        assertEquals(DayOfWeek.TUESDAY, tagKey.convertToValue("TUESDAY"));

        assertEquals("WEDNESDAY", tagKey.saveAsText(DayOfWeek.WEDNESDAY));
    }
}