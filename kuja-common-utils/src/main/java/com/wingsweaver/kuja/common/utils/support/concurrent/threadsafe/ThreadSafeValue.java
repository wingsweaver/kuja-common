package com.wingsweaver.kuja.common.utils.support.concurrent.threadsafe;

import java.util.function.Function;

/**
 * 可线程安全访问的接口。
 *
 * @param <V> 数据的类型
 * @author wingsweaver
 */
public interface ThreadSafeValue<V> {
    /**
     * 获取当前数据。
     *
     * @return 当前数据
     */
    V get();

    /**
     * 设置当前数据。
     *
     * @param value 当前数据
     */
    void set(V value);

    /**
     * 如果当前数据不存在的话，设置当前数据。
     *
     * @param value 当前数据
     */
    default void setIfAbsent(V value) {
        this.compute(oldValue -> oldValue != null ? oldValue : value);
    }

    /**
     * 更新当前数据。
     *
     * @param callback 更新函数
     * @return 新的当前数据
     */
    V compute(Function<V, V> callback);
}
