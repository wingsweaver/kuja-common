package com.wingsweaver.kuja.common.utils.model.tuple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Tuple4Test {
    @Test
    void test() {
        Tuple4<String, Integer, Boolean, Double> tuple4 = Tuple4.of("Hello", 1, true, 1.0);
        assertEquals("Hello", tuple4.getT1());
        assertEquals(1, tuple4.getT2());
        assertEquals(true, tuple4.getT3());
        assertEquals(1.0, tuple4.getT4());
        assertEquals("Hello", tuple4.get(0));
        assertEquals(1, tuple4.get(1));
        assertEquals(true, tuple4.get(2));
        assertEquals(1.0, tuple4.get(3));
        assertEquals(4, tuple4.size());
        assertArrayEquals(new Object[]{"Hello", 1, true, 1.0}, tuple4.toArray());
        assertThrows(IndexOutOfBoundsException.class, () -> tuple4.get(4));
    }
}