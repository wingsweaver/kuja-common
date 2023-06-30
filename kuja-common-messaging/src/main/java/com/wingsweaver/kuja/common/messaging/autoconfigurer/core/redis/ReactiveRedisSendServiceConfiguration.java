package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redis;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.common.AbstractSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.core.send.redis.ReactiveRedisStreamSendService;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

/**
 * Reactive 模式下的配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = {"org.springframework.data.redis.core.ReactiveStringRedisTemplate", "reactor.core.publisher.Flux"})
@AutoConfigureAfter(RedisReactiveAutoConfiguration.class)
@EnableConfigurationProperties(RedisSendServiceProperties.class)
public class ReactiveRedisSendServiceConfiguration extends AbstractSendServiceConfiguration {
    /**
     * 配置 RedisTemplateSendService 实例。
     *
     * @param properties 配置属性
     * @return RedisTemplateSendService 实例
     */
    @Bean
    public ReactiveRedisStreamSendService reactiveRedisStreamSendService(RedisSendServiceProperties properties) {
        ReactiveRedisStreamSendService sendService = new ReactiveRedisStreamSendService();
        super.configureSendService(sendService, properties);

        // 设置 redisTemplate
        if (sendService.getRedisTemplate() == null) {
            String redisTemplate = properties.getRedisTemplate();
            if (StringUtil.isNotEmpty(redisTemplate)) {
                sendService.setRedisTemplate(this.getBean(redisTemplate, ReactiveStringRedisTemplate.class, true));
            }
        }

        // 返回
        return sendService;
    }
}
