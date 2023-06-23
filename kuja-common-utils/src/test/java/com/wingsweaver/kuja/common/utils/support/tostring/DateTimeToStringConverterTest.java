package com.wingsweaver.kuja.common.utils.support.tostring;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("deprecation")
class DateTimeToStringConverterTest {

    @Test
    void testToString() throws ParseException {
        DateTimeToStringConverter converter = DateTimeToStringConverter.INSTANCE;

        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        StringBuilder sb = new StringBuilder();
        assertFalse(converter.toString(null, sb, config));
        assertFalse(converter.toString("2020-10-11", sb, config));
        assertFalse(converter.toString(123456789, sb, config));

        // Date 类型
        Date date = DateFormatUtils.ISO_DATE_FORMAT.parse("2020-10-11");
        Date dateTime = DateFormatUtils.ISO_DATETIME_FORMAT.parse("2013-04-16T12:34:56");

        {
            sb.delete(0, sb.length());
            assertTrue(converter.toString(date, sb, config));
            assertEquals("2020-10-11", sb.toString());
        }
        {
            sb.delete(0, sb.length());
            assertTrue(converter.toString(dateTime, sb, config));
            assertEquals("2013-04-16T12:34:56+08:00", sb.toString());
        }

        // Calendar 类型
        {
            Calendar calandar = Calendar.getInstance();
            calandar.setTime(date);
            sb.delete(0, sb.length());
            assertTrue(converter.toString(calandar, sb, config));
            assertEquals("2020-10-11", sb.toString());
        }
        {
            Calendar calandar = Calendar.getInstance();
            calandar.setTime(dateTime);
            sb.delete(0, sb.length());
            assertTrue(converter.toString(calandar, sb, config));
            assertEquals("2013-04-16T12:34:56+08:00", sb.toString());
        }

        // Instant 类型
        {
            sb.delete(0, sb.length());
            assertTrue(converter.toString(date.toInstant(), sb, config));
            assertEquals("2020-10-11T00:00:00+0800", sb.toString());
        }
        {
            sb.delete(0, sb.length());
            assertTrue(converter.toString(dateTime.toInstant(), sb, config));
            assertEquals("2013-04-16T12:34:56+0800", sb.toString());
        }

        // ChronoLocalDate 类型
        {
            ChronoLocalDate chronoLocalDate = date.toInstant().atZone(ZoneOffset.systemDefault()).toLocalDate();
            sb.delete(0, sb.length());
            assertTrue(converter.toString(chronoLocalDate, sb, config));
            assertEquals("2020-10-11", sb.toString());
        }
        {
            ChronoLocalDate chronoLocalDate = dateTime.toInstant().atZone(ZoneOffset.systemDefault()).toLocalDate();
            sb.delete(0, sb.length());
            assertTrue(converter.toString(chronoLocalDate, sb, config));
            assertEquals("2013-04-16", sb.toString());
        }

        // LocalTime 类型
        {
            LocalTime localTime = date.toInstant().atZone(ZoneOffset.systemDefault()).toLocalTime();
            sb.delete(0, sb.length());
            assertTrue(converter.toString(localTime, sb, config));
            assertEquals("00:00:00", sb.toString());
        }
        {
            LocalTime localTime = dateTime.toInstant().atZone(ZoneOffset.systemDefault()).toLocalTime();
            sb.delete(0, sb.length());
            assertTrue(converter.toString(localTime, sb, config));
            assertEquals("12:34:56", sb.toString());
        }
    }
}