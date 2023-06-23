package com.wingsweaver.kuja.common.utils.support.aop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FalseClassFilterTest {

    @Test
    void matches() {
        // 补全 FalseClassFilter#matches() 的测试用例
        assertFalse(FalseClassFilter.INSTANCE.matches(Object.class));
        assertFalse(FalseClassFilter.INSTANCE.matches(FalseClassFilterTest.class));
    }

    @Test
    void readResolve() {
        assertEquals(FalseClassFilter.INSTANCE, FalseClassFilter.INSTANCE.readResolve());
    }

    @Test
    void testToString() {
        assertNotNull(FalseClassFilter.INSTANCE.toString());
    }
}