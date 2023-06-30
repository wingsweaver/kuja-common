package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;

/**
 * 异常处理过程的代理接口类。
 *
 * @author wingsweaver
 */
public interface ErrorHandlingDelegate {
    /**
     * 创建错误处理上下文。
     *
     * @param businessContext 业务上下文
     * @param error           要处理的异常
     * @param preProcessor    预处理
     * @return 错误处理上下文
     */
    ErrorHandlerContext createErrorHandlerContext(BusinessContext businessContext, Throwable error,
                                                  ErrorHandlerContextCustomizer preProcessor);

    /**
     * 处理异常。
     *
     * @param businessContext 业务上下文
     * @param error           要处理的异常
     * @param preProcessor    预处理
     * @param postProcessor   后处理
     * @return 错误处理上下文
     */
    ErrorHandlerContext handleError(BusinessContext businessContext, Throwable error,
                                    ErrorHandlerContextCustomizer preProcessor,
                                    ErrorHandlerContextCustomizer postProcessor);

    /**
     * 处理异常。
     *
     * @param businessContext 业务上下文
     * @param error           要处理的异常
     * @return 错误处理上下文
     */
    default ErrorHandlerContext handleError(BusinessContext businessContext, Throwable error) {
        return this.handleError(businessContext, error, null, null);
    }
}
