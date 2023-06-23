package com.wingsweaver.kuja.common.web.wrapper;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MockWebRequestWrapperTest {
    @Test
    void test() {
        MockWebRequestWrapper requestWrapper = new MockWebRequestWrapper();

        // attributes
        {
            assertTrue(requestWrapper.getAttributes().isEmpty());
            assertTrue(requestWrapper.getAttributeNames().isEmpty());

            assertNull(requestWrapper.getAttribute("host.name"));
            assertEquals("some-host", requestWrapper.getAttribute("host.name", "some-host"));

            requestWrapper.setAttribute("host.name", "localhost");
            assertEquals("localhost", requestWrapper.getAttribute("host.name"));
            assertEquals("localhost", requestWrapper.getAttribute("host.name", "some-host"));

            requestWrapper.removeAttribute("host.name");
            assertNull(requestWrapper.getAttribute("host.name"));
        }

        // headers
        {
            assertTrue(requestWrapper.getHeaders().isEmpty());
            assertTrue(requestWrapper.getHeaderNames().isEmpty());

            assertNull(requestWrapper.getHeader("host.name"));
            assertEquals("some-host", requestWrapper.getHeader("host.name", "some-host"));

            HttpHeaders headers = requestWrapper.getHeaders();
            headers.add("host.name", "localhost");
            assertEquals("localhost", requestWrapper.getHeader("host.name"));
            Collection<String> values = requestWrapper.getHeaders("host.name");
            assertEquals(1, values.size());
            assertEquals("localhost", values.iterator().next());

            headers.add("host.name", "some-host");
            assertEquals("localhost", requestWrapper.getHeader("host.name"));
            values = requestWrapper.getHeaders("host.name");
            assertEquals(2, values.size());
            assertIterableEquals(Arrays.asList("localhost", "some-host"), headers.get("host.name"));
        }

        // parameters
        {
            assertTrue(requestWrapper.getParameters().isEmpty());
            assertTrue(requestWrapper.getParameterNames().isEmpty());

            assertNull(requestWrapper.getParameter("host.name"));
            assertEquals("some-host", requestWrapper.getParameter("host.name", "some-host"));

            MultiValueMap<String, String> parameters = requestWrapper.getParameters();
            parameters.add("host.name", "localhost");
            assertEquals("localhost", requestWrapper.getParameter("host.name"));
            Collection<String> values = requestWrapper.getParameters("host.name");
            assertEquals(1, values.size());
            assertEquals("localhost", values.iterator().next());

            parameters.add("host.name", "some-host");
            assertEquals("localhost", requestWrapper.getParameter("host.name"));
            values = requestWrapper.getParameters("host.name");
            assertEquals(2, values.size());
            assertIterableEquals(Arrays.asList("localhost", "some-host"), parameters.get("host.name"));
        }

        // queryString
        {
            MultiValueMap<String, String> parameters = requestWrapper.getParameters();
            parameters.clear();
            assertNull(requestWrapper.getQueryString());

            parameters.add("host.name", "localhost");
            assertEquals("host.name=localhost", requestWrapper.getQueryString());

            parameters.add("host.name", "some-host");
            assertEquals("host.name=localhost&host.name=some-host", requestWrapper.getQueryString());
        }

        // originalRequest
        {
            assertNull(requestWrapper.getOriginalRequest());
            Object originalRequest = new Object();
            requestWrapper.setOriginalRequest(originalRequest);
            assertEquals(originalRequest, requestWrapper.getOriginalRequest());
        }

        // scheme
        {
            assertNull(requestWrapper.getScheme());
            requestWrapper.setScheme("http");
            assertEquals("http", requestWrapper.getScheme());
        }

        // method
        {
            assertNull(requestWrapper.getMethod());
            requestWrapper.setMethod("GET");
            assertEquals("GET", requestWrapper.getMethod());
        }

        // requestUri
        {
            assertNull(requestWrapper.getRequestPath());
            requestWrapper.setRequestPath("/some/path");
            assertEquals("/some/path", requestWrapper.getRequestPath());

            // fullPath
            {
                MultiValueMap<String, String> parameters = requestWrapper.getParameters();
                parameters.clear();
                assertEquals("/some/path", requestWrapper.getFullRequestPath());

                parameters.add("host.name", "localhost");
                assertEquals("/some/path?host.name=localhost", requestWrapper.getFullRequestPath());
            }
        }

        // hasParts
        {
            assertFalse(requestWrapper.hasParts());
            requestWrapper.setHasParts(true);
            assertTrue(requestWrapper.hasParts());
        }

        // localHost
        {
            assertNull(requestWrapper.getLocalHost());
            HostInfo localhost = new HostInfo();
            requestWrapper.setLocalHost(localhost);
            assertEquals(localhost, requestWrapper.getLocalHost());
        }

        // remoteHost
        {
            assertNull(requestWrapper.getRemoteHost());
            HostInfo remoteHost = new HostInfo();
            requestWrapper.setRemoteHost(remoteHost);
            assertEquals(remoteHost, requestWrapper.getRemoteHost());
        }

        // charset
        {
            assertEquals(StandardCharsets.UTF_8, requestWrapper.getCharset());
            requestWrapper.setCharset(Charset.defaultCharset());
            assertEquals(Charset.defaultCharset(), requestWrapper.getCharset());
        }
    }
}