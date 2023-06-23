package com.wingsweaver.kuja.common.web.wrapper;

import java.util.Collection;

/**
 * Web 参数的包装器。
 *
 * @author wingsweaver
 */
public interface WebParametersWrapper {
    /**
     * 获取所有的 Parameter 的名称。
     *
     * @return 所有的 Parameter 的名称
     */
    Collection<String> getParameterNames();

    /**
     * 获取指定 Parameter 的值。
     *
     * @param name Parameter 名称
     * @return 指定 Parameter 的值
     */
    String getParameter(String name);

    /**
     * 获取指定 Parameter 的值，如果不存在则返回默认值。
     *
     * @param name         Parameter 名称
     * @param defaultValue 默认值
     * @return 指定 Parameter 的值
     */
    String getParameter(String name, String defaultValue);

    /**
     * 设置指定 Parameter 的值（多值）。
     *
     * @param name Parameter 名称
     * @return 指定 Parameter 的值
     */
    Collection<String> getParameters(String name);
}
