package com.wingsweaver.kuja.common.messaging.autoconfigurer.constants;

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
     * {@link com.wingsweaver.kuja.common.messaging.autoconfigurer.sender.KujaMessageSenderProperties}。
     */
    String PREFIX_MESSAGE_SENDER = PREFIX_KUJA_COMMON_MESSAGING + ".sender";

    /**
     * {@link com.wingsweaver.kuja.common.messaging.autoconfigurer.jms.JmsSendServiceProperties}。
     */
    String PREFIX_JMS_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".jms";

    /**
     * {@link com.wingsweaver.kuja.common.messaging.autoconfigurer.rabbit.RabbitSendServiceProperties}。
     */
    String PREFIX_RABBIT_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".rabbit";

    /**
     * {@link com.wingsweaver.kuja.common.messaging.autoconfigurer.rabbit.RabbitSendServiceProperties}。
     */
    String PREFIX_ROCKETMQ_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".rocketmq";

    /**
     * {@link com.wingsweaver.kuja.common.messaging.autoconfigurer.kafka.KafkaSendServiceProperties}。
     */
    String PREFIX_KAFKA_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".kafka";

    /**
     * {@link com.wingsweaver.kuja.common.messaging.autoconfigurer.redis.RedisSendServiceProperties}。
     */
    String PREFIX_REDIS_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".redis";

    /**
     * {@link com.wingsweaver.kuja.common.messaging.autoconfigurer.redisson.RedissonSendServiceProperties}。
     */
    String PREFIX_REDISSON_SEND_SERVICE = PREFIX_KUJA_COMMON_MESSAGING + ".redisson";
}
