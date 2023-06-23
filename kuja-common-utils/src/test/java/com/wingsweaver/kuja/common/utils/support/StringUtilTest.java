package com.wingsweaver.kuja.common.utils.support;

import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilTest {

    @Test
    void isEmpty() {
        assertTrue(StringUtil.isEmpty(null));
        assertTrue(StringUtil.isEmpty(""));
        assertFalse(StringUtil.isEmpty(" "));
        assertFalse(StringUtil.isEmpty("Hello, world!"));
    }

    @Test
    void isNotEmpty() {
        assertFalse(StringUtil.isNotEmpty(null));
        assertFalse(StringUtil.isNotEmpty(""));
        assertTrue(StringUtil.isNotEmpty(" "));
        assertTrue(StringUtil.isNotEmpty("Hello, world!"));
    }

    @Test
    void trimToEmpty() {
        assertEquals("", StringUtil.trimToEmpty(null));
        assertEquals("", StringUtil.trimToEmpty(""));
        assertEquals("", StringUtil.trimToEmpty(" "));
        assertEquals("Hello, world!", StringUtil.trimToEmpty("Hello, world!"));
        assertEquals("Hello, world!", StringUtil.trimToEmpty(" Hello, world! "));
    }

    @Test
    void trimToNull() {
        assertNull(StringUtil.trimToNull(null));
        assertNull(StringUtil.trimToNull(""));
        assertNull(StringUtil.trimToNull(" "));
        assertEquals("Hello, world!", StringUtil.trimToNull("Hello, world!"));
        assertEquals("Hello, world!", StringUtil.trimToNull(" Hello, world! "));
    }

    @Test
    void notEmptyOrElse() {
        assertEquals("tom", StringUtil.notEmptyOrElse(null, "tom"));
        assertEquals("tom", StringUtil.notEmptyOrElse("", "tom"));
        assertEquals(" ", StringUtil.notEmptyOrElse(" ", "tom"));
        assertEquals("tom", StringUtil.notEmptyOrElse("tom", "jerry"));
    }

    @Test
    void notEmptyOrElse2() {
        assertEquals("tom", StringUtil.notEmptyOrElse(null, () -> "tom"));
        assertEquals("tom", StringUtil.notEmptyOrElse("", () -> "tom"));
        assertEquals(" ", StringUtil.notEmptyOrElse(" ", () -> "tom"));
        assertEquals("tom", StringUtil.notEmptyOrElse("tom", () -> "jerry"));
    }

    @Test
    void fromSupplierOrDefault() {
        assertEquals("tom", StringUtil.fromSupplierOrDefault(null, "tom"));
        assertNull(StringUtil.fromSupplierOrDefault(() -> null, "tom"));
        assertEquals("jerry", StringUtil.fromSupplierOrDefault(() -> "jerry", "tom"));
    }

    @Test
    void fromSupplierOrEmpty() {
        assertEquals("", StringUtil.fromSupplierOrEmpty(null));
        assertNull(StringUtil.fromSupplierOrEmpty(() -> null));
        assertEquals("jerry", StringUtil.fromSupplierOrEmpty(() -> "jerry"));
    }

    @Test
    void fromSupplierOrNull() {
        assertNull(StringUtil.fromSupplierOrNull(null));
        assertNull(StringUtil.fromSupplierOrNull(() -> null));
        assertEquals("jerry", StringUtil.fromSupplierOrNull(() -> "jerry"));
    }

    @Test
    void compare() {
        assertEquals(0, StringUtil.compare(null, null, true));
        assertEquals(0, StringUtil.compare(null, null, false));
        assertTrue(StringUtil.compare(null, "", true) < 0);
        assertTrue(StringUtil.compare("", null, false) > 0);
        assertEquals(0, StringUtil.compare("", "", true));
        assertEquals(0, StringUtil.compare("", "", false));
        assertEquals(0, StringUtil.compare(" ", " ", true));
        assertEquals(0, StringUtil.compare(" ", " ", false));

        assertEquals(0, StringUtil.compare("Hello, world!", "HELLO, world!", true));
        assertTrue(StringUtil.compare("Hello, world!", "HELLO, world!", false) > 0);
    }
}