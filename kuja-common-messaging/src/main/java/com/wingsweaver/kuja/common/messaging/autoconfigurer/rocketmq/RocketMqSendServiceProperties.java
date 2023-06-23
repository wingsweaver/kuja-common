package com.wingsweaver.kuja.common.messaging.autoconfigurer.rocketmq;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.messaging.MessagingTemplateCommonSendServiceProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 适用于 RocketMQ 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingKeys.PREFIX_ROCKETMQ_SEND_SERVICE)
public class RocketMqSendServiceProperties extends MessagingTemplateCommonSendServiceProperties {
    /**
     * 是否是异步模式。
     */
    private boolean asyncMode;
}
