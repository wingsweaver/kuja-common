package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.support.tostring.DontReflect;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 属性访问器（辅助工具类）。
 *
 * @param <K> 属性名称的类型
 * @author wingsweaver
 */
@Getter
@Setter
@DontReflect
public class AttributesAccessor<K> {
    private final Attributes<K> attributes;

    /**
     * 构造函数。
     *
     * @param attributes Attributes 实例
     */
    public AttributesAccessor(Attributes<K> attributes) {
        this.attributes = attributes;
    }

    protected Attributes<K> getAttributesOrEmpty() {
        return attributes == null ? Attributes.empty() : attributes;
    }

    public <V> V getAttribute(K key) {
        return this.getAttributesOrEmpty().getAttribute(key);
    }

    public <V> V getAttribute(K key, V defaultValue) {
        return this.getAttributesOrEmpty().getAttribute(key, defaultValue);
    }

    public <V> V getAttribute(K key, Function<K, V> defaultValueSupplier) {
        return this.getAttributesOrEmpty().getAttribute(key, defaultValueSupplier);
    }

    public <V> void setAttribute(K key, V value) {
        this.getMutableAttributes().setAttribute(key, value);
    }

    public <V> V setAttributeIfAbsent(K key, V fallbackValue) {
        return this.getMutableAttributes().setAttributeIfAbsent(key, fallbackValue);
    }

    public <V> V updateAttribute(K key, BiFunction<K, V, V> updater) {
        return this.getMutableAttributes().updateAttribute(key, updater);
    }

    public void removeAttribute(K key) {
        this.getMutableAttributes().removeAttribute(key);
    }

    public MutableAttributes<K> getMutableAttributes() {
        if (this.attributes instanceof MutableAttributes) {
            return (MutableAttributes<K>) attributes;
        }
        throw new UnsupportedOperationException("Attributes is not mutable: " + this.attributes);
    }
}
