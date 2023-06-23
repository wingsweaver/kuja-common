package com.wingsweaver.kuja.common.web.context;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.web.wrapper.MockWebRequestWrapper;
import com.wingsweaver.kuja.common.web.wrapper.MockWebResponseWrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class WebContextAccessorTest {
    @Test
    void test() {
        BusinessContext businessContext = BusinessContext.create();
        WebContextAccessor accessor = new WebContextAccessor(businessContext);

        // originalRequest
        {
            assertNull(accessor.getOriginalRequest());
            Object originalRequest = new Object();
            accessor.setOriginalRequest(originalRequest);
            assertSame(originalRequest, accessor.getOriginalRequest());
        }

        // originalResponse
        {
            assertNull(accessor.getOriginalResponse());
            Object originalResponse = new Object();
            accessor.setOriginalResponse(originalResponse);
            assertSame(originalResponse, accessor.getOriginalResponse());
        }

        // webRequestWrapper
        {
            assertNull(accessor.getRequestWrapper());
            assertNull(accessor.getRequestUri());
            assertNull(accessor.getRequestFullPath());

            MockWebRequestWrapper requestWrapper = new MockWebRequestWrapper();
            requestWrapper.setRequestPath("/test");
            requestWrapper.getParameters().set("id", "100");
            accessor.setRequestWrapper(requestWrapper);
            assertSame(requestWrapper, accessor.getRequestWrapper());
            assertEquals("/test", accessor.getRequestUri());
            assertEquals("/test?id=100", accessor.getRequestFullPath());
        }

        // webResponseWrapper
        {
            assertNull(accessor.getResponseWrapper());
            MockWebResponseWrapper responseWrapper = new MockWebResponseWrapper();
            accessor.setResponseWrapper(responseWrapper);
            assertSame(responseWrapper, accessor.getResponseWrapper());
        }
    }

}