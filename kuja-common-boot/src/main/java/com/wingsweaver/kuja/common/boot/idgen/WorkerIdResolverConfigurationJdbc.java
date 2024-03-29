package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.JdbcWorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.WorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * 基于 JDBC 的 WorkerIdResolver 配置。
 *
 * @author wingsweaver
 */
@Configuration
@ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID, name = "type", havingValue = "jdbc")
@EnableConfigurationProperties(WorkerIdResolverProperties.class)
public class WorkerIdResolverConfigurationJdbc extends AbstractConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID, name = "enabled", havingValue = "true", matchIfMissing = true)
    public WorkerIdResolver kujaWorkerIdResolver(WorkerIdResolverProperties properties) {
        JdbcWorkerIdResolver workerIdResolver = new JdbcWorkerIdResolver(properties.getBits());
        WorkerIdResolverProperties.JdbcProperties jdbcProperties = properties.getJdbc();
        workerIdResolver.setTrimToMaskValue(properties.isTrimToMaskValue());
        workerIdResolver.setSql(jdbcProperties.getSql());
        workerIdResolver.setAutoGeneratedKeys(jdbcProperties.getAutoGeneratedKeys());

        // 设置 JdbcTemplate
        if (StringUtil.isNotEmpty(jdbcProperties.getJdbcTemplate())) {
            workerIdResolver.setJdbcTemplate(this.getBean(jdbcProperties.getJdbcTemplate(), JdbcTemplate.class, true));
        }

        // 设置 PreparedStatementSetter
        if (StringUtil.isNotEmpty(jdbcProperties.getPreparedStatementSetter())) {
            workerIdResolver.setPreparedStatementSetter(
                    this.getBean(jdbcProperties.getPreparedStatementSetter(), PreparedStatementSetter.class, true));
        }

        // 返回
        return workerIdResolver;
    }
}
