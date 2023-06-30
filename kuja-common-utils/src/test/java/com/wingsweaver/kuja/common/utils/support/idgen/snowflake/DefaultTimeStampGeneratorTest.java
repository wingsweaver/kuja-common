package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultTimeStampGeneratorTest {

    @Test
    void test() {
        DefaultTimeStampGenerator timeStampGenerator = new DefaultTimeStampGenerator(41);
        timeStampGenerator.setStartEpoch(0);
        timeStampGenerator.setUnit(TimeStampUnit.SECONDS);

        assertEquals(41, timeStampGenerator.bits());
        assertEquals(2199023255551L, timeStampGenerator.maxValue());
        assertEquals(0, timeStampGenerator.getStartEpoch());
        assertEquals(TimeStampUnit.SECONDS, timeStampGenerator.getUnit());

        long now = System.currentTimeMillis();
        System.out.println("now: " + now);
        long timestamp = timeStampGenerator.toTimeStamp(now);
        System.out.println("timestamp: " + timestamp);
        long epochUtc = timeStampGenerator.toEpoch(timestamp);
        System.out.println("epochUtc: " + epochUtc);
        assertEquals(now / 1000 * 1000, epochUtc);

        assertThrows(IllegalArgumentException.class, () -> timeStampGenerator.toTimeStamp(Long.MAX_VALUE));
    }

    @Test
    void test2() throws ParseException {
        DefaultTimeStampGenerator timeStampGenerator = new DefaultTimeStampGenerator(41);
        timeStampGenerator.setStartEpoch(0);
        timeStampGenerator.setUnit(TimeStampUnit.MILLISECONDS);

        Date start = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.parse("2020-01-01T00:00:00+08:00");
        timeStampGenerator.setStartEpoch(start.getTime());

        Date now = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.parse("2023-12-31T23:59:59+08:00");
        long timestamp = timeStampGenerator.toTimeStamp(now.getTime());

        long epochUtc = timeStampGenerator.toEpoch(timestamp);
        assertEquals(now.getTime(), epochUtc);
    }
}