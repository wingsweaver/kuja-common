package com.wingsweaver.kuja.common.messaging.autoconfigurer.redisson;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.common.AbstractSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.core.send.redis.RedissonClientSendService;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 消息发送服务的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.redisson.api.RedissonClient")
@EnableConfigurationProperties(RedissonSendServiceProperties.class)
public class RedissonClientSendServiceConfiguration extends AbstractSendServiceConfiguration {
    /**
     * 配置 RedissonClientSendService 实例。
     *
     * @param properties 配置属性
     * @return RedissonClientSendService 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_REDISSON_SEND_SERVICE,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public RedissonClientSendService redisTemplateSendService(RedissonSendServiceProperties properties) {
        RedissonClientSendService sendService = new RedissonClientSendService();
        super.configureSendService(sendService, properties);

        // 设置 redissonClient
        if (sendService.getRedissonClient() == null) {
            String redissonClient = properties.getRedissonClient();
            if (StringUtil.isNotEmpty(redissonClient)) {
                sendService.setRedissonClient(this.getBean(redissonClient, RedissonClient.class, true));
            }
        }

        // 设置 codec
        if (sendService.getCodec() == null) {
            String codec = properties.getCodec();
            if (StringUtil.isNotEmpty(codec)) {
                sendService.setCodec(this.getBean(codec, Codec.class, true));
            }
        }

        // 返回
        return sendService;
    }
}
