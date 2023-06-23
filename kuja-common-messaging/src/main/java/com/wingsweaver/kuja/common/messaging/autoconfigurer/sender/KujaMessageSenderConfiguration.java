package com.wingsweaver.kuja.common.messaging.autoconfigurer.sender;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.jms.JmsJeeMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.rabbit.RabbitMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.core.send.DefaultMessageSender;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSender;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Kuja 消息发送服务的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KujaMessageSenderProperties.class)
@Import({JmsJeeMessageSendServiceConfiguration.class, RabbitMessageSendServiceConfiguration.class})
public class KujaMessageSenderConfiguration extends AbstractConfiguration {
    /**
     * 配置 MessageSender 实例。
     *
     * @param properties 配置属性
     * @return MessageSender 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageSender kujaMessageSender(KujaMessageSenderProperties properties,
                                           ObjectProvider<StringIdGenerator> stringIdGenerators) {
        DefaultMessageSender messageSender = new DefaultMessageSender();
        String contextIdGenerator = properties.getContextIdGenerator();
        if (StringUtil.isNotEmpty(contextIdGenerator)) {
            messageSender.setContextIdGenerator(this.getBean(contextIdGenerator, StringIdGenerator.class, true));
        }
        return messageSender;
    }
}
