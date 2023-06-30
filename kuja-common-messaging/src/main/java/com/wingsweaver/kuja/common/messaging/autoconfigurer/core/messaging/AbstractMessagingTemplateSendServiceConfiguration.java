package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.messaging;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.common.AbstractSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.core.send.messaging.AbstractMessagingTemplateSendService;
import com.wingsweaver.kuja.common.messaging.core.send.messaging.MessageSendingTemplateOptions;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;

/**
 * 适用于 {@link AbstractMessagingTemplateSendService} 的消息发送配置。
 *
 * @author wingsweaver
 */
public abstract class AbstractMessagingTemplateSendServiceConfiguration extends AbstractSendServiceConfiguration {
    /**
     * 配置 {@link AbstractMessagingTemplateSendService} 实例。
     *
     * @param sendService AbstractMessagingTemplateSendService 实例
     * @param properties  配置属性
     * @param <D>         消息目的地类型
     * @param <T>         消息发送模板类型
     * @param <O>         消息发送模板配置类型
     */
    protected <D, T extends AbstractMessageSendingTemplate<D>, O extends MessageSendingTemplateOptions> void configureSendService(
            AbstractMessagingTemplateSendService<D, T, O> sendService, MessagingTemplateCommonSendServiceProperties properties) {
        super.configureSendService(sendService, properties);

        // 设置 MessageTemplate
        if (sendService.getMessagingTemplate() == null) {
            String messageTemplate = properties.getMessageTemplate();
            if (StringUtil.isNotEmpty(messageTemplate)) {
                sendService.setMessagingTemplate(this.getBean(messageTemplate, sendService.getMessagingTemplateType(), true));
            }
        }
    }

}
