package com.wingsweaver.kuja.common.utils.support;

/**
 * 含有数据的接口。
 *
 * @param <T> 数据类型
 * @author wingsweaver
 */
public interface Valued<T> {
    /**
     * 获取数据。
     *
     * @return 数据
     */
    T getValue();
}
