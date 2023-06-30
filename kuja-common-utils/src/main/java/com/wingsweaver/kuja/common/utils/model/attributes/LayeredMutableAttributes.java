package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.tostring.DontReflect;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 基于层级的 {@link MutableAttributes} 实现类。
 *
 * @param <K> 属性名称的类型
 * @author wingsweaver
 */
@DontReflect
public class LayeredMutableAttributes<K> implements MutableAttributes<K> {
    /**
     * 用于标记属性不存在的值。
     */
    private static final Object NON_VALUE = new Object();

    /**
     * 父级属性。
     */
    @Getter
    private final Attributes<K> parent;

    /**
     * 已经删除的属性的 Key 的集合。
     */
    @Getter(lombok.AccessLevel.PROTECTED)
    private final Set<K> keysRemoved = new HashSet<>(BufferSizes.SMALL);

    /**
     * 自定义属性的字典。
     */
    @Getter(lombok.AccessLevel.PROTECTED)
    private final Map<K, Object> custom = new HashMap<>(BufferSizes.SMALL);

    public LayeredMutableAttributes(Attributes<K> parent) {
        this.parent = parent;
    }

    protected Attributes<K> getParentOrEmpty() {
        return this.parent == null ? Attributes.empty() : this.parent;
    }

    @Override
    public Collection<K> getKeys() {
        Collection<K> parentKeys = this.getParentOrEmpty().getKeys();
        int size = this.custom.size() + parentKeys.size() - this.keysRemoved.size();
        if (size <= 0) {
            return Collections.emptySet();
        }

        // 计算 Keys 集合
        Set<K> keys = new HashSet<>(MapUtil.hashInitCapacity(size));
        keys.addAll(this.custom.keySet());
        keys.addAll(parentKeys);
        keys.removeAll(this.keysRemoved);

        // 返回结果
        return keys;
    }

    public boolean isAttributeRemoved(K key) {
        return this.keysRemoved.contains(key);
    }

    @Override
    public boolean hasAttribute(K key) {
        // 检查是否被删除
        if (this.isAttributeRemoved(key)) {
            return false;
        }

        // 检查是否包含在自定义属性字典中
        if (this.custom.containsKey(key)) {
            return true;
        }

        // 检查是否包含在父级属性中
        return this.getParentOrEmpty().hasAttribute(key);
    }

    @Override
    public <V> V getAttribute(K key) {
        return this.getAttribute(key, (V) null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getAttribute(K key, V defaultValue) {
        // 检查是否被删除
        if (this.isAttributeRemoved(key)) {
            return defaultValue;
        }

        // 尝试从自定义属性字典中获取
        Object value = this.custom.getOrDefault(key, NON_VALUE);
        if (value == NON_VALUE) {
            // 尝试从父属性中获取
            value = this.getParentOrEmpty().getAttribute(key, NON_VALUE);
        }

        // 返回结果
        return (value != NON_VALUE) ? (V) value : defaultValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getAttribute(K key, Function<K, V> defaultValueSupplier) {
        Object value = this.getAttribute(key, NON_VALUE);
        return (value != NON_VALUE) ? (V) value : defaultValueSupplier.apply(key);
    }

    @Override
    public Map<K, ?> asMap() {
        Map<K, ?> parentMap = this.getParentOrEmpty().asMap();
        int size = parentMap.size() + this.custom.size() - this.keysRemoved.size();
        if (size <= 0) {
            return Collections.emptyMap();
        }

        // 合并 Map
        Map<K, Object> map = new HashMap<>(MapUtil.hashInitCapacity(size));
        map.putAll(parentMap);
        map.putAll(this.custom);
        map.keySet().removeAll(this.keysRemoved);

        // 返回结果
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void forEach(BiConsumer<K, ?> consumer) {
        this.asMap().forEach((BiConsumer<K, Object>) consumer);
    }

    @Override
    public void setAttribute(K key, Object value) {
        this.custom.put(key, value);
        this.keysRemoved.remove(key);
    }

    @Override
    public <V> V updateAttribute(K key, BiFunction<K, V, V> updater) {
        V oldValue = this.getAttribute(key);
        V newValue = updater.apply(key, oldValue);
        this.setAttribute(key, newValue);
        return newValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V setAttributeIfAbsent(K key, V fallbackValue) {
        // 获取当前的属性值
        Object value = this.getAttribute(key, NON_VALUE);
        if (value == NON_VALUE) {
            // 如果不存在的话，设置为指定值
            this.setAttribute(key, fallbackValue);
        }
        // 返回结果
        return (value != NON_VALUE) ? (V) value : fallbackValue;
    }

    @Override
    public void removeAttribute(K key) {
        this.custom.remove(key);
        this.keysRemoved.add(key);
    }

    @Override
    public void clearAttributes() {
        this.custom.clear();
        this.keysRemoved.clear();
        this.keysRemoved.addAll(this.getParentOrEmpty().getKeys());
    }

    @Override
    public void importMap(Map<K, ?> map, boolean overwrite) {
        // 检查参数
        if (CollectionUtils.isEmpty(map) || map == this.custom) {
            return;
        }

        // 导入 map 中的属性
        if (overwrite) {
            // 如果覆盖的话，直接更新 custom 和 keysRemoved 即可
            this.custom.putAll(map);
            this.keysRemoved.removeAll(map.keySet());
        } else {
            // 否则的话，逐个导入不存在的属性
            map.forEach(this::setAttributeIfAbsent);
        }
    }
}
