package com.wingsweaver.kuja.common.webmvc.jee.context;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ServletContextAccessorTest {
    @Test
    void test() {
        BusinessContext businessContext = BusinessContext.create();
        ServletContextAccessor accessor = new ServletContextAccessor(businessContext);

        // servletRequest
        {
            assertNull(accessor.getServletRequest());
            HttpServletRequest servletRequest = new MockHttpServletRequest();
            accessor.setServletRequest(servletRequest);
            assertSame(servletRequest, accessor.getServletRequest());
        }

        // servletResponse
        {
            assertNull(accessor.getServletResponse());
            HttpServletResponse servletResponse = new MockHttpServletResponse();
            accessor.setServletResponse(servletResponse);
            assertSame(servletResponse, accessor.getServletResponse());
        }
    }
}