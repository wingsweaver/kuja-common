package com.wingsweaver.kuja.common.webmvc.jee.filter;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextCustomizer;
import com.wingsweaver.kuja.common.boot.context.DefaultBusinessContextFactory;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import com.wingsweaver.kuja.common.webmvc.common.constants.KujaCommonWebMvcOrders;
import com.wingsweaver.kuja.common.webmvc.jee.util.ServletRequestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class BusinessContextFilterTest {

    @Test
    void doFilter() throws Exception {
        DefaultBusinessContextFactory businessContextFactory = new DefaultBusinessContextFactory();
        businessContextFactory.afterPropertiesSet();

        BusinessContextFilter filter = new BusinessContextFilter();
        assertNull(filter.getBusinessContextFactory());
        assertNull(filter.getBusinessContextCustomizers());
        assertEquals(KujaCommonWebMvcOrders.BUSINESS_CONTEXT_FILTER, filter.getOrder());

        filter.setBusinessContextFactory(businessContextFactory);
        BusinessContextCustomizer customizer = businessContext -> {
            businessContext.setAttribute("test-attr", "test-value-12345678");
        };
        filter.setBusinessContextCustomizers(Collections.singletonList(customizer));
        filter.afterPropertiesSet();

        testWithoutBusinessContext(filter);
        testWithBusinessContext(filter);
    }

    private void testWithBusinessContext(BusinessContextFilter filter) throws ServletException, IOException {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        BusinessContext businessContext = new MapBusinessContext();
        ServletRequestUtil.setBusinessContext(servletRequest, businessContext);

        filter.doFilter(servletRequest, servletResponse, filterChain);
        assertSame(businessContext, ServletRequestUtil.getBusinessContext(servletRequest));
        assertEquals("test-value-12345678", businessContext.getAttribute("test-attr"));
    }

    private void testWithoutBusinessContext(BusinessContextFilter filter) throws ServletException, IOException {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        filter.doFilter(servletRequest, servletResponse, filterChain);
        BusinessContext businessContext = ServletRequestUtil.getBusinessContext(servletRequest);
        assertNotNull(businessContext);
        assertEquals("test-value-12345678", businessContext.getAttribute("test-attr"));
    }
}