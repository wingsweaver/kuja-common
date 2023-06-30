package com.wingsweaver.kuja.common.utils.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StackTraceTest {
    @Test
    void test() {
        StackTrace stackTrace = new StackTrace();
        assertNull(stackTrace.getClassName());
        assertNull(stackTrace.getMethodName());
        assertNull(stackTrace.getFileName());
        assertEquals(0, stackTrace.getLineNumber());

        stackTrace.setClassName("className");
        stackTrace.setMethodName("methodName");
        stackTrace.setFileName("fileName");
        stackTrace.setLineNumber(1234);
        assertEquals("className", stackTrace.getClassName());
        assertEquals("methodName", stackTrace.getMethodName());
        assertEquals("fileName", stackTrace.getFileName());
        assertEquals(1234, stackTrace.getLineNumber());
    }

    @Test
    void test3() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[0];
        StackTrace stackTrace = new StackTrace(element);
        assertEquals(element.getClassName(), stackTrace.getClassName());
        assertEquals(element.getMethodName(), stackTrace.getMethodName());
        assertEquals(element.getFileName(), stackTrace.getFileName());
        assertEquals(element.getLineNumber(), stackTrace.getLineNumber());
    }
}