package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.rocketmq;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.messaging.AbstractMessagingTemplateSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.core.send.rocketmq.RocketMqTemplateSendService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ 消息发送服务的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.apache.rocketmq.spring.core.RocketMQTemplate")
@EnableConfigurationProperties(RocketMqSendServiceProperties.class)
@AutoConfigureAfter(name = "org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration")
public class RocketMqMessageSendServiceConfiguration extends AbstractMessagingTemplateSendServiceConfiguration {
    /**
     * 配置 RocketMqTemplateSendService 实例。
     *
     * @param properties 配置属性
     * @return RocketMqTemplateSendService 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_JMS_SEND_SERVICE,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public RocketMqTemplateSendService rocketMqTemplateSendService(RocketMqSendServiceProperties properties) {
        RocketMqTemplateSendService sendService = new RocketMqTemplateSendService();
        super.configureSendService(sendService, properties);

        // 设置 asyncMode
        sendService.setAsyncMode(properties.isAsyncMode());

        // 返回
        return sendService;
    }
}
