package com.wingsweaver.kuja.common.boot.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于校验 Spring Boot 2.x 的条件注解。
 *
 * @author wingsweaver
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnSpringBootVersion(SpringBootVersionUtil.V2)
public @interface ConditionalOnSpringBootVersion2x {
}
