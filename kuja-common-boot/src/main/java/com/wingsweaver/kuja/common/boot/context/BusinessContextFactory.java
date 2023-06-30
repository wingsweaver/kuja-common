package com.wingsweaver.kuja.common.boot.context;

/**
 * {@link BusinessContext} 工厂类的接口定义。
 *
 * @author wingsweaver
 */
public interface BusinessContextFactory {
    /**
     * 创建 {@link BusinessContext} 实例。
     *
     * @return 业务上下文的实例
     */
    BusinessContext create();

    /**
     * 创建 {@link BusinessContext} 实例。
     *
     * @param parent 父级上下文
     * @return 业务上下文的实例
     */
    BusinessContext create(BusinessContext parent);
}
