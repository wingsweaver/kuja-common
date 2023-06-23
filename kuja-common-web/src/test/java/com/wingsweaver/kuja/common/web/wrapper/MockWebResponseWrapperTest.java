package com.wingsweaver.kuja.common.web.wrapper;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MockWebResponseWrapperTest {
    @Test
    void test() {
        MockWebResponseWrapper responseWrapper = new MockWebResponseWrapper();

        // originalResponse
        {
            assertNull(responseWrapper.getOriginalResponse());
            responseWrapper.setOriginalResponse("some-response");
            assertEquals("some-response", responseWrapper.getOriginalResponse());
        }

        // statusCode
        {
            assertEquals(200, responseWrapper.getStatusCode());
            responseWrapper.setStatusCode(404);
            assertEquals(404, responseWrapper.getStatusCode());
        }

        // headers
        {
            assertTrue(responseWrapper.getHeaders().isEmpty());
            assertTrue(responseWrapper.getHeaderNames().isEmpty());

            assertNull(responseWrapper.getHeader("host.name"));
            assertEquals("some-host", responseWrapper.getHeader("host.name", "some-host"));

            responseWrapper.setHeader("host.name", "localhost");
            assertEquals("localhost", responseWrapper.getHeader("host.name"));
            assertEquals("localhost", responseWrapper.getHeader("host.name", "some-host"));

            responseWrapper.setHeader("host.name", "some-host");
            assertEquals("some-host", responseWrapper.getHeader("host.name"));
            assertEquals("some-host", responseWrapper.getHeader("host.name", "localhost"));

            responseWrapper.setHeaders("host.name", Arrays.asList("pc-1", "pc-2"));
            assertEquals("pc-1", responseWrapper.getHeader("host.name"));
            assertIterableEquals(Arrays.asList("pc-1", "pc-2"), responseWrapper.getHeaders("host.name"));

            responseWrapper.setHeaders("host.name.2", new TreeSet<>(Arrays.asList("pc-1", "pc-2")));
            assertEquals("pc-1", responseWrapper.getHeader("host.name.2"));
            assertIterableEquals(Arrays.asList("pc-1", "pc-2"), responseWrapper.getHeaders("host.name.2"));

            responseWrapper.removeHeader("host.name");
            assertNull(responseWrapper.getHeader("host.name"));

            responseWrapper.addHeader("host.name", "pc-1");
            assertEquals("pc-1", responseWrapper.getHeader("host.name"));

            responseWrapper.addHeader("host.name", "pc-2");
            assertEquals("pc-1", responseWrapper.getHeader("host.name"));
            assertIterableEquals(Arrays.asList("pc-1", "pc-2"), responseWrapper.getHeaders("host.name"));

            responseWrapper.addHeaders("host.name", Collections.singletonList("pc-3"));
            assertIterableEquals(Arrays.asList("pc-1", "pc-2", "pc-3"), responseWrapper.getHeaders("host.name"));

            responseWrapper.addHeaders("host.name", Collections.singleton("pc-4"));
            assertIterableEquals(Arrays.asList("pc-1", "pc-2", "pc-3", "pc-4"), responseWrapper.getHeaders("host.name"));
        }
    }
}