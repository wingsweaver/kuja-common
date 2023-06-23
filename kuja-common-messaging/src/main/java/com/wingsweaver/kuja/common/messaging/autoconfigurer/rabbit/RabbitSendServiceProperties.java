package com.wingsweaver.kuja.common.messaging.autoconfigurer.rabbit;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.messaging.MessagingTemplateCommonSendServiceProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 适用于 RabbitMQ 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingKeys.PREFIX_RABBIT_SEND_SERVICE)
public class RabbitSendServiceProperties extends MessagingTemplateCommonSendServiceProperties {
    /**
     * 默认的 routingKey。
     */
    private String defaultRoutingKey;
}
