package com.wingsweaver.kuja.common.messaging.autoconfigurer.jms;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.messaging.AbstractMessagingTemplateSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.core.send.jms.JmsJakartaMessagingTemplateSendService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JMS 消息发送服务的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = {"org.springframework.jms.core.JmsMessagingTemplate", "jakarta.jms.Destination"})
@AutoConfigureAfter(JmsAutoConfiguration.class)
@EnableConfigurationProperties(JmsSendServiceProperties.class)
public class JmsJakartaMessageSendServiceConfiguration extends AbstractMessagingTemplateSendServiceConfiguration {
    /**
     * 配置 JmsMessagingTemplateSendService 实例。
     *
     * @param properties 配置属性
     * @return JmsMessagingTemplateSendService 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_JMS_SEND_SERVICE,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public JmsJakartaMessagingTemplateSendService jmsJakartaMessagingTemplateSendService(JmsSendServiceProperties properties) {
        JmsJakartaMessagingTemplateSendService sendService = new JmsJakartaMessagingTemplateSendService();
        super.configureSendService(sendService, properties);

        // 设置 resolveDestinationFromBean
        sendService.setResolveDestinationFromBean(properties.isResolveDestinationFromBean());

        // 返回
        return sendService;
    }
}
