package com.wingsweaver.kuja.common.boot.context;

/**
 * 可以设置业务上下文类型的接口定义。
 *
 * @author wingsweaver
 */
public interface BusinessContextTypeSetter {
    /**
     * 设置业务上下文的类型。
     *
     * @param businessContextType 业务上下文的类型
     */
    void setContextType(BusinessContextType businessContextType);
}
