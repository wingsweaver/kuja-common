package com.wingsweaver.kuja.common.utils.model.tags.impl;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EnumTagKeyTest {
    @Test
    void test() {
        EnumTagKey<DayOfWeek> tagKey = new EnumTagKey<>("test", DayOfWeek.class);
        assertEquals("test", tagKey.getKey());
        assertEquals(DayOfWeek.class, tagKey.getValueType());
        assertEquals("enum:" + DayOfWeek.class.getName(), tagKey.getTypeCode());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");
        assertNotEquals(tagKey, new EnumTagKey<>("test", Month.class));

        {
            EnumTagKey<DayOfWeek> tagKey2 = new EnumTagKey<>("test", DayOfWeek.class);
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            EnumTagKey<DayOfWeek> tagKey3 = new EnumTagKey<>("Test", DayOfWeek.class);
            assertNotEquals(tagKey, tagKey3);
        }

        assertEquals(DayOfWeek.MONDAY, tagKey.convertToValue(DayOfWeek.MONDAY));
        assertEquals(DayOfWeek.TUESDAY, tagKey.convertToValue("TUESDAY"));

        assertEquals("WEDNESDAY", tagKey.saveAsText(DayOfWeek.WEDNESDAY));
    }
}