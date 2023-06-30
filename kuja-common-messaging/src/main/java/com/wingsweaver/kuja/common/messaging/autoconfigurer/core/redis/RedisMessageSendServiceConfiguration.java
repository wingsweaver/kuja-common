package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redis;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Redis 消息发送服务的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
@EnableConfigurationProperties(RedisSendServiceProperties.class)
@ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_REDIS_SEND_SERVICE,
        name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(RedisSendServiceImportSelector.class)
public class RedisMessageSendServiceConfiguration extends AbstractConfiguration {
}
