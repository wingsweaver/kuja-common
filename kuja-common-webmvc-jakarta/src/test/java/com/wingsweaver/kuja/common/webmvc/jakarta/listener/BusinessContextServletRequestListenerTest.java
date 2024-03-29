package com.wingsweaver.kuja.common.webmvc.jakarta.listener;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextType;
import com.wingsweaver.kuja.common.boot.context.DefaultBusinessContextFactory;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import com.wingsweaver.kuja.common.webmvc.common.constants.KujaCommonWebMvcOrders;
import com.wingsweaver.kuja.common.webmvc.jakarta.context.ServletContextAccessor;
import com.wingsweaver.kuja.common.webmvc.jakarta.util.ServletRequestUtil;
import jakarta.servlet.ServletRequestEvent;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class BusinessContextServletRequestListenerTest {

    @Test
    void requestDestroyed() {
        BusinessContextServletRequestListener listener = new BusinessContextServletRequestListener();
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        ServletRequestEvent sre = new ServletRequestEvent(servletContext, servletRequest);

        // ServletRequest 中没有关联 BusinessContext 的场景
        listener.requestDestroyed(sre);
        assertNull(ServletRequestUtil.getBusinessContext(servletRequest));

        // ServletRequest 中关联了 BusinessContext 的场景
        BusinessContext businessContext = new MapBusinessContext();
        ServletRequestUtil.setBusinessContext(servletRequest, businessContext);
        listener.requestDestroyed(sre);
        assertNull(ServletRequestUtil.getBusinessContext(servletRequest));
    }

    @Test
    void requestInitialized() throws Exception {
        DefaultBusinessContextFactory businessContextFactory = new DefaultBusinessContextFactory();
        businessContextFactory.afterPropertiesSet();

        BusinessContextServletRequestListener listener = new BusinessContextServletRequestListener();
        listener.setBusinessContextFactory(businessContextFactory);
        listener.afterPropertiesSet();

        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        ServletRequestEvent sre = new ServletRequestEvent(servletContext, servletRequest);

        // ServletRequest 中没有关联 BusinessContext 的场景
        {
            listener.requestInitialized(sre);
            BusinessContext businessContext = ServletRequestUtil.getBusinessContext(servletRequest);
            assertNotNull(businessContext);
            ServletContextAccessor accessor = new ServletContextAccessor(businessContext);
            assertSame(BusinessContextType.Web.Blocked.JakartaEE.SERVLET, businessContext.getContextType());
            assertSame(servletRequest, accessor.getOriginalRequest());
            assertSame(servletRequest, accessor.getServletRequest());
        }

        // ServletRequest 中关联了 BusinessContext 的场景
        {
            ServletRequestUtil.removeBusinessContext(servletRequest);
            BusinessContext businessContext = new MapBusinessContext();
            ServletRequestUtil.setBusinessContext(servletRequest, businessContext);
            listener.requestInitialized(sre);
            assertSame(businessContext, ServletRequestUtil.getBusinessContext(servletRequest));

            ServletContextAccessor accessor = new ServletContextAccessor(businessContext);
            assertSame(BusinessContextType.Web.Blocked.JakartaEE.SERVLET, businessContext.getContextType());
            assertSame(servletRequest, accessor.getOriginalRequest());
            assertSame(servletRequest, accessor.getServletRequest());
        }
    }

    @Test
    void afterPropertiesSet() throws Exception {
        DefaultBusinessContextFactory businessContextFactory = new DefaultBusinessContextFactory();
        businessContextFactory.afterPropertiesSet();

        BusinessContextServletRequestListener listener = new BusinessContextServletRequestListener();
        assertEquals(KujaCommonWebMvcOrders.BUSINESS_CONTEXT_SERVLET_REQUEST_LISTENER_ORDER, listener.getOrder());
        assertNull(listener.getBusinessContextFactory());
        listener.setBusinessContextFactory(businessContextFactory);
        assertSame(businessContextFactory, listener.getBusinessContextFactory());
        listener.afterPropertiesSet();
    }
}