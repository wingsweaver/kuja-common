package com.wingsweaver.kuja.common.webmvc.jee.interceptor;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextAccessor;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.webmvc.jee.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于处理上下文的 {@link HandlerInterceptor} 实现。
 *
 * @author wingsweaver
 */
public class BusinessContextHandlerInterceptor extends AbstractComponent implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessContextHandlerInterceptor.class);

    public static final String KEY_PRE_HANDLED = ClassUtil.resolveKey(BusinessContextHandlerInterceptor.class, "preHandled");

    public static final String KEY_AFTER_COMPLETION = ClassUtil.resolveKey(BusinessContextHandlerInterceptor.class, "afterCompletion");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 检查是否执行过
        if (Boolean.TRUE.equals(request.getAttribute(KEY_PRE_HANDLED))) {
            LogUtil.trace(LOGGER, "Already pre handled");
            return true;
        }
        request.setAttribute(KEY_PRE_HANDLED, true);

        // 设置处理器
        BusinessContext businessContext = ServletRequestUtil.getBusinessContext(request);
        if (businessContext != null) {
            BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
            accessor.setHandlerIfAbsent(handler);
            LogUtil.trace(LOGGER, "Set handler to {}", handler);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 检查是否执行过
        if (Boolean.TRUE.equals(request.getAttribute(KEY_AFTER_COMPLETION))) {
            LogUtil.trace(LOGGER, "Already after completion");
            return;
        }
        request.setAttribute(KEY_AFTER_COMPLETION, true);

        // 设置错误
        BusinessContext businessContext = ServletRequestUtil.getBusinessContext(request);
        if (businessContext != null) {
            BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
            accessor.setHandlerIfAbsent(handler);
            accessor.setErrorIfAbsent(ex);
            if (ex != null) {
                LogUtil.trace(LOGGER, () -> "Set error to " + ex.getClass().getName() + "@" + Integer.toHexString(ex.hashCode()));
            }
        }
    }
}
