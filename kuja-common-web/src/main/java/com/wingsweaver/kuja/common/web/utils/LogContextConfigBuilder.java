package com.wingsweaver.kuja.common.web.utils;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogContext;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.web.context.WebContextAccessor;
import com.wingsweaver.kuja.common.web.wrapper.WebRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;

import java.util.function.Function;

/**
 * 从 Web 请求中构建 {@link LogContext.Config} 实例的工具类。
 *
 * @author wingsweaver
 */
public final class LogContextConfigBuilder {
    private LogContextConfigBuilder() {
        // 禁止实例化
    }

    public static final String HEADER_LOGGER = "X-LOGGING-LOGGER";

    public static final String HEADER_MARKER = "X-LOGGING-MARKER";

    public static final String HEADER_LEVEL = "X-LOGGING-LEVEL";

    /**
     * 从 Web 请求中构建 {@link LogContext.Config} 实例。
     *
     * @param businessContext 业务上下文
     * @return {@link LogContext.Config} 实例
     */
    public static LogContext.Config build(BusinessContext businessContext) {
        if (businessContext == null) {
            return null;
        } else {
            return build(new WebContextAccessor(businessContext).getRequestWrapper());
        }
    }

    /**
     * 从 Web 请求中构建 {@link LogContext.Config} 实例。
     *
     * @param requestWrapper Web 请求包装器
     * @return {@link LogContext.Config} 实例
     */
    public static LogContext.Config build(WebRequestWrapper requestWrapper) {
        if (requestWrapper == null) {
            return null;
        } else {
            return build(requestWrapper::getHeader);
        }
    }

    /**
     * 从 Web 请求中构建 {@link LogContext.Config} 实例。
     *
     * @param attributeResolver 属性解析处理
     * @return {@link LogContext.Config} 实例
     */
    public static LogContext.Config build(Function<String, String> attributeResolver) {
        if (attributeResolver == null) {
            return null;
        }

        Logger logger = resolveLogger(attributeResolver);
        Marker marker = resolveMarker(attributeResolver);
        Level level = resolveLevel(attributeResolver);
        if (logger == null && marker == null && level == null) {
            return null;
        } else {
            return new LogContext.Config(logger, marker, level);
        }
    }

    static Logger resolveLogger(Function<String, String> attributeResolver) {
        String logger = StringUtil.trimToNull(attributeResolver.apply(HEADER_LOGGER));
        return logger != null ? LoggerFactory.getLogger(logger) : null;
    }

    static Marker resolveMarker(Function<String, String> attributeResolver) {
        String marker = StringUtil.trimToNull(attributeResolver.apply(HEADER_MARKER));
        return marker != null ? MarkerFactory.getMarker(marker) : null;
    }

    static Level resolveLevel(Function<String, String> attributeResolver) {
        String level = StringUtil.trimToNull(attributeResolver.apply(HEADER_LEVEL));
        return level != null ? Level.valueOf(level.toUpperCase()) : null;
    }
}
