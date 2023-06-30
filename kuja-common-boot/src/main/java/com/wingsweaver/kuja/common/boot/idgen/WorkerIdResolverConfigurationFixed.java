package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.FixedWorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.WorkerIdResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于 {@link FixedWorkerIdResolver} 的配置。
 *
 * @author wingsweaver
 */
@Configuration
@ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID, name = "type", havingValue = "fixed")
public class WorkerIdResolverConfigurationFixed extends AbstractConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID, name = "enabled", havingValue = "true", matchIfMissing = true)
    public WorkerIdResolver kujaWorkerIdResolver(WorkerIdResolverProperties properties) {
        FixedWorkerIdResolver workerIdResolver = new FixedWorkerIdResolver(properties.getBits());
        workerIdResolver.setTrimToMaskValue(properties.isTrimToMaskValue());
        workerIdResolver.setWorkerId(properties.getFixed().getWorkerId());
        return workerIdResolver;
    }
}
