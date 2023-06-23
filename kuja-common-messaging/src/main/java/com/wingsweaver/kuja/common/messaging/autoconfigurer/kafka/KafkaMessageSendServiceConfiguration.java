package com.wingsweaver.kuja.common.messaging.autoconfigurer.kafka;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.common.AbstractSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.core.send.kafka.KafkaTemplateSendService;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Kafka 消息发送服务的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = {"org.springframework.kafka.core.KafkaTemplate", "org.apache.kafka.clients.producer.Producer"})
@AutoConfigureAfter(KafkaAutoConfiguration.class)
@EnableConfigurationProperties(KafkaSendServiceProperties.class)
public class KafkaMessageSendServiceConfiguration extends AbstractSendServiceConfiguration {
    /**
     * 配置 KafkaTemplateSendService 实例。
     *
     * @param properties 配置属性
     * @return KafkaTemplateSendService 实例
     */
    @SuppressWarnings("unchecked")
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_KAFKA_SEND_SERVICE,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public KafkaTemplateSendService kafkaTemplateSendService(KafkaSendServiceProperties properties) {
        KafkaTemplateSendService sendService = new KafkaTemplateSendService();
        super.configureSendService(sendService, properties);

        // 设置 kafkaTemplate
        if (sendService.getKafkaTemplate() == null) {
            String kafkaTemplate = properties.getKafkaTemplate();
            if (StringUtil.isNotEmpty(kafkaTemplate)) {
                sendService.setKafkaTemplate(this.getBean(kafkaTemplate, KafkaTemplate.class, true));
            }
        }

        // 返回
        return sendService;
    }
}
