package com.wingsweaver.kuja.common.utils.model.tuple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Tuple2Test {
    @Test
    void test() {
        Tuple2<String, Integer> tuple2 = Tuple2.of("Hello", 1);
        assertEquals("Hello", tuple2.getT1());
        assertEquals(1, tuple2.getT2());
        assertEquals("Hello", tuple2.get(0));
        assertEquals(1, tuple2.get(1));
        assertEquals(2, tuple2.size());
        assertArrayEquals(new Object[]{"Hello", 1}, tuple2.toArray());
        assertThrows(IndexOutOfBoundsException.class, () -> tuple2.get(2));
    }

    @Test
    void test2() {
        Tuple2<String, Integer> t1 = Tuple2.of("Hello", 1234);
        Tuple2<String, Integer> t2 = Tuple2.of("Hello", 1234);
        assertEquals(t1, t1);
        assertEquals(t1, t2);
        assertNotEquals(null, t1);
        assertNotEquals("Hello", t1);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertEquals(t1.toString(), t2.toString());
    }
}