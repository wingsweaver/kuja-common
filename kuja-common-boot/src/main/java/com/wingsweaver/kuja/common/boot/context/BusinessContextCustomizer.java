package com.wingsweaver.kuja.common.boot.context;

/**
 * 业务上下文的定制处理器的接口定义。
 *
 * @author wingsweaver
 */
public interface BusinessContextCustomizer {
    /**
     * 定制业务上下文。
     *
     * @param businessContext 业务上下文
     */
    void customize(BusinessContext businessContext);
}
