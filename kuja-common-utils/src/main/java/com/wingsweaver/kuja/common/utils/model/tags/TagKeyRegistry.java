package com.wingsweaver.kuja.common.utils.model.tags;

/**
 * 注册 {@link TagKey} 的接口定义。
 *
 * @author wingsweaver
 */
public interface TagKeyRegistry {
    /**
     * 注册一个 {@link TagKey} 实例。
     *
     * @param tagKey {@link TagKey} 实例
     */
    void register(TagKey<?> tagKey);

    /**
     * 注销一个 {@link TagKey} 实例。
     *
     * @param tagKey {@link TagKey} 实例
     */
    void unregister(TagKey<?> tagKey);
}
