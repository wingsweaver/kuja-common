package com.wingsweaver.kuja.common.web.wrapper;

import java.util.Collection;

/**
 * Header 的包装器。
 *
 * @author wingsweaver
 */
public interface WebHeadersWriter extends WebHeadersReader {
    /**
     * 设置指定 Header 的值。
     *
     * @param name  Header 名称
     * @param value Header 值
     */
    void setHeader(String name, String value);

    /**
     * 设置指定 Header 的值（多值）。
     *
     * @param name   Header 名称
     * @param values Header 值
     */
    void setHeaders(String name, Collection<String> values);

    /**
     * 添加指定 Header 的值。
     *
     * @param name  Header 名称
     * @param value Header 值
     */
    void addHeader(String name, String value);

    /**
     * 添加指定 Header 的值（多值）。
     *
     * @param name   Header 名称
     * @param values Header 值
     */
    void addHeaders(String name, Collection<String> values);

    /**
     * 删除指定的 Header。
     *
     * @param name Header 名称
     */
    void removeHeader(String name);
}
