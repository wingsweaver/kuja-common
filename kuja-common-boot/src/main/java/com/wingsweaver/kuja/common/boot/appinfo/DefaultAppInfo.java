package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.attributes.MapMutableAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的 {@link AppInfo} 实现类。
 *
 * @author wingsweaver
 */
public class DefaultAppInfo extends MapMutableAttributes<String> implements AppInfo {
    /**
     * 构造函数。
     */
    public DefaultAppInfo() {
        super(new HashMap<>(BufferSizes.SMALL));
    }

    /**
     * 构造函数。
     *
     * @param map 属性值的映射
     */
    public DefaultAppInfo(Map<String, ?> map) {
        super(map);
    }
}
