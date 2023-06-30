package com.wingsweaver.kuja.common.utils.support.idgen;

import org.springframework.util.backoff.BackOff;

/**
 * 带缓存的 {@link StringIdGenerator} 实现类。
 *
 * @author wingsweaver
 */
public class CachedStringIdGenerator extends CachedIdGenerator<String> implements StringIdGenerator {
    public CachedStringIdGenerator(IdGenerator<String> idGenerator, int cacheSize, BackOff backOff) {
        super(idGenerator, cacheSize, backOff);
    }
}
