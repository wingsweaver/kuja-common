package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redis;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.common.CommonSendServiceProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 适用于 Redis Streams 的消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingKeys.PREFIX_REDIS_SEND_SERVICE)
public class RedisSendServiceProperties extends CommonSendServiceProperties {
    /**
     * RedisTemplate 或者 ReactiveRedisTemplate 实例。
     */
    private String redisTemplate;

    /**
     * 是否使用 Reactive 模式。
     */
    private Boolean reactive;

    /**
     * {@link #reactive} 不指定时，是否优先使用 reactive 模式。
     */
    private boolean preferReactive = false;
}
