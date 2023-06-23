package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaskedConverterTest {
    @Test
    void testToStringWithConverter() throws NoSuchFieldException, IllegalAccessException {
        MaskedConverter converter = new MaskedConverter();
        ToStringConfig config = ToStringBuilder.getDefaultConfig().mutable()
                .setIncludeTypeName(false).build();
        Tester tester = new Tester();

        {
            Field field = Tester.class.getDeclaredField("text");
            Object fieldValue = field.get(tester);
            StringBuilder sb = new StringBuilder();
            converter.toString(fieldValue, field, sb, config);
            assertEquals(StringUtils.repeat(MaskedConfig.DEFAULT_MASK_CHAR, 10), sb.toString());
        }

        {
            Field field = Tester.class.getDeclaredField("number");
            Object fieldValue = field.get(tester);
            StringBuilder sb = new StringBuilder();
            converter.toString(fieldValue, field, sb, config);
            assertEquals(StringUtils.repeat('?', 3), sb.toString());
        }

        {
            Field field = Tester.class.getDeclaredField("text2");
            Object fieldValue = field.get(tester);
            StringBuilder sb = new StringBuilder();
            converter.toString(fieldValue, field, sb, config);
            assertEquals("134******5678", sb.toString());
        }

        {
            Field field = Tester.class.getDeclaredField("name");
            Object fieldValue = field.get(tester);
            StringBuilder sb = new StringBuilder();
            converter.toString(fieldValue, field, sb, config);
            assertEquals("张*丰", sb.toString());
        }
    }

    @Test
    void testConfigurableToStringConverter() {
        String number = "13912345678";
        MaskedConverter converter = new MaskedConverter();
        ToStringConfig config = ToStringBuilder.getDefaultConfig().mutable()
                .setIncludeTypeName(false).build();

        {
            StringBuilder sb = new StringBuilder();
            MaskedConfig customConfig = new MaskedConfig();
            converter.toString(number, sb, config, customConfig);
            assertEquals(StringUtils.repeat(MaskedConfig.DEFAULT_MASK_CHAR, number.length()), sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            MaskedConfig customConfig = new MaskedConfig();
            customConfig.setMaskChar('#');
            customConfig.setMaxLength(-1);
            converter.toString(number, sb, config, customConfig);
            assertEquals(StringUtils.repeat('#', 10), sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            MaskedConfig customConfig = new MaskedConfig();
            customConfig.setMaskChar('?');
            customConfig.setMaxLength(10);
            customConfig.setRanges(CollectionUtil.listOf(new MaskedConfig.Range(2, 6)));
            converter.toString(number, sb, config, customConfig);
            assertEquals("13????4567", sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            MaskedConfig customConfig = new MaskedConfig();
            customConfig.setRanges(CollectionUtil.listOf(new MaskedConfig.Range(3, -3)));
            converter.toString(number, sb, config, customConfig);
            assertEquals("139*****678", sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            MaskedConfig customConfig = new MaskedConfig();
            customConfig.setRanges(CollectionUtil.listOf(
                    // 屏蔽最后两位
                    new MaskedConfig.Range(-2, 0),
                    // 屏蔽第 2 位到第 4 位
                    new MaskedConfig.Range(-10, 4)));
            converter.toString(number, sb, config, customConfig);
            assertEquals("1***23456**", sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            MaskedConfig customConfig = new MaskedConfig();
            converter.toString(null, sb, config, customConfig);
            assertEquals("null", sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            MaskedConfig customConfig = new MaskedConfig();
            converter.toString("", sb, config, customConfig);
            assertEquals("", sb.toString());
        }
    }

    static class Tester {
        @Masked()
        String text = "1234567890";

        @Masked(value = '?', maxLength = 3)
        int number = 12345;

        @Masked(ranges = {@Masked.Range(start = 3, end = -4)})
        String text2 = "134-1234-5678";

        @Masked(ranges = {@Masked.Range(start = 1, end = -1)})
        String name = "张三丰";
    }
}