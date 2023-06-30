package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.AbstractRenewableWorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;

import java.util.concurrent.ScheduledExecutorService;

/**
 * {@link AbstractRenewableWorkerIdResolver} 的配置类的基类。
 *
 * @author wingsweaver
 */
public abstract class AbstractRenewableWorkerIdResolverConfiguration extends AbstractConfiguration {
    /**
     * 配置 {@link AbstractRenewableWorkerIdResolver}。
     *
     * @param workerIdResolver {@link AbstractRenewableWorkerIdResolver} 实例
     * @param properties       {@link WorkerIdResolverProperties.AbstractRenewableProperties} 实例
     */
    protected void configure(AbstractRenewableWorkerIdResolver workerIdResolver,
                             WorkerIdResolverProperties.AbstractRenewableProperties properties) {
        workerIdResolver.setAutoRenew(properties.isAutoRenew());
        workerIdResolver.setRenewIntervalMin(properties.getRenewIntervalMin());
        workerIdResolver.setRenewIntervalMax(properties.getRenewIntervalMax());
        workerIdResolver.setRenewTimeUnit(properties.getRenewTimeUnit());

        // 设置 scheduledExecutorService
        String executorServiceName = properties.getScheduledExecutorService();
        if (StringUtil.isNotEmpty(executorServiceName)) {
            ScheduledExecutorService executorService = this.getBean(executorServiceName, ScheduledExecutorService.class, true);
            workerIdResolver.setScheduledExecutorService(executorService);
        }
    }
}
