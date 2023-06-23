package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.attributes.MapMutableAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于 {@link MapMutableAttributes} 的 {@link BusinessContext} 实现类。
 *
 * @author wingsweaver
 */
public class MapBusinessContext extends MapMutableAttributes<String> implements BusinessContext {
    public MapBusinessContext(Map<String, ?> map) {
        super(map);
    }

    public MapBusinessContext(int initCapacity) {
        this(new HashMap<>(initCapacity));
    }

    public MapBusinessContext() {
        this(BufferSizes.SMALL);
    }
}
