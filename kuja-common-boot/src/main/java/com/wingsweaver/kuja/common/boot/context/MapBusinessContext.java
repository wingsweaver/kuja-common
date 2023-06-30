package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.model.attributes.MapMutableAttributes;
import com.wingsweaver.kuja.common.utils.model.context.MapContext;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 基于 {@link MapMutableAttributes} 的 {@link BusinessContext} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MapBusinessContext extends MapContext implements BusinessContext, BusinessContextTypeSetter {
    /**
     * 上下文类型。
     */
    private BusinessContextType contextType;

    /**
     * 构造函数。
     *
     * @param map Map 对象
     */
    public MapBusinessContext(Map<String, ?> map) {
        super(map);
    }

    /**
     * 构造函数。
     *
     * @param initCapacity 初始容量
     */
    public MapBusinessContext(int initCapacity) {
        super(initCapacity);
    }

    /**
     * 构造函数。
     */
    public MapBusinessContext() {
        // 什么也不做
    }

    /**
     * 构造函数。
     *
     * @param context BusinessContext 实例
     */
    public MapBusinessContext(BusinessContext context) {
        super(context);
    }
}
