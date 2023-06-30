package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.LocalPidWorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.WorkerIdResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于 {@link LocalPidWorkerIdResolver} 的配置。
 *
 * @author wingsweaver
 */
@Configuration
@ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID, name = "type", havingValue = "local_pid")
public class WorkerIdResolverConfigurationLocalPid extends AbstractConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public WorkerIdResolver kujaWorkerIdResolver(WorkerIdResolverProperties properties) {
        LocalPidWorkerIdResolver workerIdResolver = new LocalPidWorkerIdResolver(properties.getBits());
        workerIdResolver.setTrimToMaskValue(properties.isTrimToMaskValue());
        return workerIdResolver;
    }
}
