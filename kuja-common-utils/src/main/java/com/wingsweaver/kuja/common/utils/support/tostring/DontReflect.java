package com.wingsweaver.kuja.common.utils.support.tostring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于忽略指定数据的注入解析的注解。
 *
 * @author wingsweaver
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ToStringWith(MaskedConverter.class)
public @interface DontReflect {
}
