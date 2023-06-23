package com.wingsweaver.kuja.common.boot.include;

/**
 * 是否包含某数据的设置。
 *
 * @author wingsweaver
 */
public enum IncludeAttribute {
    /**
     * 不包含。
     */
    NEVER,

    /**
     * 包含。
     */
    ALWAYS,

    /**
     * 错误时包含。
     */
    ON_ERROR,

    /**
     * 根据属性的值。
     */
    ON_ATTRIBUTE
}
