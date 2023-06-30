package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * 基于 Redis 的 {@link WorkerIdResolver} 实现类的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractRedisWorkerIdResolver extends AbstractRenewableWorkerIdResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRedisWorkerIdResolver.class);

    /**
     * Redis Key。
     */
    private String key;

    /**
     * WorkerId 节点的值。
     */
    private String workerIdValue;

    /**
     * WorkerId 节点的有效期间。
     */
    private Duration workerIdExpiration;

    protected AbstractRedisWorkerIdResolver(int bits) {
        super(bits);
    }

    /**
     * 计算指定 WorkerId 对应的 Redis 数据的 Key。
     *
     * @param workerId WorkerId
     * @return Redis 数据的 Key
     */
    protected String resolveWorkerIdKey(Integer workerId) {
        if (workerId == null) {
            return null;
        }
        return this.key + ":" + workerId;
    }

    @Override
    protected int resolveWorkerIdInternal() {
        if (!this.isAutoRenew()) {
            // 如果不带自动续约的话，那么直接从 Redis 中获取 WorkerId
            return this.resolveWorkerIdInternalWithoutRenew();
        }

        // 如果带自动续约的话，那么从 Redis 中获取 WorkerId 后还需要校验其是否存在
        long maxRetries = this.maxValue();
        for (long i = 0; i < maxRetries; i++) {
            int workerId = this.resolveWorkerIdInternalWithoutRenew();
            if (this.validateWorkerId(workerId)) {
                return workerId;
            }

            // 准备下一轮尝试
            LogUtil.trace(LOGGER, "Redis WorkerId is invalid: workerId = {}, preparing for next retry...", workerId);
            if (workerId > this.maxValue() && !this.isTrimToMaskValue()) {
                LogUtil.trace(LOGGER, "Redis WorkerId is invalid: workerId = {}, but trimToMaskValue = false, so throw exception", workerId);
                return workerId;
            }
        }

        // 没有找到可用的 WorkerId 的话，抛出异常
        throw new IllegalStateException("No valid WorkerId found in Redis");
    }

    /**
     * 获取 Worker Id 的实际逻辑。
     *
     * @return Worker Id
     */
    protected abstract int resolveWorkerIdInternalWithoutRenew();

    /**
     * 校验 WorkerId 是否有效。<br>
     * 即在 Redis 中对应的数据是否存在。
     *
     * @param workerId WorkerId
     * @return 是否有效
     */
    protected abstract boolean validateWorkerId(int workerId);

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 校验参数
        AssertState.Named.notEmpty("key", this.key);
        AssertState.Named.notEmpty("workerIdValue", this.workerIdValue);
        AssertState.Named.notNull("workerIdExpiration", this.workerIdExpiration);
    }
}
