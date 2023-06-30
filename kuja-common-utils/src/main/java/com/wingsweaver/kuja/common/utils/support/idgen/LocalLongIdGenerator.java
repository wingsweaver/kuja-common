package com.wingsweaver.kuja.common.utils.support.idgen;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.lang.ThreadUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;

/**
 * 基于时间戳和序列 ID 生成器。<br>
 * 可以保证本进程中生成的 ID 的唯一性，但是不能保证多进程以及分布式环境下的唯一性。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class LocalLongIdGenerator implements LongIdGenerator {
    /**
     * 默认实例。
     */
    public static final LocalLongIdGenerator INSTANCE = new LocalLongIdGenerator();

    /**
     * 默认的序列值的 BIT 数。
     */
    public static final int DEFAULT_SEQUENCE_BITS = 16;

    /**
     * 序列值的 BIT 数。<br>
     * 建议取值范围是 8 - 16。
     */
    private final int sequenceBits;

    /**
     * 最大序列值。
     */
    private final long maxSequenceValue;

    /**
     * 序列值生成器。
     */
    @Getter(AccessLevel.NONE)
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * 上次生成时使用的时间戳。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private volatile long lastTimestamp;

    /**
     * 上次生成的序列值。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private volatile long lastSequence;

    /**
     * 重试的等待时间（毫秒）。
     */
    public long retryInterval = 100;

    /**
     * 互斥锁。
     */
    private final Object locker = new Object();

    public LocalLongIdGenerator(int sequenceBits) {
        this.sequenceBits = Math.max(sequenceBits, 0);
        this.maxSequenceValue = (1L << this.sequenceBits) - 1;
    }

    public LocalLongIdGenerator() {
        this(DEFAULT_SEQUENCE_BITS);
    }

    @Override
    public Long nextId() {
        long timestamp = System.currentTimeMillis();

        // 如果不使用序列值的话，那么直接返回当前的时间戳
        if (this.sequenceBits <= 0) {
            return timestamp;
        }

        // 否则，生成序列值
        long sequence = -1;
        synchronized (this.locker) {
            if (this.lastTimestamp < timestamp) {
                // 如果时间戳发生变化，那么重置 bitset
                this.lastTimestamp = timestamp;
                this.lastSequence = 0;
                sequence = this.lastSequence;
            } else if (this.lastTimestamp == timestamp) {
                this.lastSequence += 1;
                if (this.lastSequence <= this.maxSequenceValue) {
                    sequence = this.lastSequence;
                }
            }
        }

        // 返回结果
        if (sequence >= 0) {
            // 如果序列值有效，那么据时间戳和序列值，生成 id
            return timestamp << this.sequenceBits | sequence;
        } else {
            // 如果还没有可用的序列值的话，那么等待一段时间后，再次生成
            ThreadUtil.sleep(this.retryInterval);
            return this.nextId();
        }
    }

    /**
     * 解析 ID，得到时间戳和序列值。
     *
     * @param id 生成的 ID
     * @return t1: 时间戳, t2: 序列值
     */
    public Tuple2<Long, Long> parse(long id) {
        long timestamp = id;
        long random = 0;
        if (sequenceBits > 0) {
            random = id & this.maxSequenceValue;
            timestamp = id >> this.sequenceBits;
        }
        return Tuple2.of(timestamp, random);
    }

    public void setRetryInterval(long retryInterval) {
        AssertArgs.Named.greaterThan("retryInterval", retryInterval, 0L);
        this.retryInterval = retryInterval;
    }
}
