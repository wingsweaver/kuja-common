package com.wingsweaver.kuja.common.utils.support.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayUtilTest {
    @Test
    void test() {
        Object[] emptyArray = new Object[0];
        Object[] arrayWithNull = new Object[]{null};
        Object objectEmptyArray = emptyArray;
        Object objectArrayWithNull = arrayWithNull;

        assertTrue(ArrayUtil.isEmpty(null));
        assertTrue(ArrayUtil.isEmpty(emptyArray));
        assertFalse(ArrayUtil.isEmpty(arrayWithNull));

        assertFalse(ArrayUtil.isNotEmpty(null));
        assertFalse(ArrayUtil.isNotEmpty(emptyArray));
        assertTrue(ArrayUtil.isNotEmpty(arrayWithNull));

        assertTrue(ArrayUtil.isEmpty(objectEmptyArray));
        assertFalse(ArrayUtil.isEmpty(objectArrayWithNull));

        assertFalse(ArrayUtil.isNotEmpty(objectEmptyArray));
        assertTrue(ArrayUtil.isNotEmpty(objectArrayWithNull));
    }
}