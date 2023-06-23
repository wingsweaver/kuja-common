package com.wingsweaver.kuja.common.webflux.context;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.web.context.WebContextAccessor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

/**
 * 面向 Servlet (J2EE) 处理的 {@link WebContextAccessor} 子类。
 *
 * @author wingsweaver
 */
public class WebFluxContextAccessor extends WebContextAccessor {
    public WebFluxContextAccessor(BusinessContext context) {
        super(context);
    }

    public static final String KEY_SERVER_WEB_EXCHANGE = ClassUtil.resolveKey(ServerWebExchange.class);

    public ServerWebExchange getServerWebExchange() {
        return this.getAttribute(KEY_SERVER_WEB_EXCHANGE);
    }

    public void setServerWebExchange(ServerWebExchange serverWebExchange) {
        this.setAttribute(KEY_SERVER_WEB_EXCHANGE, serverWebExchange);
    }

    public static final String KEY_SERVER_HTTP_REQUEST = ClassUtil.resolveKey(ServerHttpRequest.class);

    public ServerHttpRequest getServerHttpRequest() {
        return this.getAttribute(KEY_SERVER_HTTP_REQUEST);
    }

    public void setServerHttpRequest(ServerHttpRequest serverHttpRequest) {
        this.setAttribute(KEY_SERVER_HTTP_REQUEST, serverHttpRequest);
    }

    public static final String KEY_SERVER_HTTP_RESPONSE = ClassUtil.resolveKey(ServerHttpResponse.class);

    public ServerHttpResponse getServerHttpResponse() {
        return this.getAttribute(KEY_SERVER_HTTP_RESPONSE);
    }

    public void setServerHttpResponse(ServerHttpResponse serverHttpResponse) {
        this.setAttribute(KEY_SERVER_HTTP_RESPONSE, serverHttpResponse);
    }
}
