package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextAccessor;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 异常处理器管理器。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultErrorHandlingDelegate extends AbstractComponent implements ErrorHandlingDelegate {
    /**
     * 错误处理器上下文的定制器列表。
     */
    private List<ErrorHandlerContextCustomizer> contextCustomizers;

    /**
     * 错误处理器列表。
     */
    private List<ErrorHandler> errorHandlers;

    @Override
    public ErrorHandlerContext handleError(BusinessContext businessContext, Throwable error,
                                           ErrorHandlerContextCustomizer preProcessor,
                                           ErrorHandlerContextCustomizer postProcessor) {
        // 创建异常处理上下文
        ErrorHandlerContext context = this.createErrorHandlerContext(businessContext, error, preProcessor);

        // 执行异常处理
        this.handleError(context);

        // 执行后处理
        if (postProcessor != null) {
            postProcessor.customize(context);
        }

        // 返回异常处理上下文
        return context;
    }

    /**
     * 处理异常。
     *
     * @param context 错误处理上下文
     */
    protected void handleError(ErrorHandlerContext context) {
        for (ErrorHandler errorHandler : this.errorHandlers) {
            if (errorHandler.handle(context)) {
                break;
            }
        }
    }

    /**
     * 创建错误处理上下文，并执行定制处理（初始化）。
     *
     * @param businessContext 业务上下文
     * @param error           要处理的异常
     * @param preProcessor    预处理的定制器
     * @return 错误处理上下文
     */
    @Override
    public ErrorHandlerContext createErrorHandlerContext(BusinessContext businessContext, Throwable error,
                                                         ErrorHandlerContextCustomizer preProcessor) {
        // 创建错误处理上下文
        ErrorHandlerContext context = this.createContext(businessContext, error);

        // 定制错误处理上下文（使用自定义的定制设置）
        if (preProcessor != null) {
            preProcessor.customize(context);
        }

        // 定制错误处理上下文（使用内置的定制设置）
        this.customizeContext(context);

        // 返回
        return context;
    }

    /**
     * 创建错误处理上下文。
     *
     * @param businessContext 业务上下文
     * @param error           要处理的异常
     * @return 错误处理上下文
     */
    protected ErrorHandlerContext createContext(BusinessContext businessContext, Throwable error) {
        // 关联业务上下文和异常
        BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
        accessor.setErrorIfAbsent(error);

        // 创建错误处理上下文
        DefaultErrorHandlerContext context = new DefaultErrorHandlerContext();
        context.setBusinessContext(businessContext);
        context.setInputError(error);
        context.setOutputError(error);

        ErrorHandlerContextAccessor errorHandlerContextAccessor = new ErrorHandlerContextAccessor(context);
        errorHandlerContextAccessor.setThread(Thread.currentThread());

        // 返回
        return context;
    }

    /**
     * 定制错误处理上下文。
     *
     * @param context 错误处理上下文
     */
    protected void customizeContext(ErrorHandlerContext context) {
        this.contextCustomizers.forEach(customizer -> customizer.customize(context));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 contextCustomizers
        this.initContextCustomizers();

        // 初始化 errorHandlers
        this.initErrorHandlers();
    }

    /**
     * 初始化 contextCustomizers。
     */
    protected void initContextCustomizers() {
        if (this.contextCustomizers == null) {
            this.contextCustomizers = this.getBeansOrdered(ErrorHandlerContextCustomizer.class);
        }
    }

    /**
     * 初始化 errorHandlers。
     */
    protected void initErrorHandlers() {
        if (this.errorHandlers == null) {
            this.errorHandlers = this.getBeansOrdered(ErrorHandler.class);
        }
    }
}
