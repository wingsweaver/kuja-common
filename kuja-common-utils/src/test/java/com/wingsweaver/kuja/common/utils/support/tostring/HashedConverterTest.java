package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.support.codec.binary.BinaryCodec;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HashedConverterTest {
    @Test
    void testToStringWithConverter() throws NoSuchFieldException, IllegalAccessException {
        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        config.setIncludeTypeName(false);

        HashedConverter converter = new HashedConverter();
        HashedConverterTest.Tester tester = new HashedConverterTest.Tester();

        {
            Field field = HashedConverterTest.Tester.class.getDeclaredField("text");
            Object fieldValue = field.get(tester);
            StringBuilder sb = new StringBuilder();
            converter.toString(fieldValue, field, sb, config);
            String result = BinaryCodec.encodeAsNamed(DigestUtils.md5("hello, world!"), HashedConfig.DEFAULT_CODEC);
            assertEquals(result, sb.toString());
        }
    }


    @Test
    void testConfigurableToStringConverter() {
        String number = "13912345678";
        HashedConverter converter = new HashedConverter();
        ToStringConfig config = ToStringBuilder.getDefaultConfig();
        config.setIncludeTypeName(false);

        {
            HashedConfig customConfig = new HashedConfig();
            customConfig.setAlgorithm("sha256");
            customConfig.setCodec("hex");
            StringBuilder sb = new StringBuilder();
            assertTrue(converter.toString(number, sb, config, customConfig));
            String result = BinaryCodec.encodeAsNamed(DigestUtils.sha256(number), "hex");
            assertEquals(result, sb.toString());
        }
    }

    static class Tester {
        @Hashed
        String text = "hello, world!";
    }
}