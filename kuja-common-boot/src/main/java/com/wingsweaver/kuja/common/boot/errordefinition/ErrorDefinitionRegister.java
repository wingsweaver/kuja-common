package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

import java.util.Collection;

/**
 * 错误定义的注册器的接口定义。
 *
 * @author wingsweaver
 */
public interface ErrorDefinitionRegister extends DefaultOrdered {
    /**
     * 注册错误定义到指定的集合中。
     *
     * @param errorDefinitions 错误定义集合
     */
    void register(Collection<ErrorDefinition> errorDefinitions);
}
