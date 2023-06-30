package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.RedissonWorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.WorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于 Redisson 的 WorkerIdResolver 配置。
 *
 * @author wingsweaver
 */
@Configuration
@ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID, name = "type", havingValue = "redisson")
@EnableConfigurationProperties(WorkerIdResolverProperties.class)
public class WorkerIdResolverConfigurationRedisson extends AbstractRenewableWorkerIdResolverConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID, name = "enabled", havingValue = "true", matchIfMissing = true)
    public WorkerIdResolver kujaWorkerIdResolver(WorkerIdResolverProperties properties) {
        WorkerIdResolverProperties.RedissonProperties redissonProperties = properties.getRedisson();
        RedissonWorkerIdResolver workerIdResolver = new RedissonWorkerIdResolver(properties.getBits());
        workerIdResolver.setTrimToMaskValue(properties.isTrimToMaskValue());

        // 进行 AbstractRenewableWorkerIdResolverConfiguration 级别的配置
        this.configure(workerIdResolver, redissonProperties);

        // 进行 RedisWorkerIdResolver 特有的设置
        workerIdResolver.setKey(redissonProperties.getKey());
        workerIdResolver.setWorkerIdValue(redissonProperties.getWorkerIdValue());
        workerIdResolver.setWorkerIdExpiration(redissonProperties.getWorkerIdExpiration());

        // 设置 RedissonClient
        if (StringUtil.isNotEmpty(redissonProperties.getRedissonClient())) {
            workerIdResolver.setRedissonClient(this.getBean(redissonProperties.getRedissonClient(), RedissonClient.class, true));
        }

        // 返回
        return workerIdResolver;
    }
}
