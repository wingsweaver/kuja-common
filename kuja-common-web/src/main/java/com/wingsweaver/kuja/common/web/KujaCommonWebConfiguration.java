package com.wingsweaver.kuja.common.web;

import com.wingsweaver.kuja.common.boot.EnableKujaCommonBoot;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.web.common.WebServerProperties;
import com.wingsweaver.kuja.common.web.errorhandling.EnableKujaCommonWebErrorHandling;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * kuja-common-web 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({WebServerProperties.class})
@EnableKujaCommonBoot
@EnableKujaCommonWebErrorHandling
public class KujaCommonWebConfiguration extends AbstractConfiguration {
}
