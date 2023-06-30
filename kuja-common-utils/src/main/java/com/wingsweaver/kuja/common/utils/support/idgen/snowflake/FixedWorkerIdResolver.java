package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import lombok.Getter;
import lombok.Setter;

/**
 * 使用固定值的 {@link WorkerIdResolver} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class FixedWorkerIdResolver extends AbstractWorkerIdResolver implements WorkerIdResolver {
    /**
     * Worker Id。
     */
    private int workerId;

    public FixedWorkerIdResolver(int bits) {
        super(bits);
    }

    @Override
    protected int resolveWorkerIdInternal() {
        return this.workerId;
    }
}
