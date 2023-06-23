package com.wingsweaver.kuja.common.web.wrapper;

/**
 * 请求的包装器。
 *
 * @author wingsweaver
 */
public interface WebRequestWrapper extends WebAttributesWrapper, WebHeadersReader, WebParametersWrapper {
    /**
     * 获取原始请求。
     *
     * @return 原始请求
     */
    Object getOriginalRequest();

    /**
     * 获取 HTTP 请求的 SCHEME。
     *
     * @return HTTP 请求的 SCHEME
     */
    String getScheme();

    /**
     * 获取 HTTP 请求的方法。
     *
     * @return HTTP 请求的方法
     */
    String getMethod();

    /**
     * 获取 HTTP 请求的 Path。<br>
     * 即 {@link java.net.URI#getPath()} 的部分，不包括域名和端口。
     *
     * @return HTTP 请求的 URI
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    String getRequestPath();

    /**
     * 获取 HTTP 请求的参数。
     *
     * @return HTTP 请求的参数
     */
    String getQueryString();

    /**
     * 获取 HTTP 请求的完整路径。<br>
     * 包括请求参数，但是不包括域名和端口。
     *
     * @return HTTP 请求的完整路径
     */
    String getFullRequestPath();

    /**
     * 是否包含文件 Part。
     *
     * @return 是否包含文件 Part
     */
    boolean hasParts();

    /**
     * 获取本地主机信息。
     *
     * @return 本地主机信息
     */
    HostInfo getLocalHost();

    /**
     * 获取远程主机信息。
     *
     * @return 远程主机信息
     */
    HostInfo getRemoteHost();
}
