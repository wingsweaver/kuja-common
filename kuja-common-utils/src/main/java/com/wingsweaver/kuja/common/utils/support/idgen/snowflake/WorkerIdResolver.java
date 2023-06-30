package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

/**
 * Worker Id 生成器的接口定义。
 *
 * @author wingsweaver
 */
public interface WorkerIdResolver extends PartGenerator {
    /**
     * 获取 Worker Id。
     *
     * @return Worker Id
     */
    int resolveWorkerId();
}
