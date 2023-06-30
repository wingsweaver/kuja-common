package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link WorkerIdResolver} 实现类的基类。
 *
 * @author wingsweaver
 */
public abstract class AbstractWorkerIdResolver extends AbstractPartGenerator implements WorkerIdResolver {
    /**
     * 工作节点 ID 的缓存值。
     */
    private volatile Integer workerId;

    /**
     * 是否裁剪到掩码值。
     */
    @Getter
    @Setter
    private boolean trimToMaskValue;

    protected AbstractWorkerIdResolver(int bits) {
        super(bits);
    }

    @Override
    public int resolveWorkerId() {
        if (this.workerId == null) {
            synchronized (this) {
                if (this.workerId == null) {
                    int tempValue = this.resolveWorkerIdInternal();
                    if (tempValue > this.maxValue()) {
                        if (this.trimToMaskValue) {
                            tempValue = (int) (tempValue & this.maxValue());
                        } else {
                            throw new IllegalStateException("WorkerId " + tempValue + " is greater than max value" + this.maxValue());
                        }
                    }
                    this.workerId = tempValue;
                }
            }
        }
        return this.workerId;
    }

    /**
     * 获取 Worker Id 的实际逻辑。
     *
     * @return Worker Id
     */
    protected abstract int resolveWorkerIdInternal();
}
