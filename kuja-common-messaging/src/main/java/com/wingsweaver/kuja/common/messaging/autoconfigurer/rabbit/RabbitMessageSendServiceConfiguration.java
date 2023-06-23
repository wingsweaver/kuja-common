package com.wingsweaver.kuja.common.messaging.autoconfigurer.rabbit;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.messaging.AbstractMessagingTemplateSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.core.send.rabbit.RabbitMessagingTemplateSendService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbit 消息发送服务的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.springframework.amqp.rabbit.core.RabbitMessagingTemplate")
@AutoConfigureAfter(RabbitAutoConfiguration.class)
@EnableConfigurationProperties(RabbitSendServiceProperties.class)
public class RabbitMessageSendServiceConfiguration extends AbstractMessagingTemplateSendServiceConfiguration {
    /**
     * 配置 RabbitMessagingTemplateSendService 实例。
     *
     * @param properties 配置属性
     * @return RabbitMessagingTemplateSendService 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_RABBIT_SEND_SERVICE,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public RabbitMessagingTemplateSendService rabbitMessagingTemplateSendService(RabbitSendServiceProperties properties) {
        RabbitMessagingTemplateSendService sendService = new RabbitMessagingTemplateSendService();
        super.configureSendService(sendService, properties);

        // 设置 defaultRoutingKey
        sendService.setDefaultRoutingKey(properties.getDefaultRoutingKey());

        // 返回
        return sendService;
    }
}
