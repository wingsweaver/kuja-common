package com.wingsweaver.kuja.common.utils.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ValueWrapperTest {
    @Test
    void test() {
        ValueWrapper<String> valueWrapper = new ValueWrapper<>();
        assertNull(valueWrapper.getValue());
        valueWrapper.setValue("test");
        assertEquals("test", valueWrapper.getValue());
    }

    @Test
    void test2() {
        ValueWrapper<String> valueWrapper = new ValueWrapper<>("test");
        assertEquals("test", valueWrapper.getValue());
    }
}