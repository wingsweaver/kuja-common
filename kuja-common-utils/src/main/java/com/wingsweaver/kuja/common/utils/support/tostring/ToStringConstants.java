package com.wingsweaver.kuja.common.utils.support.tostring;

/**
 * 字符串转换过程中使用到的常量。
 *
 * @author wingsweaver
 */
public interface ToStringConstants {
    /**
     * 字段的连接符。
     */
    String TOKEN_DELIMITER = ", ";

    /**
     * 对象或者字典元素的开始。
     */
    String TOKEN_OBJECT_START = "{";

    /**
     * 对象或者字典元素的结束。
     */
    String TOKEN_OBJECT_END = "}";

    /**
     * 数组或者集合元素的开始。
     */
    String TOKEN_ARRAY_START = "[";

    /**
     * 数组或者集合元素的结束。
     */
    String TOKEN_ARRAY_END = "]";

    /**
     * 字段名和字段值的连接符。
     */
    String TOKEN_VALUE = ": ";

    /**
     * 省略号。
     */
    String TOKEN_OMIT = "...";

    /**
     * null 数据。
     */
    String VALUE_NULL = "null";
}
