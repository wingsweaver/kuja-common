package com.wingsweaver.kuja.common.webmvc.jee.wrapper;

import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpServletResponseWrapperTest {

    @Test
    void testHeaders() {
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(servletResponse);

        assertTrue(CollectionUtils.isEmpty(wrapper.getHeaderNames()));
        String key = "some-header";
        assertNull(wrapper.getHeader(key));
        assertEquals("some-value", wrapper.getHeader(key, "some-value"));
        assertTrue(CollectionUtils.isEmpty(wrapper.getHeaders(key)));

        String value = "some-value";
        wrapper.setHeader(key, value);
        assertEquals(value, wrapper.getHeader(key));
        assertEquals(value, wrapper.getHeader(key, "some-other-value"));
        assertIterableEquals(Collections.singletonList(value), wrapper.getHeaders(key));

        wrapper.setHeaders(key, null);
//        assertNull(wrapper.getHeader(key));

        wrapper.setHeaders(key, CollectionUtil.listOf("some-value-2", "some-value-3"));
        assertEquals("some-value-2", wrapper.getHeader(key));
        assertIterableEquals(CollectionUtil.listOf("some-value-2", "some-value-3"), wrapper.getHeaders(key));

        wrapper.removeHeader(key);
//        assertNull(wrapper.getHeader(key));

        String key2 = "some-header-2";
        wrapper.addHeader(key2, "some-value-4");
        assertEquals("some-value-4", wrapper.getHeader(key2));
        assertIterableEquals(CollectionUtil.listOf("some-value-4"), wrapper.getHeaders(key2));

        wrapper.addHeaders(key2, CollectionUtil.listOf("some-value-5"));
        assertEquals("some-value-4", wrapper.getHeader(key2));
        assertIterableEquals(CollectionUtil.listOf("some-value-4", "some-value-5"), wrapper.getHeaders(key2));
    }

    @Test
    void testOthers() {
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(servletResponse);

        assertSame(servletResponse, wrapper.getOriginalResponse());

        assertEquals(200, wrapper.getStatusCode());

        wrapper.setStatusCode(null);
        assertEquals(200, wrapper.getStatusCode());

        wrapper.setStatusCode(200);
        assertEquals(200, wrapper.getStatusCode());
    }
}