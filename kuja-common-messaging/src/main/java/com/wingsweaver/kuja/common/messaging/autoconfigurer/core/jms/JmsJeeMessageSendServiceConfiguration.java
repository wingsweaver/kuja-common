package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.jms;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.messaging.AbstractMessagingTemplateSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.core.send.jms.JmsJeeMessagingTemplateSendService;
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
@ConditionalOnClass(name = {"org.springframework.jms.core.JmsMessagingTemplate", "javax.jms.Destination"})
@AutoConfigureAfter(JmsAutoConfiguration.class)
@EnableConfigurationProperties(JmsSendServiceProperties.class)
public class JmsJeeMessageSendServiceConfiguration extends AbstractMessagingTemplateSendServiceConfiguration {
    /**
     * 配置 JmsMessagingTemplateSendService 实例。
     *
     * @param properties 配置属性
     * @return JmsMessagingTemplateSendService 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_JMS_SEND_SERVICE,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public JmsJeeMessagingTemplateSendService jmsJeeMessagingTemplateSendService(JmsSendServiceProperties properties) {
        JmsJeeMessagingTemplateSendService sendService = new JmsJeeMessagingTemplateSendService();
        super.configureSendService(sendService, properties);

        // 设置 resolveDestinationFromBean
        sendService.setResolveDestinationFromBean(properties.isResolveDestinationFromBean());

        // 返回
        return sendService;
    }
}
