package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.jms.JmsSendServiceProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.kafka.KafkaSendServiceProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.rabbit.RabbitSendServiceProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redis.RedisSendServiceProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redisson.RedissonSendServiceProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.sender.KujaMessageSenderProperties;

/**
 * kuja-common-messaging 中的 Key 定义。
 *
 * @author wingsweaver
 */
public interface KujaCommonMessagingKeys {
    /**
     * kuja-common-messaging 模块的配置属性前缀。
     */
    String PREFIX_KUJA_COMMON_MESSAGING = "kuja.messaging";

    /**
     * {@link KujaMessageSenderProperties}。
     */
    String PREFIX_MESSAGE_SENDER = PREFIX_KUJA_COMMON_MESSAGING + ".sender";

    /**
     * {@link JmsSendServiceProperties}。
     */
    String PREFIX_JMS_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".jms";

    /**
     * {@link RabbitSendServiceProperties}。
     */
    String PREFIX_RABBIT_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".rabbit";

    /**
     * {@link RabbitSendServiceProperties}。
     */
    String PREFIX_ROCKETMQ_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".rocketmq";

    /**
     * {@link KafkaSendServiceProperties}。
     */
    String PREFIX_KAFKA_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".kafka";

    /**
     * {@link RedisSendServiceProperties}。
     */
    String PREFIX_REDIS_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".redis";

    /**
     * {@link RedissonSendServiceProperties}。
     */
    String PREFIX_REDISSON_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".redisson";
}
