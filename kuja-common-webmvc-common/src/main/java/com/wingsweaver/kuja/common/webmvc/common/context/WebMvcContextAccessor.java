package com.wingsweaver.kuja.common.webmvc.common.context;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.web.context.WebContextAccessor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.context.request.WebRequest;

/**
 * WebMvc 通用的 {@link WebContextAccessor} 实现。
 *
 * @author wingsweaver
 */
public class WebMvcContextAccessor extends WebContextAccessor {
    public WebMvcContextAccessor(BusinessContext context) {
        super(context);
    }

    public static final String KEY_SERVER_HTTP_REQUEST = ClassUtil.resolveKey(ServerHttpRequest.class);

    public ServerHttpRequest getServerHttpRequest() {
        return getAttribute(KEY_SERVER_HTTP_REQUEST);
    }

    public void setServerHttpRequest(ServerHttpRequest serverHttpRequest) {
        setAttribute(KEY_SERVER_HTTP_REQUEST, serverHttpRequest);
    }

    public static final String KEY_SERVER_HTTP_RESPONSE = ClassUtil.resolveKey(ServerHttpResponse.class);

    public ServerHttpResponse getServerHttpResponse() {
        return getAttribute(KEY_SERVER_HTTP_RESPONSE);
    }

    public void setServerHttpResponse(ServerHttpResponse serverHttpResponse) {
        setAttribute(KEY_SERVER_HTTP_RESPONSE, serverHttpResponse);
    }

    public static final String KEY_WEB_REQUEST = ClassUtil.resolveKey(WebRequest.class);

    public WebRequest getWebRequest() {
        return getAttribute(KEY_WEB_REQUEST);
    }

    public void setWebRequest(WebRequest webRequest) {
        setAttribute(KEY_WEB_REQUEST, webRequest);
    }
}
