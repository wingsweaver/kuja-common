package com.wingsweaver.kuja.common.utils.support.aop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FalseMethodMatcherTest {

    @Test
    void matches() {
        assertFalse(FalseMethodMatcher.INSTANCE.matches(null, null));
        assertFalse(FalseMethodMatcher.INSTANCE.matches(null, null, (Object) null));
    }

    @Test
    void isRuntime() {
        assertFalse(FalseMethodMatcher.INSTANCE.isRuntime());
    }

    @Test
    void readResolve() {
        assertEquals(FalseMethodMatcher.INSTANCE, FalseMethodMatcher.INSTANCE.readResolve());
    }

    @Test
    void testToString() {
        assertNotNull(FalseMethodMatcher.INSTANCE.toString());
    }
}