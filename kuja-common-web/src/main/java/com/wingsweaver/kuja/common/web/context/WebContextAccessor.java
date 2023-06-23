package com.wingsweaver.kuja.common.web.context;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.web.wrapper.WebRequestWrapper;
import com.wingsweaver.kuja.common.web.wrapper.WebResponseWrapper;

/**
 * 面向 Web 处理场景的 {@link BusinessContextAccessor} 子类。
 *
 * @author wingsweaver
 */
public class WebContextAccessor extends BusinessContextAccessor {
    public WebContextAccessor(BusinessContext context) {
        super(context);
    }

    /**
     * Key: 原始请求。
     */
    public static final String KEY_ORIGINAL_REQUEST = ClassUtil.resolveKey(WebContextAccessor.class, "originalRequest");

    public <T> T getOriginalRequest() {
        return getAttribute(KEY_ORIGINAL_REQUEST);
    }

    public void setOriginalRequest(Object originalRequest) {
        setAttribute(KEY_ORIGINAL_REQUEST, originalRequest);
    }

    /**
     * Key: 原始响应。
     */
    public static final String KEY_ORIGINAL_RESPONSE = ClassUtil.resolveKey(WebContextAccessor.class, "originalResponse");

    public <T> T getOriginalResponse() {
        return getAttribute(KEY_ORIGINAL_RESPONSE);
    }

    public void setOriginalResponse(Object originalResponse) {
        setAttribute(KEY_ORIGINAL_RESPONSE, originalResponse);
    }

    public static final String KEY_REQUEST_WRAPPER = ClassUtil.resolveKey(WebRequestWrapper.class);

    public WebRequestWrapper getRequestWrapper() {
        return getAttribute(KEY_REQUEST_WRAPPER);
    }

    public void setRequestWrapper(WebRequestWrapper webRequestWrapper) {
        setAttribute(KEY_REQUEST_WRAPPER, webRequestWrapper);
    }

    public static final String KEY_RESPONSE_WRAPPER = ClassUtil.resolveKey(WebResponseWrapper.class);

    public WebResponseWrapper getResponseWrapper() {
        return getAttribute(KEY_RESPONSE_WRAPPER);
    }

    public void setResponseWrapper(WebResponseWrapper webResponseWrapper) {
        setAttribute(KEY_RESPONSE_WRAPPER, webResponseWrapper);
    }

    public String getRequestUri() {
        WebRequestWrapper requestWrapper = this.getRequestWrapper();
        if (requestWrapper != null) {
            return requestWrapper.getRequestPath();
        } else {
            return null;
        }
    }

    public String getRequestFullPath() {
        WebRequestWrapper requestWrapper = this.getRequestWrapper();
        if (requestWrapper != null) {
            return requestWrapper.getFullRequestPath();
        } else {
            return null;
        }
    }
}
