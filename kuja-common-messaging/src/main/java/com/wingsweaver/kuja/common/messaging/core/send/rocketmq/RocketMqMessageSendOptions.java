package com.wingsweaver.kuja.common.messaging.core.send.rocketmq;

import com.wingsweaver.kuja.common.messaging.core.send.messaging.MessageSendingTemplateOptions;
import lombok.Getter;
import lombok.Setter;

/**
 * 适用于 {@link RocketMqTemplateSendService} 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class RocketMqMessageSendOptions extends MessageSendingTemplateOptions {
    /**
     * 用于顺序发送的 HashKey。<br>
     * 不设置的话，则不使用顺序发送。
     */
    private String orderHashKey;

    /**
     * 发送超时。<br>
     * 不设置的话，使用 RocketMQ 的默认值。
     */
    private long timeout;
}
