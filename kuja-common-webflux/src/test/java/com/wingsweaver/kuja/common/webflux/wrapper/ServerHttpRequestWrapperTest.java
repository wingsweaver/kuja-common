package com.wingsweaver.kuja.common.webflux.wrapper;

import com.wingsweaver.kuja.common.web.wrapper.HostInfo;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerHttpRequestWrapperTest {
    @Test
    void test() throws UnknownHostException {
        MockServerHttpRequest request = MockServerHttpRequest
                .method(HttpMethod.DELETE, "https://example.com/test")
                .header("x-custom", "12345678")
                .queryParam("id", "100")
                .localAddress(new InetSocketAddress(InetAddress.getLocalHost(), 1234))
                .remoteAddress(new InetSocketAddress(InetAddress.getLocalHost(), 5678))
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        ServerHttpRequestWrapper requestWrapper = new ServerHttpRequestWrapper(exchange, request);

        assertSame(exchange, requestWrapper.getExchange());
        assertSame(request, requestWrapper.getRequest());
        assertSame(request, requestWrapper.getOriginalRequest());

        // attributes
        exchange.getAttributes().clear();
        assertTrue(requestWrapper.getAttributeNames().isEmpty());
        assertNull(requestWrapper.getAttribute("test"));
        assertEquals("default-value", requestWrapper.getAttribute("test", "default-value"));
        requestWrapper.setAttribute("test", "test-value");
        assertEquals("test-value", requestWrapper.getAttribute("test"));
        requestWrapper.removeAttribute("test");
        assertNull(requestWrapper.getAttribute("test"));

        // headers
        assertIterableEquals(Collections.singleton("x-custom"), requestWrapper.getHeaderNames());
        assertEquals("12345678", requestWrapper.getHeader("x-custom"));
        assertEquals("12345678", requestWrapper.getHeader("x-custom", "tom-jerry"));
        assertIterableEquals(Collections.singleton("12345678"), requestWrapper.getHeaders("x-custom"));

        // query
        assertIterableEquals(Collections.singleton("id"), requestWrapper.getParameterNames());
        assertEquals("100", requestWrapper.getParameter("id"));
        assertEquals("100", requestWrapper.getParameter("id", "default-id"));
        assertIterableEquals(Collections.singleton("100"), requestWrapper.getParameters("id"));

        // localhost & remotehost
        InetAddress host = InetAddress.getLocalHost();
        HostInfo localHost = requestWrapper.getLocalHost();
        assertEquals(host.getHostName(), localHost.getName());
        assertEquals(host.getHostAddress(), localHost.getAddress());
        assertEquals(1234, localHost.getPort());
        HostInfo remoteHost = requestWrapper.getRemoteHost();
        assertEquals(host.getHostName(), remoteHost.getName());
        assertEquals(host.getHostAddress(), remoteHost.getAddress());
        assertEquals(5678, remoteHost.getPort());

        // others
        assertEquals("https", requestWrapper.getScheme());
        assertEquals("DELETE", requestWrapper.getMethod());
        assertEquals("/test", requestWrapper.getRequestPath());
        assertEquals("id=100", requestWrapper.getQueryString());
        assertEquals("/test?id=100", requestWrapper.getFullRequestPath());
        assertFalse(requestWrapper.hasParts());
    }


    @Test
    void test2() throws Exception {
        String body = "--12345\r\n" +
                "Content-Disposition: form-data; name=\"foo\"\r\n" +
                "\r\n" +
                "bar\r\n" +
                "--12345\r\n" +
                "Content-Disposition: form-data; name=\"baz\"\r\n" +
                "\r\n" +
                "qux\r\n" +
                "--12345--\r\n";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=12345");
        MockServerHttpRequest request = MockServerHttpRequest
                .method(HttpMethod.GET, "https://example.com/test2")
                .headers(httpHeaders)
                .body(body);
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        ServerHttpRequestWrapper requestWrapper = new ServerHttpRequestWrapper(exchange, request);
        assertEquals("/test2", requestWrapper.getRequestPath());
        assertEquals("/test2", requestWrapper.getFullRequestPath());
        assertNull(requestWrapper.getQueryString());
        assertNull(requestWrapper.getLocalHost());
        assertNull(requestWrapper.getRemoteHost());
        assertTrue(requestWrapper.hasParts());
    }
}