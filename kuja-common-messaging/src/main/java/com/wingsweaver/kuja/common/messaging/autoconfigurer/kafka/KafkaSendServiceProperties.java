package com.wingsweaver.kuja.common.messaging.autoconfigurer.kafka;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.common.CommonSendServiceProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 适用于 Kafka 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingKeys.PREFIX_KAFKA_SEND_SERVICE)
public class KafkaSendServiceProperties extends CommonSendServiceProperties {
    private String kafkaTemplate;
}
