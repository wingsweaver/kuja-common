package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;

/**
 * 用于将错误定义 {@link ErrorDefinition} 中的信息填充到返回值 {@link ReturnValue} 中的处理器接口。
 *
 * @author wingsweaver
 */
public interface ErrorDefinition2ReturnValuePatcher {
    /**
     * 将错误定义 {@link ErrorDefinition} 中的信息填充到返回值 {@link ReturnValue} 中。
     *
     * @param errorDefinition 错误定义
     * @param returnValue     返回值
     */
    void patch(ErrorDefinition errorDefinition, ReturnValue returnValue);
}
