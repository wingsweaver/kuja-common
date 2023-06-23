package com.wingsweaver.kuja.common.utils.support.concurrent.threadsafe;

import java.util.function.Function;

/**
 * 基于 synchronized 的 {@link ThreadSafeValue} 实现类。
 *
 * @param <V> 数据的类型
 * @author wingsweaver
 */
public class SynchronizedValue<V> implements ThreadSafeValue<V> {
    private V value;

    public SynchronizedValue(V value) {
        this.value = value;
    }

    public SynchronizedValue() {
        this(null);
    }

    @Override
    public V get() {
        synchronized (this) {
            return this.value;
        }
    }

    @Override
    public void set(V value) {
        synchronized (this) {
            this.value = value;
        }
    }

    @Override
    public V compute(Function<V, V> callback) {
        V value = null;
        synchronized (this) {
            value = callback.apply(this.value);
            this.value = value;
        }
        return value;
    }
}
