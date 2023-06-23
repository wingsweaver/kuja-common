package com.wingsweaver.kuja.common.webmvc.common;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.web.EnableKujaCommonWeb;
import org.springframework.context.annotation.Configuration;

/**
 * kuja-common-webmvc-common 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableKujaCommonWeb
public class KujaCommonWebMvcCommonConfiguration extends AbstractConfiguration {
}
