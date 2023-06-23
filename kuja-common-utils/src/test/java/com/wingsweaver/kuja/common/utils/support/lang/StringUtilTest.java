package com.wingsweaver.kuja.common.utils.support.lang;

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
    }

    @Test
    void isNotEmpty() {
        assertFalse(StringUtil.isNotEmpty(null));
        assertFalse(StringUtil.isNotEmpty(""));
        assertTrue(StringUtil.isNotEmpty(" "));
    }

    @Test
    void trimToEmpty() {
        assertEquals("", StringUtil.trimToEmpty(null));
        assertEquals("", StringUtil.trimToEmpty(""));
        assertEquals("", StringUtil.trimToEmpty(" "));
        assertEquals("a", StringUtil.trimToEmpty(" a "));
    }

    @Test
    void trimToNull() {
        assertNull(StringUtil.trimToNull(null));
        assertNull(StringUtil.trimToNull(""));
        assertNull(StringUtil.trimToNull(" "));
        assertEquals("a", StringUtil.trimToNull(" a "));
    }

    @Test
    void notEmptyOrElse() {
        assertEquals("a", StringUtil.notEmptyOrElse(null, "a"));
        assertEquals("a", StringUtil.notEmptyOrElse("", () -> "a"));
        assertEquals(" ", StringUtil.notEmptyOrElse(" ", () -> "a"));
        assertEquals("a", StringUtil.notEmptyOrElse("a", () -> "b"));
        assertEquals("a", StringUtil.notEmptyOrElse("a", "b"));
    }

    @Test
    void fromSupplierOrDefault() {
        assertEquals("a", StringUtil.fromSupplierOrDefault(null, "a"));
        assertEquals("", StringUtil.fromSupplierOrDefault(() -> "", "a"));
    }

    @Test
    void fromSupplierOrEmpty() {
        assertEquals("", StringUtil.fromSupplierOrEmpty(null));
        assertEquals("", StringUtil.fromSupplierOrEmpty(() -> ""));
        assertEquals(" ", StringUtil.fromSupplierOrEmpty(() -> " "));
    }

    @Test
    void fromSupplierOrNull() {
        assertNull(StringUtil.fromSupplierOrNull(null));
        assertEquals("", StringUtil.fromSupplierOrNull(() -> ""));
        assertEquals(" ", StringUtil.fromSupplierOrNull(() -> " "));
    }

    @Test
    void compare() {
        assertEquals(0, StringUtil.compare(null, null, true));
        assertEquals(0, StringUtil.compare("", "", true));
        assertTrue(StringUtil.compare(null, "", true) < 0);
        assertTrue(StringUtil.compare("", null, true) > 0);
        assertEquals(0, StringUtil.compare("tom", "Tom", true));
        assertTrue(StringUtil.compare("tom", "Tom", false) > 0);
    }

    @Test
    void notNullOr() {
        assertEquals("a", StringUtil.notNullOr(null, "a"));
        assertEquals("", StringUtil.notNullOr("", "a"));
        assertEquals(" ", StringUtil.notNullOr(" ", "a"));
        assertEquals("a", StringUtil.notNullOr(null, () -> "a"));
        assertEquals("", StringUtil.notNullOr("", () -> "a"));
        assertEquals(" ", StringUtil.notNullOr(" ", () -> "a"));
    }

    @Test
    void notEmptyOr() {
        assertEquals("a", StringUtil.notEmptyOr(null, "a"));
        assertEquals("a", StringUtil.notEmptyOr("", "a"));
        assertEquals(" ", StringUtil.notEmptyOr(" ", "a"));
        assertEquals("a", StringUtil.notEmptyOr(null, () -> "a"));
        assertEquals("a", StringUtil.notEmptyOr("", () -> "a"));
        assertEquals(" ", StringUtil.notEmptyOr(" ", () -> "a"));
    }

    @Test
    void notBlankOr() {
        assertEquals("a", StringUtil.notBlankOr(null, "a"));
        assertEquals("a", StringUtil.notBlankOr("", "a"));
        assertEquals("a", StringUtil.notBlankOr(" ", "a"));
        assertEquals("a", StringUtil.notBlankOr("a", "b"));
        assertEquals("a", StringUtil.notBlankOr(null, () -> "a"));
        assertEquals("a", StringUtil.notBlankOr("", () -> "a"));
        assertEquals("a", StringUtil.notBlankOr(" ", () -> "a"));
        assertEquals("a", StringUtil.notBlankOr("a", () -> "b"));
    }
}