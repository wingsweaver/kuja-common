package com.wingsweaver.kuja.common.webmvc.jakarta.filter;

import com.wingsweaver.kuja.common.webmvc.common.constants.KujaCommonWebMvcOrders;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicLogContextFilterTest {

    @Test
    void doFilter() throws ServletException, IOException {
        DynamicLogContextFilter filter = new DynamicLogContextFilter();
        assertEquals(KujaCommonWebMvcOrders.DYNAMIC_LOG_CONTEXT_FILTER, filter.getOrder());

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilter(servletRequest, servletResponse, filterChain);
    }
}