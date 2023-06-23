package com.wingsweaver.kuja.common.utils.model.tuple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Tuple3Test {
    @Test
    void test() {
        Tuple3<String, Integer, Boolean> tuple3 = Tuple3.of("Hello", 1, true);
        assertEquals("Hello", tuple3.getT1());
        assertEquals(1, tuple3.getT2());
        assertEquals(true, tuple3.getT3());
        assertEquals("Hello", tuple3.get(0));
        assertEquals(1, tuple3.get(1));
        assertEquals(true, tuple3.get(2));
        assertEquals(3, tuple3.size());
        assertArrayEquals(new Object[]{"Hello", 1, true}, tuple3.toArray());
        assertThrows(IndexOutOfBoundsException.class, () -> tuple3.get(3));
    }
}