package com.wingsweaver.kuja.common.web.errorhandling;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 kuja-common-web 错误处理的注解。
 *
 * @author wingsweaver
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(KujaCommonWebErrorHandlingConfiguration.class)
public @interface EnableKujaCommonWebErrorHandling {
}
