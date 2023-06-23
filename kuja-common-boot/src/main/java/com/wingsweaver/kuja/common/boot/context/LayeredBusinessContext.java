package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.model.attributes.LayeredMutableAttributes;

/**
 * 基于 {@link LayeredMutableAttributes} 的业务上下文实现。
 *
 * @author wingsweaver
 */
public class LayeredBusinessContext extends LayeredMutableAttributes<String> implements BusinessContext {
    public LayeredBusinessContext(BusinessContext parent) {
        super(parent);
    }

    @Override
    public BusinessContext getParent() {
        return (BusinessContext) super.getParent();
    }
}
