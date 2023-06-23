package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 属性的构建器的基类。
 *
 * @param <K> Key 的类型
 * @param <B> Builder 自身的类型
 * @author wingsweaver
 */
public abstract class AbstractAttributesBuilder<K, B> {
    protected final Map<K, Object> map;

    @SuppressWarnings("unchecked")
    protected AbstractAttributesBuilder(Map<K, ?> map) {
        this.map = (Map<K, Object>) map;
    }

    /**
     * 更新配置。
     *
     * @param map       字典
     * @param overwrite 是否覆盖现有数据
     * @return 本实例
     */
    @SuppressWarnings("unchecked")
    public B update(Map<K, ?> map, boolean overwrite) {
        MapUtil.copy((Map<K, Object>) map, this.map, overwrite);
        return (B) this;
    }

    /**
     * 更新配置。
     *
     * @param consumer 字典设置处理
     * @return 本实例
     */
    @SuppressWarnings("unchecked")
    public B update(Consumer<Map<K, Object>> consumer) {
        AssertArgs.Named.notNull("consumer", consumer);
        consumer.accept(this.map);
        return (B) this;
    }

    /**
     * 更新配置。
     *
     * @param key   键
     * @param value 值
     * @return 本实例
     */
    @SuppressWarnings("unchecked")
    public B update(K key, Object value) {
        this.map.put(key, value);
        return (B) this;
    }
}
