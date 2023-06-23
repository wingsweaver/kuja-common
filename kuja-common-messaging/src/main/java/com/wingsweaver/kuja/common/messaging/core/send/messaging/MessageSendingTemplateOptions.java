package com.wingsweaver.kuja.common.messaging.core.send.messaging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.core.MessagePostProcessor;

import java.util.Map;

/**
 * 适用于 {@link AbstractMessagingTemplateSendService} 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MessageSendingTemplateOptions {
    /**
     * 消息内容。
     */
    private Object payload;

    /**
     * 消息目的地。<br>
     * 可以是 {@link String} 或者是 {@link javax.jms.Destination}。
     */
    private Object destination;

    /**
     * 消息头。
     */
    private Map<String, Object> headers;

    /**
     * 消息处理器。
     */
    private MessagePostProcessor postProcessor;
}
