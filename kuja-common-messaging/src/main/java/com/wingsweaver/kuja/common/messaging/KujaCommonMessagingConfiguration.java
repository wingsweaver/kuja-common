package com.wingsweaver.kuja.common.messaging;

import com.wingsweaver.kuja.common.boot.EnableKujaCommonBoot;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.KujaCommonMessagingBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * kuja-common-messaging 的自动配置。
 *
 * @author wingsweaver
 */
@EnableKujaCommonBoot
@Configuration(proxyBeanMethods = false)
@Import(KujaCommonMessagingBootConfiguration.class)
public class KujaCommonMessagingConfiguration extends AbstractConfiguration {
}
