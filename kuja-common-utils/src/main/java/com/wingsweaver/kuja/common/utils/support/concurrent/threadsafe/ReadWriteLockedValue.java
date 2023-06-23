package com.wingsweaver.kuja.common.utils.support.concurrent.threadsafe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * 基于 {@link ReadWriteLock} 的 {@link ThreadSafeValue} 实现类。
 *
 * @param <V> 数据的类型
 * @author wingsweaver
 */
public class ReadWriteLockedValue<V> implements ThreadSafeValue<V> {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private V value;

    public ReadWriteLockedValue(V value) {
        this.value = value;
    }

    public ReadWriteLockedValue() {
        this(null);
    }

    @Override
    public V get() {
        Lock readLock = this.readWriteLock.readLock();
        readLock.lock();
        try {
            return this.value;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void set(V value) {
        Lock writeLock = this.readWriteLock.writeLock();
        writeLock.lock();
        try {
            this.value = value;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public V compute(Function<V, V> callback) {
        V value = null;
        Lock writeLock = this.readWriteLock.writeLock();
        writeLock.lock();
        try {
            value = callback.apply(this.value);
            this.value = value;
        } finally {
            writeLock.unlock();
        }
        return value;
    }
}
