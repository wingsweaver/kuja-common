package com.wingsweaver.kuja.common.webmvc.jee.context;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.web.context.WebContextAccessor;
import com.wingsweaver.kuja.common.webmvc.common.context.WebMvcContextAccessor;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 面向 Servlet (J2EE) 处理的 {@link WebContextAccessor} 子类。
 *
 * @author wingsweaver
 */
public class ServletContextAccessor extends WebMvcContextAccessor {
    public ServletContextAccessor(BusinessContext context) {
        super(context);
    }

    /**
     * Key: ServletRequest。
     */
    public static final String KEY_SERVLET_REQUEST = ClassUtil.resolveKey(ServletRequest.class);

    public ServletRequest getServletRequest() {
        return getAttribute(KEY_SERVLET_REQUEST);
    }

    public void setServletRequest(ServletRequest servletRequest) {
        setAttribute(KEY_SERVLET_REQUEST, servletRequest);
    }

    /**
     * Key: ServletResponse。
     */
    public static final String KEY_SERVLET_RESPONSE = ClassUtil.resolveKey(ServletResponse.class);

    public ServletResponse getServletResponse() {
        return getAttribute(KEY_SERVLET_RESPONSE);
    }

    public void setServletResponse(ServletResponse servletResponse) {
        setAttribute(KEY_SERVLET_RESPONSE, servletResponse);
    }
}
