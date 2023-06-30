package com.wingsweaver.kuja.common.web.utils;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogContext;
import com.wingsweaver.kuja.common.web.wrapper.MockWebRequestWrapper;
import com.wingsweaver.kuja.common.web.wrapper.WebRequestWrapper;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LogContextConfigBuilderTest {
    @Test
    void buildBusinessContext() {
        assertNull(LogContextConfigBuilder.build((BusinessContext) null));

        BusinessContext businessContext = new MapBusinessContext();
        assertNull(LogContextConfigBuilder.build(businessContext));
    }

    @Test
    void buildWebRequestWrapper() {
        assertNull(LogContextConfigBuilder.build((WebRequestWrapper) null));

        MockWebRequestWrapper requestWrapper = new MockWebRequestWrapper();
        assertNull(LogContextConfigBuilder.build(requestWrapper));

        requestWrapper.getHeaders().set(LogContextConfigBuilder.HEADER_LOGGER, "test-logger");
        requestWrapper.getHeaders().set(LogContextConfigBuilder.HEADER_MARKER, "test-marker");
        requestWrapper.getHeaders().set(LogContextConfigBuilder.HEADER_LEVEL, "ERROR");
        LogContext.Config config = LogContextConfigBuilder.build(requestWrapper);

        assertEquals("test-logger", config.getLogger().getName());
        assertEquals("test-marker", config.getMarker().getName());
        assertEquals(Level.ERROR, config.getLevel());
    }

    @Test
    void buildFunction() {
        assertNull(LogContextConfigBuilder.build((Function<String, String>) null));

        Function<String, String> function = s -> null;
        assertNull(LogContextConfigBuilder.build(function));

        function = s -> "";
        assertNull(LogContextConfigBuilder.build(function));

        function = s -> {
            switch (s) {
                case LogContextConfigBuilder.HEADER_LOGGER:
                    return "test-logger-2";
                case LogContextConfigBuilder.HEADER_MARKER:
                    return "test-marker-2";
                case LogContextConfigBuilder.HEADER_LEVEL:
                    return "warn";
                default:
                    return null;
            }
        };
        LogContext.Config config = LogContextConfigBuilder.build(function);
        assertEquals("test-logger-2", config.getLogger().getName());
        assertEquals("test-marker-2", config.getMarker().getName());
        assertEquals(Level.WARN, config.getLevel());
    }
}