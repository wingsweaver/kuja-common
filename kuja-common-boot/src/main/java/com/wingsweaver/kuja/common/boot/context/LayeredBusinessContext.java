package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.model.attributes.LayeredMutableAttributes;
import com.wingsweaver.kuja.common.utils.model.context.LayeredContext;
import lombok.Getter;
import lombok.Setter;

/**
 * 基于 {@link LayeredMutableAttributes} 的业务上下文实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class LayeredBusinessContext extends LayeredContext implements BusinessContext, BusinessContextTypeSetter {
    /**
     * 上下文类型。
     */
    private BusinessContextType contextType;

    /**
     * 构造函数。
     *
     * @param parent 父级上下文
     */
    public LayeredBusinessContext(BusinessContext parent) {
        super(parent);
    }
}
