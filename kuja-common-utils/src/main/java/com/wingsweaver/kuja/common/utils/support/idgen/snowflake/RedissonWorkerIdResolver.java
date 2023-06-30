package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redisson 的 {@link WorkerIdResolver} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class RedissonWorkerIdResolver extends AbstractRedisWorkerIdResolver {
    /**
     * RedissonClient 实例。
     */
    private RedissonClient redissonClient;

    public RedissonWorkerIdResolver(int bits) {
        super(bits);
    }

    @Override
    protected void renewWorkerId() {
        String workerIdKey = this.resolveWorkerIdKey(this.getWorkerId());
        if (StringUtil.isNotEmpty(workerIdKey)) {
            this.redissonClient.getBucket(workerIdKey).set(this.getWorkerIdValue(), this.getWorkerIdExpiration().toMillis(),
                    TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected void revokeWorkerId() {
        String workerIdKey = this.resolveWorkerIdKey(this.getWorkerId());
        if (StringUtil.isNotEmpty(workerIdKey)) {
            this.redissonClient.getBucket(workerIdKey).delete();
        }
    }

    @Override
    protected int resolveWorkerIdInternalWithoutRenew() {
        return (int) this.redissonClient.getAtomicLong(this.getKey()).getAndIncrement();
    }

    @Override
    protected boolean validateWorkerId(int workerId) {
        String workerIdKey = this.resolveWorkerIdKey((int) (workerId & this.maxValue()));
        return this.redissonClient.getBucket(workerIdKey).setIfAbsent(this.getWorkerIdValue(), this.getWorkerIdExpiration());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 redissonClient
        this.initRedissonClient();
    }

    /**
     * 初始化 redissonClient。
     */
    protected void initRedissonClient() {
        if (this.redissonClient == null) {
            this.redissonClient = this.getBean(RedissonClient.class);
        }
    }
}
