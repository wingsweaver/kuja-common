package com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.send;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.KujaBroadcastMessageProperties;
import com.wingsweaver.kuja.common.messaging.broadcast.send.BroadMessageDestinationResolver;
import com.wingsweaver.kuja.common.messaging.broadcast.send.BroadcastMessagePayloadResolver;
import com.wingsweaver.kuja.common.messaging.common.SenderInfoResolver;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * kuja-common-messaging 的广播机制的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KujaBroadcastMessageProperties.class)
public class KujaCommonMessagingBroadcastSendConfiguration extends AbstractConfiguration {
    @Bean
    public BroadcastMessagePayloadResolver kujaBroadcastMessagePayloadResolver(
            StringIdGenerator stringIdGenerator,
            SenderInfoResolver senderInfoResolver) {
        BroadcastMessagePayloadResolver resolver = new BroadcastMessagePayloadResolver();
        resolver.setIdGenerator(stringIdGenerator);
        resolver.setSenderInfoResolver(senderInfoResolver);
        return resolver;
    }

    @Bean
    public BroadMessageDestinationResolver kujaBroadMessageDestinationResolver(KujaBroadcastMessageProperties properties) {
        BroadMessageDestinationResolver resolver = new BroadMessageDestinationResolver();
        resolver.setDefaultDestination(properties.getDefaultDestination());
        return resolver;
    }
}
