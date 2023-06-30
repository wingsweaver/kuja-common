package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 默认的 {@link SequenceIdGenerator} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultSequenceIdGenerator extends AbstractPartGenerator implements SequenceIdGenerator {
    /**
     * 上次生成序列 ID 时使用的时间戳。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private long lastTimeStamp;

    /**
     * 上次生成的序列 ID。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private long lastSequence;

    public DefaultSequenceIdGenerator(int bits) {
        super(bits);
    }

    @Override
    public Long nextSequenceId(long timestamp) {
        synchronized (this) {
            if (this.lastTimeStamp != timestamp) {
                // 如果时间戳发生变化，那么重新生成序列 ID
                this.lastTimeStamp = timestamp;
                this.lastSequence = 0;
            } else {
                // 如果时间戳不变，那么基于 lastSequence 生成下一个序列 ID
                if (this.lastSequence >= this.maxValue()) {
                    return NO_MORE_SEQUENCE_ID;
                }
                this.lastSequence += 1;
            }

            // 返回结果
            return this.lastSequence;
        }
    }
}
