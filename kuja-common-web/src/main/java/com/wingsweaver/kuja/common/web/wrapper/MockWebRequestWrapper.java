package com.wingsweaver.kuja.common.web.wrapper;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 跟实际的 Web 请求无关的、简易版的 {@link WebRequestWrapper} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MockWebRequestWrapper implements WebRequestWrapper {
    /**
     * 原始的请求实例。
     */
    private Object originalRequest;

    /**
     * 属性字典。
     */
    private final Map<String, Object> attributes = new HashMap<>(BufferSizes.SMALL);

    /**
     * Header 字典。
     */
    private final HttpHeaders headers = new HttpHeaders();

    /**
     * 参数字典。
     */
    private final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

    /**
     * Http Scheme。
     */
    private String scheme;

    /**
     * Http Method。
     */
    private String method;

    /**
     * 请求 URI。
     */
    private String requestPath;

    /**
     * 是否包含文件 Part。
     */
    @Getter(AccessLevel.NONE)
    private boolean hasParts;

    /**
     * 本地主机信息（Web 服务器的主机信息）。
     */
    private HostInfo localHost;

    /**
     * 远程主机信息（发起 Web 请求的主机信息）。
     */
    private HostInfo remoteHost;

    /**
     * 字符集。
     */
    private Charset charset = StandardCharsets.UTF_8;

    @Override
    public Collection<String> getAttributeNames() {
        return this.attributes.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String name) {
        return (T) this.attributes.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String name, T defaultValue) {
        return (T) this.attributes.getOrDefault(name, defaultValue);
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

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
    public Collection<String> getParameterNames() {
        return this.parameters.keySet();
    }

    @Override
    public String getParameter(String name) {
        return this.parameters.getFirst(name);
    }

    @Override
    public String getParameter(String name, String defaultValue) {
        String value = this.parameters.getFirst(name);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public Collection<String> getParameters(String name) {
        return this.parameters.get(name);
    }

    @Override
    public String getQueryString() {
        if (this.parameters.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        this.parameters.forEach((name, values) -> {
            for (String value : values) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(UriUtils.encode(name, this.charset));
                sb.append("=");
                sb.append(UriUtils.encode(value, this.charset));
            }
        });
        return sb.toString();
    }

    @Override
    public String getFullRequestPath() {
        String queryString = this.getQueryString();
        if (StringUtil.isEmpty(queryString)) {
            return this.requestPath;
        } else {
            return this.requestPath + "?" + queryString;
        }
    }

    @Override
    public boolean hasParts() {
        return this.hasParts;
    }
}
