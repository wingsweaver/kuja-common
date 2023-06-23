package com.wingsweaver.kuja.common.webflux.wrapper;

import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ServerHttpResponseWrapperTest {
    @Test
    void test() {
        MockServerHttpResponse response = new MockServerHttpResponse();
        response.getHeaders().add("x-trace", "1234");

        ServerHttpResponseWrapper responseWrapper = new ServerHttpResponseWrapper(response);

        assertSame(response, responseWrapper.getResponse());
        assertSame(response, responseWrapper.getOriginalResponse());

        // headers
        assertIterableEquals(Collections.singleton("x-trace"), responseWrapper.getHeaderNames());
        assertEquals("1234", responseWrapper.getHeader("x-trace"));
        assertEquals("1234", responseWrapper.getHeader("x-trace", "5678"));
        assertIterableEquals(Collections.singleton("1234"), responseWrapper.getHeaders("x-trace"));
        responseWrapper.setHeader("x-span", "tom");
        assertEquals("tom", responseWrapper.getHeader("x-span"));
        responseWrapper.setHeaders("x-span", Arrays.asList("jerry", "kate"));
        assertIterableEquals(Arrays.asList("jerry", "kate"), responseWrapper.getHeaders("x-span"));
        responseWrapper.addHeader("x-span", "lucy");
        assertIterableEquals(Arrays.asList("jerry", "kate", "lucy"), responseWrapper.getHeaders("x-span"));
        responseWrapper.addHeaders("x-span", Arrays.asList("mike", "nancy"));
        assertIterableEquals(Arrays.asList("jerry", "kate", "lucy", "mike", "nancy"), responseWrapper.getHeaders("x-span"));
        responseWrapper.removeHeader("x-span");
        assertNull(responseWrapper.getHeader("x-span"));

        // status
        assertNull(responseWrapper.getStatusCode());
        responseWrapper.setStatusCode(403);
        assertEquals(403, responseWrapper.getStatusCode());
    }
}