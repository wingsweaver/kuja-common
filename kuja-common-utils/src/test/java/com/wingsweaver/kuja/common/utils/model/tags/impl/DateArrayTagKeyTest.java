package com.wingsweaver.kuja.common.utils.model.tags.impl;

import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("deprecation")
class DateArrayTagKeyTest {
    @Test
    void test() throws ParseException {
        DateArrayTagKey tagKey = new DateArrayTagKey("test");
        assertEquals("test", tagKey.getKey());
        ParameterizedType valueType = (ParameterizedType) tagKey.getValueType();
        assertSame(List.class, valueType.getRawType());
        assertArrayEquals(new Type[]{Date.class}, valueType.getActualTypeArguments());
        assertEquals("[Date]", tagKey.getTypeCode());
        assertSame(Date.class, tagKey.getItemType());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            DateArrayTagKey tagKey2 = new DateArrayTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            DateArrayTagKey tagKey3 = new DateArrayTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        Date date = DateFormatUtils.ISO_DATE_FORMAT.parse("2020-01-01");
        Date date2 = DateFormatUtils.ISO_DATE_FORMAT.parse("2023-12-19");
        assertIterableEquals(CollectionUtil.listOf(date), tagKey.convertToValue(date));
        assertIterableEquals(CollectionUtil.listOf(date, date2), tagKey.convertToValue(new Object[]{date, date2}));
        assertIterableEquals(CollectionUtil.listOf(date, date2), tagKey.convertToValue("2020-01-01," + date2.getTime()));
        assertIterableEquals(CollectionUtil.listOf(date, date2), tagKey.convertToValue(CollectionUtil.listOf(date.toInstant(), date2.getTime())));

        assertEquals(date.getTime() + "," + date2.getTime(),
                tagKey.saveAsText(CollectionUtil.listOf(date, date2)));
    }
}