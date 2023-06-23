package com.wingsweaver.kuja.common.web.wrapper;

/**
 * 响应的包装器。
 *
 * @author wingsweaver
 */
public interface WebResponseWrapper extends WebHeadersWriter {
    /**
     * 获取原始响应。
     *
     * @return 原始响应
     */
    Object getOriginalResponse();

    /**
     * 获取响应状态码。
     *
     * @return 状态码
     */
    Integer getStatusCode();

    /**
     * 设置返回状态码。
     *
     * @param statusCode 状态码
     */
    void setStatusCode(Integer statusCode);
}
