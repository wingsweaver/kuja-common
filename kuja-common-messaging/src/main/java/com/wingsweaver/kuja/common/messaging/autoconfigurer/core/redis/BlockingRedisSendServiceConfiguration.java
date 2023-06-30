package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redis;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.common.AbstractSendServiceConfiguration;
import com.wingsweaver.kuja.common.messaging.core.send.redis.RedisStreamSendService;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 阻塞模式下的配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.springframework.data.redis.core.StringRedisTemplate")
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisSendServiceProperties.class)
public class BlockingRedisSendServiceConfiguration extends AbstractSendServiceConfiguration {
    /**
     * 配置 RedisTemplateSendService 实例。
     *
     * @param properties 配置属性
     * @return RedisTemplateSendService 实例
     */
    @Bean
    public RedisStreamSendService redisStreamSendService(RedisSendServiceProperties properties) {
        RedisStreamSendService sendService = new RedisStreamSendService();
        super.configureSendService(sendService, properties);

        // 设置 redisTemplate
        if (sendService.getRedisTemplate() == null) {
            String redisTemplate = properties.getRedisTemplate();
            if (StringUtil.isNotEmpty(redisTemplate)) {
                sendService.setRedisTemplate(this.getBean(redisTemplate, StringRedisTemplate.class, true));
            }
        }

        // 返回
        return sendService;
    }
}
