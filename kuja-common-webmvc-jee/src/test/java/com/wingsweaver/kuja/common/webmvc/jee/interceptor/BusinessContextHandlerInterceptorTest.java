package com.wingsweaver.kuja.common.webmvc.jee.interceptor;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextAccessor;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import com.wingsweaver.kuja.common.webmvc.jee.util.ServletRequestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessContextHandlerInterceptorTest {

    @Test
    void preHandle() {
        BusinessContextHandlerInterceptor interceptor = new BusinessContextHandlerInterceptor();

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        Object handler = new Object();

        {
            servletRequest.setAttribute(BusinessContextHandlerInterceptor.KEY_PRE_HANDLED, true);
            assertTrue(interceptor.preHandle(servletRequest, servletResponse, handler));
            assertEquals(true, servletRequest.getAttribute(BusinessContextHandlerInterceptor.KEY_PRE_HANDLED));
        }

        {
            servletRequest.setAttribute(BusinessContextHandlerInterceptor.KEY_PRE_HANDLED, false);
            assertTrue(interceptor.preHandle(servletRequest, servletResponse, handler));
            assertEquals(true, servletRequest.getAttribute(BusinessContextHandlerInterceptor.KEY_PRE_HANDLED));
        }

        {
            servletRequest.setAttribute(BusinessContextHandlerInterceptor.KEY_PRE_HANDLED, false);
            BusinessContext businessContext = new MapBusinessContext();
            ServletRequestUtil.setBusinessContext(servletRequest, businessContext);
            assertTrue(interceptor.preHandle(servletRequest, servletResponse, handler));
            assertEquals(true, servletRequest.getAttribute(BusinessContextHandlerInterceptor.KEY_PRE_HANDLED));
            BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
            assertSame(handler, accessor.getHandler());
        }
    }

    @Test
    void afterCompletion() {
        BusinessContextHandlerInterceptor interceptor = new BusinessContextHandlerInterceptor();

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        Object handler = new Object();

        {
            servletRequest.setAttribute(BusinessContextHandlerInterceptor.KEY_AFTER_COMPLETION, true);
            interceptor.afterCompletion(servletRequest, servletResponse, handler, null);
            assertEquals(true, servletRequest.getAttribute(BusinessContextHandlerInterceptor.KEY_AFTER_COMPLETION));
        }

        {
            servletRequest.setAttribute(BusinessContextHandlerInterceptor.KEY_AFTER_COMPLETION, false);
            BusinessContext businessContext = new MapBusinessContext();
            ServletRequestUtil.setBusinessContext(servletRequest, businessContext);
            Exception error = new Exception("some-error");
            interceptor.afterCompletion(servletRequest, servletResponse, handler, error);
            assertEquals(true, servletRequest.getAttribute(BusinessContextHandlerInterceptor.KEY_AFTER_COMPLETION));
            BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
            assertSame(handler, accessor.getHandler());
            assertSame(error, accessor.getError());
        }
    }
}