package com.wingsweaver.kuja.common.web.wrapper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 跟实际的 Web 响应无关的、简易版的 {@link WebResponseWrapper} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MockWebResponseWrapper implements WebResponseWrapper {
    /**
     * 原始的响应实例。
     */
    private Object originalResponse;

    /**
     * Http Header 字典。
     */
    private final HttpHeaders headers = new HttpHeaders();

    /**
     * 响应状态码。
     */
    private Integer statusCode = HttpStatus.OK.value();

    @Override
    public Collection<String> getHeaderNames() {
        return this.headers.keySet();
    }

    @Override
    public String getHeader(String name) {
        return this.headers.getFirst(name);
    }

    @Override
    public String getHeader(String name, String defaultValue) {
        String value = this.headers.getFirst(name);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return this.headers.get(name);
    }

    @Override
    public void setHeader(String name, String value) {
        this.headers.set(name, value);
    }

    @Override
    public void setHeaders(String name, Collection<String> values) {
        if (values instanceof List) {
            this.headers.put(name, (List<String>) values);
        } else {
            this.headers.put(name, new LinkedList<>(values));
        }
    }

    @Override
    public void addHeader(String name, String value) {
        this.headers.add(name, value);
    }

    @Override
    public void addHeaders(String name, Collection<String> values) {
        if (values instanceof List) {
            this.headers.addAll(name, (List<String>) values);
        } else {
            this.headers.addAll(name, new LinkedList<>(values));
        }
    }

    @Override
    public void removeHeader(String name) {
        this.headers.remove(name);
    }
}
