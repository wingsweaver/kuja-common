package com.wingsweaver.kuja.common.messaging.autoconfigurer.core;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.jms.JmsJeeMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.kafka.KafkaMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.rabbit.RabbitMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redis.RedisMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redisson.RedissonClientSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.rocketmq.RocketMqMessageSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.sender.KujaMessageSenderConfiguration;
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
public class KujaCommonMessagingCoreConfiguration extends AbstractConfiguration {
}
