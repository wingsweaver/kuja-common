package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedElement;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("deprecation")
class ToStringBuilderTest {
    @Test
    void test() throws ParseException {
        {
            assertEquals("1234", ToStringBuilder.toString(1234));
            assertEquals("ToM", ToStringBuilder.toString("ToM"));
            assertEquals("[]", ToStringBuilder.toString(new int[0]));
            assertEquals("[1, 2, 3, 4]", ToStringBuilder.toString(new int[]{1, 2, 3, 4}));
            assertEquals("{}", ToStringBuilder.toString(Collections.emptyMap()));
            Map<String, Object> map = MapUtil.from("id", 100, "name", "tom", "age", 10);
            assertEquals("{age: 10, id: 100, name: tom}", ToStringBuilder.toString(new TreeMap<>(map)));
            Date date = DateFormatUtils.ISO_DATE_FORMAT.parse("2020-10-11");
            assertEquals("2020-10-11", ToStringBuilder.toString(date));
        }
    }

    @Test
    void test2() {
        Person person = new Person();

        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        String result = ToStringBuilder.toString(person, config);
        System.out.println(result);
        assertTrue(result.contains("id"));
        assertFalse(result.contains("13579"));
        assertTrue(result.contains("name"));
        assertTrue(result.contains("tom"));
        assertFalse(result.contains("_phone"));
        assertFalse(result.contains("password"));
        assertTrue(result.contains("phone"));
        assertFalse(result.contains("13912345678"));
        assertTrue(result.contains("139****5678"));
        assertFalse(result.contains("ignored"));
        assertFalse(result.contains("valid"));
        assertFalse(result.contains("idHash"));
    }

    @Test
    void test3() {
        Person person = new Person();
        String result = ToStringBuilder.toString(person, configBuilder -> {
            configBuilder.setIncludeTransient(true).setPublicOnly(false);
        });
        System.out.println(result);
        assertTrue(result.contains("id"));
        assertFalse(result.contains("13579"));
        assertTrue(result.contains("name"));
        assertTrue(result.contains("tom"));
        assertTrue(result.contains("_phone"));
        assertTrue(result.contains("13912345678"));
        assertTrue(result.contains("password"));
        assertFalse(result.contains("topsecret"));
        assertTrue(result.contains("phone"));
        assertTrue(result.contains("139****5678"));
        assertFalse(result.contains("ignored"));
        assertTrue(result.contains("valid"));
        assertFalse(result.contains("idHash"));
    }


    @Test
    void test4() {
        YearMonth yearMonth = new YearMonth();
        yearMonth.setYear(2023);
        yearMonth.setMonth(10);
        String result = ToStringBuilder.toString(yearMonth, (Consumer<ToStringConfig.Builder>) null);
        assertEquals("2023-10", result);

        {
            StringBuilder sb = new StringBuilder();
            ToStringBuilder.toStringBuilder(yearMonth, sb);
            assertEquals("2023-10", sb.toString());
        }

        {
            StringBuilder sb2 = new StringBuilder();
            ToStringBuilder.toStringBuilder(yearMonth, (ToStringConfig) null, sb2);
            assertEquals("2023-10", sb2.toString());
        }
    }


    @Test
    void test5() {
        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        ToStringBuilder.setDefaultConfig(config);
        assertSame(config, ToStringBuilder.getDefaultConfig());
    }

    static class Person {
        @Masked
        public int id = 13579;

        public String name = "tom";

        protected String _phone = "13912345678";

        @Hashed
        public transient String password = "topsecret";

        @ToStringIgnored
        public Date ignored = new Date();

        @Masked(ranges = @Masked.Range(start = 3, end = -4))
        public String getPhone() {
            return _phone;
        }

        protected boolean isValid() {
            return true;
        }

        @ToStringIgnored
        public int getIdHash() {
            return Objects.hash(this.id);
        }
    }

    @Getter
    @Setter
    @ToStringWith(ToStringWithYearMonth.class)
    static class YearMonth {
        private int year;

        private int month;
    }

    public static class ToStringWithYearMonth implements ToStringWithConverter {
        @Override
        public boolean toString(Object value, AnnotatedElement annotatedElement, StringBuilder builder, ToStringConfig context) {
            if (!(value instanceof YearMonth)) {
                return false;
            }

            YearMonth yearMonth = (YearMonth) value;
            builder.append(yearMonth.getYear());
            builder.append("-");
            builder.append(yearMonth.getMonth());
            return true;
        }
    }
}