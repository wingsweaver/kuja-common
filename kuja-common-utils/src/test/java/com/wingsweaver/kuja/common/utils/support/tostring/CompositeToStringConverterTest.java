package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("deprecation")
class CompositeToStringConverterTest {
    @Test
    void test() throws ParseException {
        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        StringBuilder sb = new StringBuilder();

        Date date = DateFormatUtils.ISO_DATE_FORMAT.parse("2020-10-11");
        Date dateTime = DateFormatUtils.ISO_DATETIME_FORMAT.parse("2013-04-16T12:34:56");
        UUID uuid = UUID.randomUUID();

        CompositeToStringConverter converter = new CompositeToStringConverter();
        assertNull(converter.getConverters());
        assertEquals(0, converter.getOrder());
        converter.setOrder(1234);
        assertEquals(1234, converter.getOrder());
        assertFalse(converter.toString(date, sb, config));
        assertFalse(converter.toString(dateTime, sb, config));
        assertFalse(converter.toString(uuid, sb, config));

        converter.setConverters(CollectionUtil.listOf(DateTimeToStringConverter.INSTANCE));
        assertTrue(converter.toString(date, sb, config));
        assertTrue(converter.toString(dateTime, sb, config));
        assertFalse(converter.toString(uuid, sb, config));
    }
}