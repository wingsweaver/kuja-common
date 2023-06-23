package com.wingsweaver.kuja.common.boot.condition;

import com.vdurmont.semver4j.Semver;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于 Spring Boot 版本的条件检测注解。
 *
 * @author wingsweaver
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnSpringBootVersionCondition.class)
public @interface ConditionalOnSpringBootVersion {
    /**
     * 版本区间。
     *
     * @return 版本区间
     */
    String value();

    /**
     * 版本区间的指定方式。
     *
     * @return 版本区间的指定方式
     */
    Semver.SemverType type() default Semver.SemverType.NPM;
}
