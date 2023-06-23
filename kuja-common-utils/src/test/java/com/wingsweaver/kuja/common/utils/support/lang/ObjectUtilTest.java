package com.wingsweaver.kuja.common.utils.support.lang;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectUtilTest {

    @Test
    void cast() {
        Date date = new Date();
        assertEquals(date, ObjectUtil.cast(date, Date.class));
        assertNull(ObjectUtil.cast(date, String.class));
    }

    @Test
    void originalToString() {
        Date date = new Date();
        assertNotEquals(date.toString(), ObjectUtil.originalToString(date));
        assertTrue(ObjectUtil.originalToString(date).contains(date.getClass().getName()));
        assertNull(ObjectUtil.originalToString(null));
    }
}