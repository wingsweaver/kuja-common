package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.RedisWorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.WorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 基于 Redis 的 WorkerIdResolver 配置。
 *
 * @author wingsweaver
 */
@Configuration
@ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID, name = "type", havingValue = "redis")
@EnableConfigurationProperties(WorkerIdResolverProperties.class)
public class WorkerIdResolverConfigurationRedis extends AbstractRenewableWorkerIdResolverConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID, name = "enabled", havingValue = "true", matchIfMissing = true)
    public WorkerIdResolver kujaWorkerIdResolver(WorkerIdResolverProperties properties) {
        WorkerIdResolverProperties.RedisProperties redisProperties = properties.getRedis();
        RedisWorkerIdResolver workerIdResolver = new RedisWorkerIdResolver(properties.getBits());
        workerIdResolver.setTrimToMaskValue(properties.isTrimToMaskValue());

        // 进行 AbstractRenewableWorkerIdResolverConfiguration 级别的配置
        this.configure(workerIdResolver, redisProperties);

        // 进行 RedisWorkerIdResolver 特有的设置
        workerIdResolver.setKey(redisProperties.getKey());
        workerIdResolver.setWorkerIdValue(redisProperties.getWorkerIdValue());
        workerIdResolver.setWorkerIdExpiration(redisProperties.getWorkerIdExpiration());

        // 设置 RedisTemplate
        if (StringUtil.isNotEmpty(redisProperties.getRedisTemplate())) {
            workerIdResolver.setRedisTemplate(this.getBean(redisProperties.getRedisTemplate(), StringRedisTemplate.class, true));
        }

        // 返回
        return workerIdResolver;
    }
}
