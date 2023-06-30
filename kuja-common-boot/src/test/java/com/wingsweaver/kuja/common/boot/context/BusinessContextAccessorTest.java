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
        BusinessContext businessContext = new MapBusinessContext();
        BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
        assertSame(businessContext, accessor.getContext());

        BusinessContextTypeSetter setter = (BusinessContextTypeSetter) businessContext;
        assertNull(businessContext.getContextType());
        assertFalse(businessContext.isContextType(DayOfWeek.class));
        setter.setContextType(TestBusinessContextType.TOM);
        assertEquals(TestBusinessContextType.TOM, businessContext.getContextType());
        assertTrue(businessContext.isContextType(TestBusinessContextType.class));
        assertTrue(businessContext.isContextType(TestBusinessContextType.TOM));
        assertFalse(businessContext.isContextType(TestBusinessContextType.JERRY));

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

    enum TestBusinessContextType implements BusinessContextType {
        TOM,

        JERRY
    }
}