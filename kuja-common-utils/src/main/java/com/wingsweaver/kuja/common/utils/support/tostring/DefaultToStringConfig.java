package com.wingsweaver.kuja.common.utils.support.tostring;

import com.wingsweaver.kuja.common.utils.model.attributes.MapAttributes;

import java.util.Map;

/**
 * 默认的 {@link ToStringConfig} 实现。
 *
 * @author wingsweaver
 */
class DefaultToStringConfig extends MapAttributes<Object> implements ToStringConfig {
    @SuppressWarnings("unchecked")
    public DefaultToStringConfig(Map<?, ?> map) {
        super((Map<Object, ?>) map);
    }
}
