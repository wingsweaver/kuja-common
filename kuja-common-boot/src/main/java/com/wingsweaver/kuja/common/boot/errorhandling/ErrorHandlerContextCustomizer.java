package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 错误处理器上下文的定制器的接口定义。
 *
 * @author wingsweaver
 */
public interface ErrorHandlerContextCustomizer extends DefaultOrdered {
    /**
     * 定制错误处理上下文。
     *
     * @param context 错误处理上下文
     */
    void customize(ErrorHandlerContext context);
}
