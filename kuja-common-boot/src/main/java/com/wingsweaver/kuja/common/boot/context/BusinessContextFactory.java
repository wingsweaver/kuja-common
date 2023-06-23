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
     * 默认的 {@link BusinessContextFactory} 实例。
     */
    BusinessContextFactory DEFAULT = MapBusinessContext::new;
}
