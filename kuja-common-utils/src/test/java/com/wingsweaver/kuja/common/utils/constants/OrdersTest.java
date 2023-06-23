package com.wingsweaver.kuja.common.utils.constants;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersTest {

    @Test
    void higher() {
        assertEquals(-1234, Orders.higher(0, 1234));
    }

    @Test
    void lower() {
        assertEquals(1234, Orders.lower(0, 1234));
    }

    @Test
    void compare() {
        assertEquals(0, Orders.compare(new TestOrdered(0), new TestOrdered(0)));
        assertTrue(Orders.compare(new TestOrdered(0), new TestOrdered(1)) < 0);
        assertTrue(Orders.compare(new TestOrdered(1), new TestOrdered(0)) > 0);
    }

    @Test
    void compareReversed() {
        assertEquals(0, Orders.compareReversed(new TestOrdered(0), new TestOrdered(0)));
        assertTrue(Orders.compareReversed(new TestOrdered(0), new TestOrdered(1)) > 0);
        assertTrue(Orders.compareReversed(new TestOrdered(1), new TestOrdered(0)) < 0);
    }

    @Getter
    static class TestOrdered implements Ordered {
        private final int order;

        TestOrdered(int order) {
            this.order = order;
        }
    }
}