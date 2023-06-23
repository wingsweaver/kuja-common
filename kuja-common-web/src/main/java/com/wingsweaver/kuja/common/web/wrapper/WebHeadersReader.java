package com.wingsweaver.kuja.common.web.wrapper;

import java.util.Collection;

/**
 * Header 的包装器。
 *
 * @author wingsweaver
 */
public interface WebHeadersReader {
    /**
     * 获取所有的 Header 的名称。
     *
     * @return 所有的 Header 的名称
     */
    Collection<String> getHeaderNames();

    /**
     * 获取指定 Header 的值。
     *
     * @param name Header 名称
     * @return 指定 Header 的值
     */
    String getHeader(String name);

    /**
     * 获取指定 Header 的值，如果不存在则返回默认值。
     *
     * @param name         Header 名称
     * @param defaultValue 默认值
     * @return 指定 Header 的值
     */
    String getHeader(String name, String defaultValue);

    /**
     * 设置指定 Header 的值（多值）。
     *
     * @param name Header 名称
     * @return 指定 Header 的值
     */
    Collection<String> getHeaders(String name);
}
