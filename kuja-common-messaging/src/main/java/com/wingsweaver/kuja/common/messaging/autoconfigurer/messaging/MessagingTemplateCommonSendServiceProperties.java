package com.wingsweaver.kuja.common.messaging.autoconfigurer.messaging;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.common.CommonSendServiceProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 适用于 {@link com.wingsweaver.kuja.common.messaging.core.send.messaging.AbstractMessagingTemplateSendService} 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MessagingTemplateCommonSendServiceProperties extends CommonSendServiceProperties {
    /**
     * 消息模板 Bean 的名称。
     */
    private String messageTemplate;
}
