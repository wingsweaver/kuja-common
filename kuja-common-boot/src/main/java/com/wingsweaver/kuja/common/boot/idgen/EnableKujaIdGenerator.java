package com.wingsweaver.kuja.common.boot.idgen;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 Id Generator 功能的注解。
 *
 * @author wingsweaver
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LongIdGeneratorConfiguration.class, StringIdGeneratorConfiguration.class,
        WorkerIdResolverConfigurationJdbc.class, WorkerIdResolverConfigurationRedis.class,
        WorkerIdResolverConfigurationRedisson.class, WorkerIdResolverConfigurationFixed.class,
        WorkerIdResolverConfigurationLocalPid.class, WorkerIdResolverConfigurationFallback.class})
public @interface EnableKujaIdGenerator {
}
