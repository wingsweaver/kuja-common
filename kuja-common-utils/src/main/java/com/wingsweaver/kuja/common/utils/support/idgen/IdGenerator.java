package com.wingsweaver.kuja.common.utils.support.idgen;

/**
 * ID 生成器的接口定义。
 *
 * @param <V> ID 类型
 * @author wingsweaver
 */
public interface IdGenerator<V> {
    /**
     * 生成下一个 ID。
     *
     * @return 下一个 ID
     */
    V nextId();
}
