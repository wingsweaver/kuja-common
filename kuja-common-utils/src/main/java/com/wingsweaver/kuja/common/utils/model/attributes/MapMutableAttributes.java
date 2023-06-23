package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 基于字典的 {@link MutableAttributes} 实现类。
 *
 * @param <K> 属性名称的类型
 * @author wingsweaver
 */
public class MapMutableAttributes<K> extends MapAttributes<K> implements MutableAttributes<K> {
    public MapMutableAttributes(Map<K, ?> map) {
        super(map);
    }

    @Override
    public void setAttribute(K key, Object value) {
        MapUtil.put(this.getMap(true), key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V updateAttribute(K key, BiFunction<K, V, V> updater) {
        return (V) this.getMap(true).compute(key, (k, v) -> updater.apply(k, (V) v));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V setAttributeIfAbsent(K key, V fallbackValue) {
        return (V) MapUtil.putIfAbsent(this.getMap(true), key, fallbackValue);
    }

    @Override
    public void removeAttribute(K key) {
        this.getMap(true).remove(key);
    }

    @Override
    public void clearAttributes() {
        this.getMap(true).clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void importMap(Map<K, ?> map, boolean overwrite) {
        MapUtil.copy((Map<K, Object>) map, this.getMap(true), overwrite);
    }

    public Map<K, Object> getMap(boolean createIfAbsent) {
        Map<K, Object> map = this.getMap();
        if (map == null && createIfAbsent) {
            map = new HashMap<>(BufferSizes.SMALL);
            this.setMap(map);
        }
        return map;
    }
}
