package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.model.attributes.MutableAttributes;

import java.util.Map;

/**
 * 业务上下文的接口定义。
 *
 * @author wingsweaver
 */
public interface BusinessContext extends MutableAttributes<String> {
    /**
     * 创建一个新的空的业务上下文。
     *
     * @return 新的业务上下文
     */
    static BusinessContext create() {
        return new MapBusinessContext();
    }

    /**
     * 创建一个新的业务上下文。
     *
     * @param map 初始的属性映射
     * @return 新的业务上下文
     */
    static BusinessContext of(Map<String, ?> map) {
        return new MapBusinessContext(map);
    }

    /**
     * 复制一个新的业务上下文。
     *
     * @param other 父业务上下文
     * @return 新的业务上下文
     */
    static BusinessContext clone(BusinessContext other) {
        if (other == null) {
            return new MapBusinessContext();
        } else {
            return new MapBusinessContext(other.asMap());
        }
    }

    /**
     * 创建一个新的分层业务上下文。
     *
     * @param parent 父业务上下文
     * @return 新的业务上下文
     */
    static BusinessContext layered(BusinessContext parent) {
        return new LayeredBusinessContext(parent);
    }
}
