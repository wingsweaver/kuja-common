package com.wingsweaver.kuja.common.webmvc.jakarta.util;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ServletRequestUtilTest {

    @Test
    void testAttributes() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        String key = "some-attribute";
        assertNull(ServletRequestUtil.getAttribute(servletRequest, key));
        assertEquals("default-value", ServletRequestUtil.getAttribute(servletRequest, key, "default-value"));
        assertEquals("default-value", ServletRequestUtil.getAttribute(servletRequest, key, () -> "default-value"));
        Object value = new Object();
        ServletRequestUtil.setAttribute(servletRequest, key, value);
        assertSame(value, ServletRequestUtil.getAttribute(servletRequest, key));
        ServletRequestUtil.removeAttribute(servletRequest, key);
        assertNull(ServletRequestUtil.getAttribute(servletRequest, key));
        ServletRequestUtil.setAttribute(servletRequest, key, null);
        assertNull(ServletRequestUtil.getAttribute(servletRequest, key));
    }

    @Test
    void testBusinessContext() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        assertNull(ServletRequestUtil.getBusinessContext(servletRequest));
        BusinessContext businessContext = BusinessContext.create();
        ServletRequestUtil.setBusinessContext(servletRequest, businessContext);
        assertSame(businessContext, ServletRequestUtil.getBusinessContext(servletRequest));
    }

    @Test
    void resolveFullPath() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/test");
        assertEquals("/test", ServletRequestUtil.resolveFullPath(servletRequest));
        assertEquals("/test", ServletRequestUtil.resolveFullPath(servletRequest));

        ServletRequestUtil.removeAttribute(servletRequest, ServletRequestUtil.KEY_FULL_PATH);
        servletRequest.setQueryString("id=100");
        assertEquals("/test?id=100", ServletRequestUtil.resolveFullPath(servletRequest));
    }

    @Test
    void getErrorRequestUri() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        assertNull(ServletRequestUtil.getErrorRequestUri(servletRequest));
        servletRequest.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, "some-error-request-uri");
        assertEquals("some-error-request-uri", ServletRequestUtil.getErrorRequestUri(servletRequest));
    }
}