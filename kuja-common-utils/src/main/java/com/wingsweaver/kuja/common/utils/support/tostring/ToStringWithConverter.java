package com.wingsweaver.kuja.common.utils.support.tostring;

import java.lang.reflect.AnnotatedElement;

/**
 * 基于指定注解、将指定实例转换成字符串的接口定义。<br>
 * 与 {@link ToStringWith} 注解配合使用。
 *
 * @author wingsweaver
 */
public interface ToStringWithConverter {
    /**
     * 基于指定注解、将指定实例转换成字符串。
     *
     * @param value            指定实例
     * @param annotatedElement 指定注解
     * @param builder          用于存储转换结果的 StringBuilder 实例
     * @param config           转换处理的配置
     * @return 是否完成转换
     */
    boolean toString(Object value, AnnotatedElement annotatedElement, StringBuilder builder, ToStringConfig config);
}
