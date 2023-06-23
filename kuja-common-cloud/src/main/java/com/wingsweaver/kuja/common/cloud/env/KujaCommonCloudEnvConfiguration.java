package com.wingsweaver.kuja.common.cloud.env;

import com.wingsweaver.kuja.common.boot.env.postprocessor.InheritEnvironmentSettings;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * kuja-common-cloud 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(InheritEnvironmentSettings.class)
public class KujaCommonCloudEnvConfiguration extends AbstractConfiguration {
}
