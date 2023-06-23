package com.wingsweaver.kuja.common.utils.support;

import com.wingsweaver.kuja.common.utils.support.lang.StringFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StringFormatterTest {

    @Test
    void format() {
        assertNull(StringFormatter.format(null));
        assertEquals("", StringFormatter.format(""));
        assertEquals("Hello, world!", StringFormatter.format("Hello, world!"));

        assertEquals("tom & jerry", StringFormatter.format("%s & %s", "tom", "jerry"));
        assertEquals("tom & jerry", StringFormatter.format("{0} & {1}", "tom", "jerry"));
        assertEquals("jerry & tom", StringFormatter.format("{1} & {0}", "tom", "jerry"));
        assertEquals("tom & jerry", StringFormatter.format("{} & {}", "tom", "jerry"));
    }
}