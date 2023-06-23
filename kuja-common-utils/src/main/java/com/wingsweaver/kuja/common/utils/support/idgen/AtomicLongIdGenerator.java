package com.wingsweaver.kuja.common.utils.support.idgen;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于 {@link AtomicLong} 的自增型的 {@link LongIdGenerator} 实现。
 * 仅用于单机版系统，分布式系统会出现重复 ID。
 *
 * @author wingsweaver
 */
public class AtomicLongIdGenerator implements LongIdGenerator {
    /**
     * ID 的值。
     */
    private final AtomicLong id;

    /**
     * 构造函数。
     */
    public AtomicLongIdGenerator() {
        this(0);
    }

    /**
     * 构造函数。
     *
     * @param initValue ID 的初始值
     */
    public AtomicLongIdGenerator(long initValue) {
        this.id = new AtomicLong(initValue);
    }

    @Override
    public Long nextId() {
        return this.id.getAndIncrement();
    }
}
