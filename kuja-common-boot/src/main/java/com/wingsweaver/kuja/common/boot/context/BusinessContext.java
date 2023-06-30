package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.model.context.Context;

import java.util.Objects;

/**
 * 业务上下文的接口定义。
 *
 * @author wingsweaver
 */
public interface BusinessContext extends Context {
    /**
     * 获取业务上下文的类型。
     *
     * @return 业务上下文的类型
     */
    BusinessContextType getContextType();

    /**
     * 检查是否是指定类型（实例）的业务上下文。
     *
     * @param target 目标类型实例
     * @return 是否匹配
     */
    default boolean isContextType(Object target) {
        return Objects.equals(this.getContextType(), target);
    }

    /**
     * 检查是否是指定类型的业务上下文。
     *
     * @param clazz 目标类型
     * @return 是否匹配
     */
    default boolean isContextType(Class<?> clazz) {
        BusinessContextType contextType = this.getContextType();
        if (contextType == null || clazz == null) {
            return false;
        } else {
            return clazz.isInstance(contextType);
        }
    }
}
