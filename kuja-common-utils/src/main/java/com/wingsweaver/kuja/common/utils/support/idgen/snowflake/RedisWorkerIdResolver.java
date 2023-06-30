package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 基于 Redis 的 {@link WorkerIdResolver} 实现类。
 *
 * @author wingsweaver
 */
@SuppressWarnings("DataFlowIssue")
@Getter
@Setter
public class RedisWorkerIdResolver extends AbstractRedisWorkerIdResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisWorkerIdResolver.class);

    /**
     * Redis 模板。
     */
    private StringRedisTemplate redisTemplate;

    public RedisWorkerIdResolver(int bits) {
        super(bits);
    }

    @SuppressWarnings("PMD.GuardLogStatement")
    @Override
    protected void renewWorkerId() {
        String workerIdKey = this.resolveWorkerIdKey(this.getWorkerId());
        if (StringUtil.isNotEmpty(workerIdKey)) {
            LogUtil.trace(LOGGER, "Renew Redis WorkerId: workerIdKey = {}, workerIdValue = {}, workerIdExpiration = {}",
                    workerIdKey, this.getWorkerId(), this.getWorkerIdExpiration());
            this.redisTemplate.opsForValue().set(workerIdKey, this.getWorkerIdValue(), this.getWorkerIdExpiration());
        }
    }

    @Override
    protected void revokeWorkerId() {
        String workerIdKey = this.resolveWorkerIdKey(this.getWorkerId());
        if (StringUtil.isNotEmpty(workerIdKey)) {
            LogUtil.trace(LOGGER, "Revoke Redis WorkerId: workerIdKey = {}", workerIdKey);
            this.redisTemplate.delete(workerIdKey);
        }
    }

    @Override
    protected int resolveWorkerIdInternalWithoutRenew() {
        return this.redisTemplate.opsForValue().increment(this.getKey()).intValue();
    }

    /**
     * 校验 WorkerId 是否有效。<br>
     * 即在 Redis 中对应的数据是否存在。
     *
     * @param workerId WorkerId
     * @return 是否有效
     */
    @Override
    protected boolean validateWorkerId(int workerId) {
        String workerIdKey = this.resolveWorkerIdKey((int) (workerId & this.maxValue()));
        Boolean result = this.redisTemplate.opsForValue().setIfAbsent(workerIdKey, this.getWorkerIdValue(), this.getWorkerIdExpiration());
        return Boolean.TRUE.equals(result);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 redisTemplate
        this.initRedisTemplate();
    }

    /**
     * 初始化 redisTemplate。
     */
    protected void initRedisTemplate() {
        if (this.redisTemplate == null) {
            this.redisTemplate = this.getBean(StringRedisTemplate.class);
        }
    }
}
