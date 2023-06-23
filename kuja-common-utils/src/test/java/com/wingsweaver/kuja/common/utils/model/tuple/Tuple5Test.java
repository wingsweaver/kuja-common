package com.wingsweaver.kuja.common.utils.model.tuple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Tuple5Test {
    @Test
    void test() {
        Tuple5<String, Integer, Boolean, Double, Float> tuple5 = Tuple5.of("Hello", 1, true, 1.0, 1.0f);
        assertEquals("Hello", tuple5.getT1());
        assertEquals(1, tuple5.getT2());
        assertEquals(true, tuple5.getT3());
        assertEquals(1.0, tuple5.getT4());
        assertEquals(1.0f, tuple5.getT5());
        assertEquals("Hello", tuple5.get(0));
        assertEquals(1, tuple5.get(1));
        assertEquals(true, tuple5.get(2));
        assertEquals(1.0, tuple5.get(3));
        assertEquals(1.0f, tuple5.get(4));
        assertEquals(5, tuple5.size());
        assertArrayEquals(new Object[]{"Hello", 1, true, 1.0, 1.0f}, tuple5.toArray());
        assertThrows(IndexOutOfBoundsException.class, () -> tuple5.get(5));
    }
}