package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.Valued;
import com.wingsweaver.kuja.common.utils.support.codec.binary.BinaryCodec;
import com.wingsweaver.kuja.common.utils.support.lang.reflect.ParameterizedTypeImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultTagValueConverterTest {
    @Test
    void test() throws ParseException {
        DefaultTagValueConverter converter = new DefaultTagValueConverter();
        assertFalse(converter.convertTo(null, new ParameterizedTypeImpl(List.class, new Type[]{String.class})).getT1());
        assertEquals(Tuple2.of(true, null), converter.convertTo(null, String.class));
        assertEquals(Tuple2.of(false, null), converter.convertTo(0, UUID.class));

        assertEquals(Tuple2.of(true, "1234"), converter.convertTo("1234", String.class));

        assertEquals(Tuple2.of(true, DayOfWeek.MONDAY), converter.convertTo("monday", DayOfWeek.class));
        assertEquals(Tuple2.of(true, DayOfWeek.MONDAY), converter.convertTo("MONDAY", DayOfWeek.class));
        assertEquals(Tuple2.of(true, DayOfWeek.MONDAY), converter.convertTo(DayOfWeek.MONDAY, DayOfWeek.class));

        assertEquals(Tuple2.of(true, Seasons.SPRING), converter.convertTo("spring", Seasons.class));
        assertEquals(Tuple2.of(true, Seasons.SUMMER), converter.convertTo(2, Seasons.class));
        assertEquals(Tuple2.of(true, Seasons.AUTUMN), converter.convertTo("3", Seasons.class));
        assertEquals(Tuple2.of(true, null), converter.convertTo("fall", Seasons.class));

        assertEquals(Tuple2.of(true, "1234"), converter.convertTo(1234, String.class));
        assertEquals(Tuple2.of(true, "1234"), converter.convertTo("1234", String.class));

        assertEquals(Tuple2.of(true, true), converter.convertTo(true, Boolean.class));
        assertEquals(Tuple2.of(true, false), converter.convertTo(Boolean.FALSE, Boolean.class));
        assertEquals(Tuple2.of(true, false), converter.convertTo("false", Boolean.class));
        assertEquals(Tuple2.of(true, true), converter.convertTo("TRUE", Boolean.class));
        assertEquals(Tuple2.of(true, true), converter.convertTo(1, Boolean.class));
        assertEquals(Tuple2.of(true, false), converter.convertTo(0, Boolean.class));
        assertEquals(Tuple2.of(true, true), converter.convertTo(-1, Boolean.class));
        assertEquals(Tuple2.of(false, null), converter.convertTo("yes", Boolean.class));
        assertEquals(Tuple2.of(false, null), converter.convertTo("", Boolean.class));
        assertEquals(Tuple2.of(false, null), converter.convertTo(new Object(), Boolean.class));

        assertEquals(Tuple2.of(true, 1234L), converter.convertTo(1234, Long.class));
        assertEquals(Tuple2.of(true, 1234L), converter.convertTo(1234L, Long.class));
        assertEquals(Tuple2.of(true, 1234L), converter.convertTo("1234", Long.class));
        assertEquals(Tuple2.of(false, null), converter.convertTo(new Object(), Long.class));

        {
            Tuple2<Boolean, Object> result = converter.convertTo(1234, Double.class);
            assertTrue(result.getT1());
            assertTrue(equals(1234, (Double) result.getT2()));
        }
        {
            Tuple2<Boolean, Object> result = converter.convertTo(1234.5678, Double.class);
            assertTrue(result.getT1());
            assertTrue(equals(1234.5678, (Double) result.getT2()));
        }
        {
            Tuple2<Boolean, Object> result = converter.convertTo("1234.5678", Double.class);
            assertTrue(result.getT1());
            assertTrue(equals(1234.5678, (Double) result.getT2()));
        }
        assertEquals(Tuple2.of(false, null), converter.convertTo(new Object(), Double.class));

        {
            String plainText = "hello, " + UUID.randomUUID();
            byte[] plainBytes = plainText.getBytes();
            String encoded = BinaryCodec.encodeAsNamed(plainBytes);
            Tuple2<Boolean, Object> result = converter.convertTo(encoded, byte[].class);
            assertTrue(result.getT1());
            assertArrayEquals(plainBytes, (byte[]) result.getT2());
        }
        assertEquals(Tuple2.of(false, null), converter.convertTo(new Object(), byte[].class));

        {
            Date date = DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.parse("2012-03-04");
            assertEquals(Tuple2.of(true, date), converter.convertTo(date.getTime(), Date.class));
            assertEquals(Tuple2.of(true, date), converter.convertTo(date.toInstant(), Date.class));
            assertEquals(Tuple2.of(true, date), converter.convertTo(Long.toString(date.getTime()), Date.class));
            String text = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(date);
            assertEquals(Tuple2.of(true, date), converter.convertTo(text, Date.class));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            assertEquals(Tuple2.of(true, date), converter.convertTo(calendar, Date.class));
        }
        assertThrows(TagConversionException.class, () -> converter.convertTo("hello", Date.class));
        assertEquals(Tuple2.of(false, null), converter.convertTo(new Object(), Date.class));
    }

    static boolean equals(double v1, double v2) {
        return Math.abs(v1 - v2) < 0.001;
    }

    enum Seasons implements Valued<Integer> {
        SPRING(1),
        SUMMER(2),
        AUTUMN(3),
        WINTER(4);

        private final int value;

        Seasons(int value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }
    }
}