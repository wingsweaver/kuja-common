package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redisson;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.common.CommonSendServiceProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.core.send.redis.RedissonTopicMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 适用于 Redisson 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingKeys.PREFIX_REDISSON_SEND_SERVICE)
public class RedissonSendServiceProperties extends CommonSendServiceProperties {
    /**
     * RedissonClient 实例。
     */
    private String redissonClient;

    /**
     * 消息的编解码器。
     */
    private String codec;

    /**
     * TOPIC 的模式。
     */
    private RedissonTopicMode topicMode = RedissonTopicMode.TOPIC;
}
