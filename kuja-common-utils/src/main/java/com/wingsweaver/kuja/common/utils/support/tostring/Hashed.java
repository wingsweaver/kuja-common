package com.wingsweaver.kuja.common.utils.support.tostring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于将指定数据进行 HASH 处理的注解。
 *
 * @author wingsweaver
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ToStringWith(HashedConverter.class)
public @interface Hashed {
    /**
     * 哈希算法。
     *
     * @return 哈希算法
     */
    String value() default HashedConfig.DEFAULT_ALGORITHM;

    /**
     * 编码格式。
     *
     * @return 输出格式
     */
    String codec() default HashedConfig.DEFAULT_CODEC;
}
