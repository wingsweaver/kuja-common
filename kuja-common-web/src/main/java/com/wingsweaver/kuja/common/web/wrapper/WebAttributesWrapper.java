package com.wingsweaver.kuja.common.web.wrapper;

import java.util.Collection;

/**
 * 属性的包装器。
 *
 * @author wingsweaver
 */
public interface WebAttributesWrapper {
    /**
     * 获取所有的属性的名称。
     *
     * @return 所有的属性的名称
     */
    Collection<String> getAttributeNames();

    /**
     * 获取指定名称的属性。
     *
     * @param name 属性名称
     * @param <T>  属性类型
     * @return 指定名称的属性
     */
    <T> T getAttribute(String name);

    /**
     * 获取指定名称的属性，如果不存在则返回默认值。
     *
     * @param name         属性名称
     * @param defaultValue 默认值
     * @param <T>          属性类型
     * @return 指定名称的属性
     */
    <T> T getAttribute(String name, T defaultValue);

    /**
     * 设置指定属性的值。
     *
     * @param name  属性名称
     * @param value 属性值
     */
    void setAttribute(String name, Object value);

    /**
     * 移除指定名称的属性。
     *
     * @param name 属性名称
     */
    void removeAttribute(String name);
}
