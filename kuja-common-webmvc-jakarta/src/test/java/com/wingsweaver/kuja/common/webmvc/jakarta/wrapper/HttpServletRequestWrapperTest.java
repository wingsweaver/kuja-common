package com.wingsweaver.kuja.common.webmvc.jakarta.wrapper;

import com.wingsweaver.kuja.common.web.wrapper.HostInfo;
import jakarta.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpServletRequestWrapperTest {

    @Test
    void testAttributes() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(servletRequest);

        assertTrue(CollectionUtils.isEmpty(wrapper.getAttributeNames()));
        String key = "some-header";
        assertNull(wrapper.getAttribute(key));
        assertEquals("some-value", wrapper.getAttribute(key, "some-value"));

        Object value = new Object();
        wrapper.setAttribute(key, value);
        assertSame(value, wrapper.getAttribute(key));

        wrapper.removeAttribute(key);
        assertNull(wrapper.getAttribute(key));

        wrapper.setAttribute(key, null);
        assertNull(wrapper.getAttribute(key));
    }

    @Test
    void testHeaders() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(servletRequest);

        assertTrue(CollectionUtils.isEmpty(wrapper.getHeaderNames()));
        String key = "some-header";
        assertNull(wrapper.getHeader(key));
        assertEquals("some-value", wrapper.getHeader(key, "some-value"));
        assertTrue(CollectionUtils.isEmpty(wrapper.getHeaders(key)));

        servletRequest.addHeader("some-header", "some-value");
        assertEquals("some-value", wrapper.getHeader(key));
        assertIterableEquals(Collections.singleton("some-value"), wrapper.getHeaders(key));
    }

    @Test
    void testParameters() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(servletRequest);

        assertTrue(CollectionUtils.isEmpty(wrapper.getParameterNames()));
        String key = "some-param";
        assertNull(wrapper.getParameter(key));
        assertEquals("some-value", wrapper.getParameter(key, "some-value"));
        assertTrue(CollectionUtils.isEmpty(wrapper.getParameters(key)));

        servletRequest.addParameter("some-param", "some-value");
        assertEquals("some-value", wrapper.getParameter(key));
        assertIterableEquals(Collections.singleton("some-value"), wrapper.getParameters(key));
    }

    @Test
    void testHosts() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(servletRequest);

        // localhost
        {
            servletRequest.setLocalName("localhost");
            servletRequest.setLocalAddr("192.168.0.1");
            servletRequest.setLocalPort(1234);

            HostInfo localHost = wrapper.getLocalHost();
            assertEquals("localhost", localHost.getName());
            assertEquals("192.168.0.1", localHost.getAddress());
            assertEquals(1234, localHost.getPort());
        }

        // remote host
        {
            servletRequest.setRemoteHost("localhost2");
            servletRequest.setRemoteAddr("192.168.1.2");
            servletRequest.setRemotePort(9876);

            HostInfo remoteHost = wrapper.getRemoteHost();
            assertEquals("localhost2", remoteHost.getName());
            assertEquals("192.168.1.2", remoteHost.getAddress());
            assertEquals(9876, remoteHost.getPort());
        }
    }

    @Test
    void testParts() throws ServletException, IOException {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(servletRequest);
        assertFalse(wrapper.hasParts());

        MockPart part = new MockPart("some-part", new byte[0]);
        servletRequest.addPart(part);
        assertTrue(wrapper.hasParts());
    }

    @Test
    void testOthers() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("POST", "https://www.example.com/test");
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(servletRequest);

        assertSame(servletRequest, wrapper.getOriginalRequest());
        assertEquals("http", wrapper.getScheme());
        assertEquals("POST", wrapper.getMethod());
        assertEquals("/test", wrapper.getRequestPath());
        assertTrue(StringUtils.isEmpty(wrapper.getQueryString()));
        assertEquals("/test", wrapper.getFullRequestPath());

        servletRequest.setQueryString("id=100");
        assertEquals("id=100", wrapper.getQueryString());
        assertEquals("/test?id=100", wrapper.getFullRequestPath());

    }
}