package com.wingsweaver.kuja.common.webmvc.common.support;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessContextHandlerMethodArgumentResolverTest {

    @Test
    void supportsParameter() throws NoSuchMethodException {
        BusinessContextHandlerMethodArgumentResolver argumentResolver = new BusinessContextHandlerMethodArgumentResolver();
        assertTrue(argumentResolver.supportsParameter(createMethodParameter("foo")));
        assertFalse(argumentResolver.supportsParameter(createMethodParameter("bar")));
    }

    @Test
    void resolveArgument() throws Exception {
        BusinessContextHandlerMethodArgumentResolver argumentResolver = new BusinessContextHandlerMethodArgumentResolver();
        HttpServletRequest servletRequest = new MockHttpServletRequest();
        NativeWebRequest webRequest = new ServletWebRequest(servletRequest);
        assertNull(argumentResolver.resolveArgument(null, null, webRequest, null));

        BusinessContext businessContext = new MapBusinessContext();
        servletRequest.setAttribute(BusinessContextHandlerMethodArgumentResolver.KEY_BUSINESS_CONTEXT, businessContext);
        assertSame(businessContext, argumentResolver.resolveArgument(null, null, webRequest, null));
    }

    private MethodParameter createMethodParameter(String methodName) throws NoSuchMethodException {
        Method method = this.getClass().getDeclaredMethod(methodName);
        return new MethodParameter(method, -1);
    }

    BusinessContext foo() {
        return null;
    }

    ReturnValue bar() {
        return null;
    }
}