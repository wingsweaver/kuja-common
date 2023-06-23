package com.wingsweaver.kuja.common.utils.model.attributes;

import java.util.Map;

/**
 * {@link Attributes} 的构造器。
 *
 * @param <K> Key
 * @author wingsweaver
 */
public class AttributesBuilder<K> extends AbstractAttributesBuilder<K, AttributesBuilder<K>> {
    public AttributesBuilder(Map<K, ?> map) {
        super(map);
    }

    public AttributesBuilder(Attributes<K> attributes) {
        super(attributes.asMap());
    }

    /**
     * 构建一个 {@link Attributes} 实例。
     *
     * @return Attributes 实例
     */
    public Attributes<K> build() {
        return new MapAttributes<>(this.map);
    }
}
