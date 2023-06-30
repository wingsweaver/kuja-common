package com.wingsweaver.kuja.common.webmvc.common.context;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class WebMvcContextAccessorTest {
    @Test
    void test() {
        BusinessContext businessContext = new MapBusinessContext();
        WebMvcContextAccessor accessor = new WebMvcContextAccessor(businessContext);

        // serverHttpRequest
        {
            assertNull(accessor.getServerHttpRequest());
            HttpServletRequest servletRequest = new MockHttpServletRequest();
            ServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(servletRequest);
            accessor.setServerHttpRequest(serverHttpRequest);
            assertSame(serverHttpRequest, accessor.getServerHttpRequest());
        }

        // serverHttpResponse
        {
            assertNull(accessor.getServerHttpResponse());
            HttpServletResponse servletResponse = new MockHttpServletResponse();
            ServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(servletResponse);
            accessor.setServerHttpResponse(serverHttpResponse);
            assertSame(serverHttpResponse, accessor.getServerHttpResponse());
        }

        // webRequest
        {
            assertNull(accessor.getWebRequest());
            HttpServletRequest servletRequest = new MockHttpServletRequest();
            WebRequest webRequest = new ServletWebRequest(servletRequest);
            accessor.setWebRequest(webRequest);
            assertSame(webRequest, accessor.getWebRequest());
        }
    }
}