package com.wingsweaver.kuja.common.messaging.core.send.rabbit;

import com.wingsweaver.kuja.common.messaging.core.send.messaging.MessageSendingTemplateOptions;
import lombok.Getter;
import lombok.Setter;

/**
 * 适用于 {@link RabbitMessagingTemplateSendService} 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class RabbitMessageSendOptions extends MessageSendingTemplateOptions {
    /**
     * Rabbit Exchange 的路由 Key。
     */
    private String routingKey;
}
