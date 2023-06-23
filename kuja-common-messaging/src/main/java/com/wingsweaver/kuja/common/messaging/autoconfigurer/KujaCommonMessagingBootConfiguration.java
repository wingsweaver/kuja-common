package com.wingsweaver.kuja.common.messaging.autoconfigurer;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.jms.JmsJeeMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.kafka.KafkaMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.rabbit.RabbitMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.redis.RedisMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.redisson.RedissonClientSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.rocketmq.RocketMqMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.sender.KujaMessageSenderConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * kuja-common-messaging 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_KUJA_COMMON_MESSAGING,
        name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({
        KujaMessageSenderConfiguration.class,
        JmsJeeMessageSendServiceConfiguration.class, RabbitMessageSendServiceConfiguration.class,
        RocketMqMessageSendServiceConfiguration.class,
        KafkaMessageSendServiceConfiguration.class,
        RedisMessageSendServiceConfiguration.class, RedissonClientSendServiceConfiguration.class})
public class KujaCommonMessagingBootConfiguration extends AbstractConfiguration {
}
