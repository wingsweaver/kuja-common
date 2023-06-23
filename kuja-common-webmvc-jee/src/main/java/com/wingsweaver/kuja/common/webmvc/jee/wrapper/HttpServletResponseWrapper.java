package com.wingsweaver.kuja.common.webmvc.jee.wrapper;

import com.wingsweaver.kuja.common.web.wrapper.WebResponseWrapper;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * {@link HttpServletResponse} 的包装类。
 *
 * @author wingsweaver
 */
public class HttpServletResponseWrapper implements WebResponseWrapper {
    private final HttpServletResponse originalResponse;

    public HttpServletResponseWrapper(HttpServletResponse originalResponse) {
        this.originalResponse = originalResponse;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return this.originalResponse.getHeaderNames();
    }

    @Override
    public String getHeader(String name) {
        return this.originalResponse.getHeader(name);
    }

    @Override
    public String getHeader(String name, String defaultValue) {
        String value = this.getHeader(name);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return this.originalResponse.getHeaders(name);
    }

    @Override
    public void setHeader(String name, String value) {
        this.originalResponse.setHeader(name, value);
    }

    @Override
    public void setHeaders(String name, Collection<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            this.originalResponse.setHeader(name, null);
        } else {
            int index = 0;
            for (String value : values) {
                if (index == 0) {
                    this.originalResponse.setHeader(name, value);
                } else {
                    this.originalResponse.addHeader(name, value);
                }
                index++;
            }
        }
    }

    @Override
    public void addHeader(String name, String value) {
        this.originalResponse.addHeader(name, value);
    }

    @Override
    public void addHeaders(String name, Collection<String> values) {
        if (values != null) {
            for (String value : values) {
                this.originalResponse.addHeader(name, value);
            }
        }
    }

    @Override
    public void removeHeader(String name) {
        this.originalResponse.setHeader(name, null);
    }

    @Override
    public Object getOriginalResponse() {
        return this.originalResponse;
    }

    @Override
    public Integer getStatusCode() {
        return this.originalResponse.getStatus();
    }

    @Override
    public void setStatusCode(Integer statusCode) {
        if (statusCode != null) {
            this.originalResponse.setStatus(statusCode);
        }
    }
}
