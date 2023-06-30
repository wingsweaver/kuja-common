package com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.receive.KujaCommonMessagingBroadcastReceiveConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.send.KujaCommonMessagingBroadcastSendConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.KujaCommonMessagingCoreConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * kuja-common-messaging 的广播机制的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_KUJA_COMMON_MESSAGING,
        name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({KujaCommonMessagingCoreConfiguration.class,
        KujaCommonMessagingBroadcastReceiveConfiguration.class,
        KujaCommonMessagingBroadcastSendConfiguration.class})
@EnableConfigurationProperties(KujaBroadcastMessageProperties.class)
public class KujaCommonMessagingBroadcastConfiguration extends AbstractConfiguration {
}
