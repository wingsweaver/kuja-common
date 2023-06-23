package com.wingsweaver.kuja.common.webmvc.jee.wrapper;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.web.wrapper.HostInfo;
import com.wingsweaver.kuja.common.web.wrapper.WebRequestWrapper;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * {@link HttpServletRequest} 的包装器。
 *
 * @author wingsweaver
 */
public class HttpServletRequestWrapper implements WebRequestWrapper {
    private final HttpServletRequest originalRequest;

    public HttpServletRequestWrapper(HttpServletRequest originalRequest) {
        this.originalRequest = originalRequest;
    }

    @Override
    public List<String> getAttributeNames() {
        return Collections.list(this.originalRequest.getAttributeNames());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String name) {
        return (T) this.originalRequest.getAttribute(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String name, T defaultValue) {
        Object value = this.originalRequest.getAttribute(name);
        return (value != null) ? (T) value : defaultValue;
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (value == null) {
            this.originalRequest.removeAttribute(name);
        } else {
            this.originalRequest.setAttribute(name, value);
        }
    }

    @Override
    public void removeAttribute(String name) {
        this.originalRequest.removeAttribute(name);
    }

    @Override
    public List<String> getHeaderNames() {
        return Collections.list(this.originalRequest.getHeaderNames());
    }

    @Override
    public String getHeader(String name) {
        return this.originalRequest.getHeader(name);
    }

    @Override
    public String getHeader(String name, String defaultValue) {
        String value = this.getHeader(name);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public List<String> getHeaders(String name) {
        return Collections.list(this.originalRequest.getHeaders(name));
    }

    @Override
    public List<String> getParameterNames() {
        return Collections.list(this.originalRequest.getParameterNames());
    }

    @Override
    public String getParameter(String name) {
        return this.originalRequest.getParameter(name);
    }

    @Override
    public String getParameter(String name, String defaultValue) {
        String value = this.getParameter(name);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public List<String> getParameters(String name) {
        String[] values = this.originalRequest.getParameterValues(name);
        return (values != null) ? Arrays.asList(values) : null;
    }

    @Override
    public Object getOriginalRequest() {
        return this.originalRequest;
    }

    @Override
    public String getScheme() {
        return this.originalRequest.getScheme();
    }

    @Override
    public String getMethod() {
        return this.originalRequest.getMethod();
    }

    @Override
    public String getRequestPath() {
        try {
            return new URI(this.originalRequest.getRequestURI()).getPath();
        } catch (URISyntaxException e) {
            throw new ExtendedRuntimeException(e)
                    .withExtendedAttribute("uri", this.originalRequest.getRequestURI());
        }
    }

    @Override
    public String getQueryString() {
        return this.originalRequest.getQueryString();
    }

    @Override
    public String getFullRequestPath() {
        String requestUri = this.getRequestPath();
        String queryString = this.getQueryString();
        if (StringUtil.isEmpty(queryString)) {
            return requestUri;
        } else {
            return requestUri + "?" + queryString;
        }
    }

    @Override
    public boolean hasParts() {
        try {
            return !CollectionUtils.isEmpty(this.originalRequest.getParts());
        } catch (IOException | ServletException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public HostInfo getLocalHost() {
        HostInfo hostInfo = new HostInfo();
        hostInfo.setName(this.originalRequest.getLocalName());
        hostInfo.setAddress(this.originalRequest.getLocalAddr());
        hostInfo.setPort(this.originalRequest.getLocalPort());
        return hostInfo;
    }

    @Override
    public HostInfo getRemoteHost() {
        HostInfo hostInfo = new HostInfo();
        hostInfo.setName(this.originalRequest.getRemoteHost());
        hostInfo.setAddress(this.originalRequest.getRemoteAddr());
        hostInfo.setPort(this.originalRequest.getRemotePort());
        return hostInfo;
    }
}
