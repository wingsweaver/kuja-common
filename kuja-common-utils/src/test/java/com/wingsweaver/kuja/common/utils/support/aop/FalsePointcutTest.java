package com.wingsweaver.kuja.common.utils.support.aop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FalsePointcutTest {

    @Test
    void getClassFilter() {
        assertEquals(FalseClassFilter.INSTANCE, FalsePointcut.INSTANCE.getClassFilter());
    }

    @Test
    void getMethodMatcher() {
        assertEquals(FalseMethodMatcher.INSTANCE, FalsePointcut.INSTANCE.getMethodMatcher());
    }

    @Test
    void testToString() {
        assertNotNull(FalsePointcut.INSTANCE.toString());
    }
}