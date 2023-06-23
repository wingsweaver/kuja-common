package com.wingsweaver.kuja.common.utils.support.tostring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link ToStringConverter} 的注解定义。
 *
 * @author wingsweaver
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ToStringWith {
    /**
     * 指定 {@link ToStringWithConverter} 的类型。
     *
     * @return {@link ToStringWithConverter} 的类型
     */
    Class<? extends ToStringWithConverter>[] value() default {};
}
