package com.wingsweaver.kuja.common.webmvc.jee.filter;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogContext;
import com.wingsweaver.kuja.common.web.utils.LogContextConfigBuilder;
import com.wingsweaver.kuja.common.webmvc.common.constants.KujaCommonWebMvcOrders;
import org.springframework.core.Ordered;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用于设置动态日志上下文的 {@link OncePerRequestFilter} 实现类。
 *
 * @author wingsweaver
 */
public class DynamicLogContextFilter extends GenericFilterBean implements Ordered {
    @Override
    public int getOrder() {
        return KujaCommonWebMvcOrders.DYNAMIC_LOG_CONTEXT_FILTER;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        LogContext.Config config = LogContextConfigBuilder.build(servletRequest::getHeader);
        try (LogContext.TempHolder ignored = LogContext.with(config)) {
            chain.doFilter(request, response);
        }
    }
}
