package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessContextAccessorTest {
    @Test
    void test() {
        BusinessContext businessContext = BusinessContext.create();
        BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
        assertSame(businessContext, accessor.getContext());

        assertNull(accessor.getContextType());
        assertFalse(accessor.isContextType(DayOfWeek.class));
        accessor.setContextType(DayOfWeek.MONDAY);
        assertEquals(DayOfWeek.MONDAY, accessor.getContextType());
        assertTrue(accessor.isContextType(DayOfWeek.class));
        assertTrue(accessor.isContextType(DayOfWeek.MONDAY));
        assertFalse(accessor.isContextType(DayOfWeek.TUESDAY));

        assertNull(accessor.getError());
        Exception error = new ExtendedRuntimeException("test-exception");
        accessor.setError(error);
        assertSame(error, accessor.getError());
        Exception error2 = new RuntimeException("test-exception2");
        accessor.setErrorIfAbsent(error2);
        assertSame(error, accessor.getError());

        assertNull(accessor.getHandler());
        Object handler = new Object();
        accessor.setHandler(handler);
        assertSame(handler, accessor.getHandler());
        Object handler2 = new Object();
        accessor.setHandlerIfAbsent(handler2);
        assertSame(handler, accessor.getHandler());
    }
}