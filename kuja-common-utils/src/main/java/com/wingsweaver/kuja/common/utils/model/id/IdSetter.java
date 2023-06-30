package com.wingsweaver.kuja.common.utils.model.id;

/**
 * 可以设置和读取 ID 的类型的接口定义。
 *
 * @param <T> ID 类型
 * @author wingsweaver
 */
public interface IdSetter<T> extends IdGetter<T> {
    /**
     * 设置 ID。
     *
     * @param id ID
     */
    void setId(T id);
}
