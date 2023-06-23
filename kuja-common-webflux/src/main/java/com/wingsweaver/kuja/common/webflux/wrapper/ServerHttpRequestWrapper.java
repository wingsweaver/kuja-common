package com.wingsweaver.kuja.common.webflux.wrapper;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.web.wrapper.HostInfo;
import com.wingsweaver.kuja.common.web.wrapper.WebRequestWrapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;

/**
 * {@link ServerHttpRequest} 的包装类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ServerHttpRequestWrapper implements WebRequestWrapper {
    /**
     * {@link ServerWebExchange} 实例。
     */
    private final ServerWebExchange exchange;

    /**
     * {@link ServerHttpRequest} 实例。
     */
    private final ServerHttpRequest request;

    public ServerHttpRequestWrapper(ServerWebExchange exchange, ServerHttpRequest request) {
        this.exchange = exchange;
        this.request = request;
    }

    @Override
    public Collection<String> getAttributeNames() {
        return new ArrayList<>(this.exchange.getAttributes().keySet());
    }

    @Override
    public <T> T getAttribute(String name) {
        return this.exchange.getAttribute(name);
    }

    @Override
    public <T> T getAttribute(String name, T defaultValue) {
        return this.exchange.getAttributeOrDefault(name, defaultValue);
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.exchange.getAttributes().put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        this.exchange.getAttributes().remove(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return new ArrayList<>(this.request.getHeaders().keySet());
    }

    @Override
    public String getHeader(String name) {
        return this.request.getHeaders().getFirst(name);
    }

    @Override
    public String getHeader(String name, String defaultValue) {
        String value = this.request.getHeaders().getFirst(name);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return this.request.getHeaders().get(name);
    }

    @Override
    public Collection<String> getParameterNames() {
        return new ArrayList<>(this.request.getQueryParams().keySet());
    }

    @Override
    public String getParameter(String name) {
        return this.request.getQueryParams().getFirst(name);
    }

    @Override
    public String getParameter(String name, String defaultValue) {
        String value = this.request.getQueryParams().getFirst(name);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public Collection<String> getParameters(String name) {
        return this.request.getQueryParams().get(name);
    }

    @Override
    public Object getOriginalRequest() {
        return this.request;
    }

    @Override
    public String getScheme() {
        return this.request.getURI().getScheme();
    }

    @Override
    public String getMethod() {
        return this.request.getMethodValue();
    }

    @Override
    public String getRequestPath() {
        return this.request.getURI().getPath();
    }

    @Override
    public String getQueryString() {
        return this.request.getURI().getQuery();
    }

    @Override
    public String getFullRequestPath() {
        String uri = this.getRequestPath();
        String queryString = this.getQueryString();
        if (StringUtil.isEmpty(queryString)) {
            return uri;
        } else {
            return uri + "?" + queryString;
        }
    }

    @Override
    public boolean hasParts() {
        try {
            return this.exchange.getMultipartData()
                    .map(multipartData -> !multipartData.isEmpty())
                    .switchIfEmpty(Mono.just(false))
                    .toFuture().get();
        } catch (Exception ex) {
            throw new ExtendedRuntimeException(ex)
                    .withExtendedAttribute("exchange", this.exchange)
                    .withExtendedAttribute("request", this.request);
        }
    }

    @Override
    public HostInfo getLocalHost() {
        return this.toHostInfo(this.request.getLocalAddress());
    }

    @Override
    public HostInfo getRemoteHost() {
        return this.toHostInfo(this.request.getRemoteAddress());
    }

    private HostInfo toHostInfo(InetSocketAddress inetAddress) {
        if (inetAddress == null) {
            return null;
        }
        HostInfo hostInfo = new HostInfo();
        hostInfo.setName(inetAddress.getHostName());
        hostInfo.setAddress(inetAddress.getAddress().getHostAddress());
        hostInfo.setPort(inetAddress.getPort());
        return hostInfo;
    }
}
