package com.wingsweaver.kuja.common.utils.exception;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExtendedExceptionTest {
    PrintStream printStream = System.out;

    PrintWriter printWriter = new PrintWriter(System.out);

    @Test
    void test() {
        ExtendedException exception = new ExtendedException();
        assertNotNull(exception.extendedMap());
        assertFalse(exception.extendedMap().isEmpty());
        assertFalse(exception.extendedKeys().isEmpty());
        exception.printStackTrace(printStream);

        exception.removeExtendedAttribute("id");
        assertNull(exception.getExtendedAttribute("id"));

        assertNull(exception.getExtendedAttribute("thread"));
        assertEquals(1234, exception.getExtendedAttribute("id", 1234));
        exception.printStackTrace(printWriter);

        exception.withExtendedAttribute("id", 9876);
        assertEquals(9876, exception.getExtendedAttribute("id", 1234));
        exception.printStackTrace(printStream);

        exception.withExtendedAttribute(MapUtil.from("message", "this is a dummy message",
                "id", 24680));
        assertEquals("this is a dummy message", exception.getExtendedAttribute("message"));
        assertEquals("this is a dummy message", exception.getMessage());
        assertEquals(24680, exception.getExtendedAttribute("id", 1234));
        exception.printStackTrace(printWriter);

        exception.forEachExtended((key, value) -> printStream.println(key + " = " + value));

        exception.removeExtendedAttribute("id");
        assertNull(exception.getExtendedAttribute("id"));

        exception.setExtendedAttributeIfAbsent("message", "this is a dummy message (version 2)");
        assertEquals("this is a dummy message", exception.getMessage());
        exception.setExtendedAttributeIfAbsent("id", 97531);
        assertEquals(97531, exception.getExtendedAttribute("id", 1234));

        exception.setExtendedAttribute("id", null);
        assertNull(exception.getExtendedAttribute("id"));

        exception.setExtendedAttributeIfAbsent("message", null);
        assertEquals("this is a dummy message", exception.getMessage());
    }

    @Test
    void test2() {
        {
            String message = UUID.randomUUID().toString();
            ExtendedException ex = new ExtendedException(message);
            assertEquals(message, ex.getMessage());

            ex.setMessage("this is a dummy message for test2");
            assertEquals("this is a dummy message for test2", ex.getMessage());
        }

        {
            ExtendedException ex = new ExtendedException(new IllegalArgumentException("some IllegalArgumentException"));
            ex.setMessage("this is a dummy message for test2");
            assertEquals("this is a dummy message for test2", ex.getMessage());
        }

        {
            String message = "test message for test2, uuid = " + UUID.randomUUID();
            ExtendedException ex = new ExtendedException(message, new UnsupportedOperationException("some UnsupportedOperationException"));
            assertEquals(message, ex.getMessage());
        }
    }
}