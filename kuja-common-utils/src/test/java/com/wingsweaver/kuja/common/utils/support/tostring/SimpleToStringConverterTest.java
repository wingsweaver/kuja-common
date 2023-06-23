package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SimpleToStringConverterTest {

    @Test
    void testToString() {
        SimpleToStringConverter converter = SimpleToStringConverter.INSTANCE;
        ToStringConfig context = ToStringBuilder.getDefaultConfig();

        {
            StringBuilder sb = new StringBuilder();
            assertFalse(converter.toString(null, sb, context));
            assertFalse(converter.toString(UUID.randomUUID(), sb, context));
            assertFalse(converter.toString(new Date(), sb, context));
        }

        {
            StringBuilder sb = new StringBuilder();
            converter.toString(1234, sb, context);
            assertEquals("1234", sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            converter.toString("ToM", sb, context);
            assertEquals("ToM", sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            converter.toString(new int[]{1, 2, 3, 4}, sb, context);
            assertEquals("[1, 2, 3, 4]", sb.toString());
        }
        {
            StringBuilder sb = new StringBuilder();
            converter.toString(new String[0], sb, context);
            assertEquals("[]", sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            converter.toString(CollectionUtil.listOf(1, 2, 3, 4), sb, context);
            assertEquals("[1, 2, 3, 4]", sb.toString());
        }
        {
            StringBuilder sb = new StringBuilder();
            converter.toString(Collections.emptyList(), sb, context);
            assertEquals("[]", sb.toString());
        }

        {
            Map<String, Object> map = MapUtil.from("id", 100, "name", "tom", "age", 10);
            StringBuilder sb = new StringBuilder();
            converter.toString(new TreeMap<>(map), sb, context);
            assertEquals("{age: 10, id: 100, name: tom}", sb.toString());
        }
        {
            StringBuilder sb = new StringBuilder();
            converter.toString(Collections.emptyMap(), sb, context);
            assertEquals("{}", sb.toString());
        }

    }
}