package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple3;
import com.wingsweaver.kuja.common.utils.support.idgen.CachedLongIdGenerator;
import org.springframework.util.backoff.BackOff;

/**
 * 带缓存的 SnowFlake {@link com.wingsweaver.kuja.common.utils.support.idgen.LongIdGenerator} 实现类。
 *
 * @author wingsweaver
 */
public class CachedSnowFlakeIdGenerator extends CachedLongIdGenerator implements SnowFlakeIdParser {
    /**
     * SnowFlakeIdGenerator 实例。
     */
    private final SnowFlakeIdGenerator snowFlakeIdGenerator;

    public CachedSnowFlakeIdGenerator(SnowFlakeIdGenerator snowFlakeIdGenerator, int cacheSize, BackOff backOff) {
        super(snowFlakeIdGenerator, cacheSize, backOff);
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
    }

    @Override
    public Tuple3<Long, Long, Long> parse(long id) {
        return this.snowFlakeIdGenerator.parse(id);
    }
}
