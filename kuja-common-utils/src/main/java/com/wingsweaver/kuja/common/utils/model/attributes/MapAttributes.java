package com.wingsweaver.kuja.common.utils.model.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 基于 {@link Map} 的 {@link Attributes} 实现类。
 *
 * @param <K> 属性名称的类型
 * @author wingsweaver
 */
public class MapAttributes<K> implements Attributes<K> {
    /**
     * 空的属性字典。
     */
    @SuppressWarnings("rawtypes")
    static final Attributes EMPTY = new MapAttributes<>(null);

    /**
     * 不存在的属性的值。
     */
    protected static final Object NOT_PRESENT = new Object();

    /**
     * 实际的属性字典。
     */
    private Map<K, Object> map;

    @SuppressWarnings("unchecked")
    public MapAttributes(Map<K, ?> map) {
        this.map = (Map<K, Object>) map;
    }

    @Override
    public Collection<K> getKeys() {
        return new ArrayList<>(this.getMapOrEmpty().keySet());
    }

    @Override
    public boolean hasAttribute(K key) {
        return this.getMapOrEmpty().containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getAttribute(K key) {
        return (V) this.getMapOrEmpty().get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getAttribute(K key, V defaultValue) {
        return (V) this.getMapOrEmpty().getOrDefault(key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getAttribute(K key, Function<K, V> defaultValueSupplier) {
        Object value = this.getMapOrEmpty().getOrDefault(key, NOT_PRESENT);
        if (value == NOT_PRESENT) {
            return defaultValueSupplier.apply(key);
        } else {
            return (V) value;
        }
    }

    @Override
    public Map<K, Object> asMap() {
        return new HashMap<>(this.getMapOrEmpty());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void forEach(BiConsumer<K, ?> consumer) {
        this.getMapOrEmpty().forEach(((BiConsumer) consumer));
    }

    protected Map<K, Object> getMapOrEmpty() {
        return this.map != null ? this.map : Collections.emptyMap();
    }

    public Map<K, Object> getMap() {
        return this.map;
    }

    @SuppressWarnings("unchecked")
    protected void setMap(Map<K, ?> map) {
        this.map = (Map<K, Object>) map;
    }
}
