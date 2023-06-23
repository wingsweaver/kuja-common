package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.cloud.serviceregistry.zookeeper.ZooKeeperServiceRegistryConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 kuja-common-cloud 中服务注册相关功能的注解。
 *
 * @author wingsweaver
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({KujaCloudServiceRegistrationConfiguration.class, ZooKeeperServiceRegistryConfiguration.class})
public @interface EnableKujaCloudServiceRegistration {
}