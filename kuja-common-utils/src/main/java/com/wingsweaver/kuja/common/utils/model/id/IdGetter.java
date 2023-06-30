package com.wingsweaver.kuja.common.utils.model.id;

/**
 * 含有 ID 的类型的接口定义。
 *
 * @param <T> ID 类型
 * @author wingsweaver
 */
public interface IdGetter<T> {
    /**
     * 获取 ID。
     *
     * @return ID
     */
    T getId();
}
