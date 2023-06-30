package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 业务上下文的定制处理器的接口定义。
 *
 * @author wingsweaver
 */
public interface BusinessContextCustomizer extends DefaultOrdered {
    /**
     * 定制业务上下文。
     *
     * @param businessContext 业务上下文
     */
    void customize(BusinessContext businessContext);
}
