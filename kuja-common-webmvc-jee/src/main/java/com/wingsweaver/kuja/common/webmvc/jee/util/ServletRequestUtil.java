package com.wingsweaver.kuja.common.webmvc.jee.util;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

/**
 * {@link ServletRequest} 辅助工具类。
 *
 * @author wingsweaver
 */
public final class ServletRequestUtil {
    private ServletRequestUtil() {
        // 禁止实例化
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(ServletRequest request, String key) {
        return (T) request.getAttribute(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(ServletRequest request, String key, T defaultValue) {
        Object value = request.getAttribute(key);
        return (value != null) ? (T) value : defaultValue;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(ServletRequest request, String key, Supplier<T> defaultValueSupplier) {
        Object value = request.getAttribute(key);
        return (value != null) ? (T) value : defaultValueSupplier.get();
    }

    public static <T> T setAttribute(ServletRequest request, String key, Object value) {
        T oldValue = getAttribute(request, key);
        if (value == null) {
            request.removeAttribute(key);
        } else {
            request.setAttribute(key, value);
        }
        return oldValue;
    }

    /**
     * 移除 {@link ServletRequest} 中的属性，并返回移除前的值。
     *
     * @param request ServletRequest 实例
     * @param key     属性名
     * @param <T>     属性值类型
     * @return 移除前的值
     */
    public static <T> T removeAttribute(ServletRequest request, String key) {
        T value = getAttribute(request, key);
        request.removeAttribute(key);
        return value;
    }

    public static final String KEY_BUSINESS_CONTEXT = ClassUtil.resolveKey(BusinessContext.class);

    public static BusinessContext getBusinessContext(ServletRequest request) {
        return getAttribute(request, KEY_BUSINESS_CONTEXT);
    }

    public static BusinessContext setBusinessContext(ServletRequest request, BusinessContext businessContext) {
        return setAttribute(request, KEY_BUSINESS_CONTEXT, businessContext);
    }

    /**
     * 移除请求中关联的业务上下文。
     *
     * @param request ServletRequest 实例
     * @return 移除前的业务上下文
     */
    public static BusinessContext removeBusinessContext(ServletRequest request) {
        return removeAttribute(request, KEY_BUSINESS_CONTEXT);
    }

    public static final String KEY_FULL_PATH = ClassUtil.resolveKey(ServletRequestUtil.class, "full-path");

    /**
     * 解析请求的完整路径。
     *
     * @param request ServletRequest 实例
     * @return 请求的完整路径
     */
    public static String resolveFullPath(ServletRequest request) {
        String fullPath = getAttribute(request, KEY_FULL_PATH);
        if (StringUtil.isEmpty(fullPath) && request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            fullPath = httpServletRequest.getRequestURI();
            if (StringUtil.isNotEmpty(httpServletRequest.getQueryString())) {
                fullPath += "?" + httpServletRequest.getQueryString();
            }
            setAttribute(request, KEY_FULL_PATH, fullPath);
        }
        return fullPath;
    }

    public static String getErrorRequestUri(ServletRequest request) {
        return (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
    }
}
