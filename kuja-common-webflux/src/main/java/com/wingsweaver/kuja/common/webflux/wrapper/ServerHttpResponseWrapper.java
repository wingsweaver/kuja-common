package com.wingsweaver.kuja.common.webflux.wrapper;

import com.wingsweaver.kuja.common.web.wrapper.WebResponseWrapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.util.ArrayList;
import java.util.Collection;

/**
 * {@link ServerHttpResponse} 的包装类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ServerHttpResponseWrapper implements WebResponseWrapper {
    private final ServerHttpResponse response;

    public ServerHttpResponseWrapper(ServerHttpResponse response) {
        this.response = response;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return new ArrayList<>(this.response.getHeaders().keySet());
    }

    @Override
    public String getHeader(String name) {
        return this.response.getHeaders().getFirst(name);
    }

    @Override
    public String getHeader(String name, String defaultValue) {
        String value = this.response.getHeaders().getFirst(name);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return this.response.getHeaders().get(name);
    }

    @Override
    public void setHeader(String name, String value) {
        this.response.getHeaders().set(name, value);
    }

    @Override
    public void setHeaders(String name, Collection<String> values) {
        this.response.getHeaders().put(name, new ArrayList<>(values));
    }

    @Override
    public void addHeader(String name, String value) {
        this.response.getHeaders().add(name, value);
    }

    @Override
    public void addHeaders(String name, Collection<String> values) {
        this.response.getHeaders().addAll(name, new ArrayList<>(values));
    }

    @Override
    public void removeHeader(String name) {
        this.response.getHeaders().remove(name);
    }

    @Override
    public Object getOriginalResponse() {
        return this.response;
    }

    @Override
    public Integer getStatusCode() {
        return this.response.getRawStatusCode();
    }

    @Override
    public void setStatusCode(Integer statusCode) {
        if (statusCode != null) {
            this.response.setRawStatusCode(statusCode);
        }
    }
}
