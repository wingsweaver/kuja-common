package com.wingsweaver.kuja.common.messaging;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.jms.JmsJakartaMessageSendServiceConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * kuja-common-messaging 的自动配置。
 *
 * @author wingsweaver
 */
@EnableKujaCommonMessaging
@AutoConfiguration
@Import(JmsJakartaMessageSendServiceConfiguration.class)
public class KujaCommonMessagingConfigurationJakarta extends AbstractConfiguration {
}
