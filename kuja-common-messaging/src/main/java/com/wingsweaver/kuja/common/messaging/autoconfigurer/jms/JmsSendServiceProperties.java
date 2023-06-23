package com.wingsweaver.kuja.common.messaging.autoconfigurer.jms;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.messaging.MessagingTemplateCommonSendServiceProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 适用于 JMS 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingKeys.PREFIX_JMS_SEND_SERVICE)
public class JmsSendServiceProperties extends MessagingTemplateCommonSendServiceProperties {
    /**
     * 是否从 bean 中解析消息目的地。
     */
    private boolean resolveDestinationFromBean = false;
}
