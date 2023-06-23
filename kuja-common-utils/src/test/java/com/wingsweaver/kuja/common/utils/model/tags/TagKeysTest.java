package com.wingsweaver.kuja.common.utils.model.tags;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TagKeysTest {

    @Test
    void ofString() {
        assertNotNull(TagKeys.ofString("test"));
    }

    @Test
    void ofBoolean() {
        assertNotNull(TagKeys.ofBoolean("test"));
    }

    @Test
    void ofLong() {
        assertNotNull(TagKeys.ofLong("test"));
    }

    @Test
    void ofDouble() {
        assertNotNull(TagKeys.ofDouble("test"));
    }

    @Test
    void ofEnum() {
        assertNotNull(TagKeys.ofEnum("test", DayOfWeek.class));
    }

    @Test
    void ofBytes() {
        assertNotNull(TagKeys.ofBytes("test"));
    }

    @Test
    void ofDate() {
        assertNotNull(TagKeys.ofDate("test"));
    }

    @Test
    void ofType() {
        assertNotNull(TagKeys.ofType("test", UUID.class));
    }

    @Test
    void ofStringArray() {
        assertNotNull(TagKeys.ofStringArray("test"));
    }

    @Test
    void ofBooleanArray() {
        assertNotNull(TagKeys.ofBooleanArray("test"));
    }

    @Test
    void ofLongArray() {
        assertNotNull(TagKeys.ofLongArray("test"));
    }

    @Test
    void ofDoubleArray() {
        assertNotNull(TagKeys.ofDoubleArray("test"));
    }

    @Test
    void ofEnumArray() {
        assertNotNull(TagKeys.ofEnumArray("test", DayOfWeek.class));
    }

    @Test
    void ofTypedArray() {
        assertNotNull(TagKeys.ofTypedArray("test", Calendar.class));
    }
}