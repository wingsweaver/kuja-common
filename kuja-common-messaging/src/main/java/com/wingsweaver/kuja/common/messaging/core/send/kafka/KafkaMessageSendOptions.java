package com.wingsweaver.kuja.common.messaging.core.send.kafka;

import lombok.Getter;
import lombok.Setter;

/**
 * 适用于 {@link KafkaTemplateSendService} 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class KafkaMessageSendOptions {
    /**
     * 消息主题。
     */
    private String topic;

    /**
     * 消息内容。
     */
    private Object payload;
}
