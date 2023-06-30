package com.wingsweaver.kuja.common.utils.support.idgen;

import org.springframework.util.backoff.BackOff;

/**
 * 带缓存的 {@link LongIdGenerator} 实现类。
 *
 * @author wingsweaver
 */
public class CachedLongIdGenerator extends CachedIdGenerator<Long> implements LongIdGenerator {
    public CachedLongIdGenerator(IdGenerator<Long> idGenerator, int cacheSize, BackOff backOff) {
        super(idGenerator, cacheSize, backOff);
    }
}
