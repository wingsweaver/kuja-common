package com.wingsweaver.kuja.common.webmvc.jakarta.filter;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextCustomizer;
import com.wingsweaver.kuja.common.boot.context.BusinessContextFactory;
import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.boot.context.BusinessContextType;
import com.wingsweaver.kuja.common.boot.context.BusinessContextTypeSetter;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.utils.support.spring.BeanUtil;
import com.wingsweaver.kuja.common.webmvc.common.constants.KujaCommonWebMvcOrders;
import com.wingsweaver.kuja.common.webmvc.jakarta.context.ServletContextAccessor;
import com.wingsweaver.kuja.common.webmvc.jakarta.util.ServletRequestUtil;
import com.wingsweaver.kuja.common.webmvc.jakarta.wrapper.HttpServletRequestWrapper;
import com.wingsweaver.kuja.common.webmvc.jakarta.wrapper.HttpServletResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 用于处理业务上下文的 {@link OncePerRequestFilter} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BusinessContextFilter extends GenericFilterBean implements ApplicationContextAware, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessContextFilter.class);

    private static final String KEY_INITIALIZED = ClassUtil.resolveKey(BusinessContextFilter.class, "initialized");

    /**
     * Spring 应用上下文。
     */
    private ApplicationContext applicationContext;

    /**
     * 业务上下文工厂类的实例。
     */
    private BusinessContextFactory businessContextFactory;

    /**
     * 业务上下文的定制处理器。
     */
    private List<BusinessContextCustomizer> businessContextCustomizers;

    @Override
    public int getOrder() {
        return KujaCommonWebMvcOrders.BUSINESS_CONTEXT_FILTER;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取或者创建 BusinessContext
        boolean created = false;
        BusinessContext businessContext = ServletRequestUtil.getBusinessContext(request);
        if (businessContext == null) {
            businessContext = this.businessContextFactory.create();
            ServletRequestUtil.setBusinessContext(request, businessContext);
            LogUtil.trace(LOGGER, "Create new business context {} and bind to request", businessContext);
            created = true;
        }

        // 初始化 BusinessContext（只初始化一次）
        if (created || !ServletRequestUtil.getAttribute(request, KEY_INITIALIZED, false)) {
            this.initializeBusinessContext(businessContext, request, response);
            ServletRequestUtil.setAttribute(request, KEY_INITIALIZED, true);
        }

        // 调用 FilterChain
        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext)) {
            LogUtil.trace(LOGGER, "Invoke filter chain");
            chain.doFilter(request, response);
        }
    }

    private void initializeBusinessContext(BusinessContext businessContext, ServletRequest request, ServletResponse response) {
        // 设置 BusinessContextType
        if (businessContext instanceof BusinessContextTypeSetter setter) {
            setter.setContextType(BusinessContextType.Web.Blocked.JakartaEE.SERVLET);
        }

        // 按照 ServletContextAccessor 进行设置
        ServletContextAccessor accessor = new ServletContextAccessor(businessContext);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        accessor.setOriginalRequest(request);
        accessor.setOriginalResponse(response);
        accessor.setServerHttpRequest(new ServletServerHttpRequest(httpServletRequest));
        accessor.setServerHttpResponse(new ServletServerHttpResponse(httpServletResponse));
        accessor.setWebRequest(new ServletWebRequest(httpServletRequest, httpServletResponse));
        accessor.setServletRequest(request);
        accessor.setServletResponse(response);
        accessor.setRequestWrapper(new HttpServletRequestWrapper(httpServletRequest));
        accessor.setResponseWrapper(new HttpServletResponseWrapper(httpServletResponse));

        // 调用 Customizer 进行处理
        for (BusinessContextCustomizer customizer : this.businessContextCustomizers) {
            customizer.customize(businessContext);
        }
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        // 初始化 businessContextFactory
        this.initBusinessContextFactory();

        // 初始化 businessContextCustomizers
        this.initBusinessContextCustomizers();
    }

    /**
     * 初始化 businessContextCustomizers。
     */
    protected void initBusinessContextCustomizers() {
        if (this.businessContextCustomizers == null) {
            this.businessContextCustomizers = BeanUtil.getBeansOrdered(this.applicationContext, BusinessContextCustomizer.class);
        }
    }

    /**
     * 初始化 businessContextFactory。
     */
    protected void initBusinessContextFactory() {
        if (this.businessContextFactory == null) {
            this.businessContextFactory = BeanUtil.getBean(this.applicationContext, BusinessContextFactory.class);
        }
    }
}
