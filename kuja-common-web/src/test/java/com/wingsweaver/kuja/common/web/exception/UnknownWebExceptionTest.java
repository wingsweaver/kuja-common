package com.wingsweaver.kuja.common.web.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class UnknownWebExceptionTest {
    @Test
    void test() {
        {
            UnknownWebException error = new UnknownWebException();
            assertNotNull(error);
        }

        {
            UnknownWebException error = new UnknownWebException("some-web-error");
            assertEquals("some-web-error", error.getMessage());
        }

        {
            Exception cause = new Exception("some-error");
            UnknownWebException error = new UnknownWebException(cause);
            assertNotNull(error);
            assertSame(cause, error.getCause());
        }

        {
            Exception cause = new Exception("some-error");
            UnknownWebException error = new UnknownWebException("some-web-error", cause);
            assertNotNull(error);
            assertSame(cause, error.getCause());
            assertEquals("some-web-error", error.getMessage());
        }
    }
}