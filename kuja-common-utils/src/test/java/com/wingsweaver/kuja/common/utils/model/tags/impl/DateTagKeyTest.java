package com.wingsweaver.kuja.common.utils.model.tags.impl;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("deprecation")
class DateTagKeyTest {
    @Test
    void test() throws ParseException {
        DateTagKey tagKey = new DateTagKey("test");
        assertEquals("test", tagKey.getKey());
        assertEquals(Date.class, tagKey.getValueType());
        assertEquals("Date", tagKey.getTypeCode());
        assertNotNull(tagKey.toString());
        assertEquals(tagKey, tagKey);
        assertNotEquals(tagKey, "test");

        {
            DateTagKey tagKey2 = new DateTagKey("test");
            assertEquals(tagKey, tagKey2);
            assertEquals(tagKey.hashCode(), tagKey2.hashCode());
            assertEquals(tagKey.toString(), tagKey2.toString());
            assertEquals(tagKey2.getTypeCode(), tagKey.getTypeCode());

            DateTagKey tagKey3 = new DateTagKey("Test");
            assertNotEquals(tagKey, tagKey3);
        }

        {
            Date date = DateFormatUtils.ISO_DATE_FORMAT.parse("2020-01-01");
            assertEquals(date, tagKey.convertToValue(date.toInstant()));
            assertEquals(date, tagKey.convertToValue(date.getTime()));
            assertEquals(date, tagKey.convertToValue(new Date(date.getTime())));
        }

        {
            Date date = DateFormatUtils.ISO_DATE_FORMAT.parse("2020-01-01");
            assertEquals(Long.toString(date.getTime()), tagKey.saveAsText(date));
        }
    }
}