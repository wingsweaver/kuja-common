package com.wingsweaver.kuja.common.webmvc.jakarta.context;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ServletContextAccessorTest {
    @Test
    void test() {
        BusinessContext businessContext = new MapBusinessContext();
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