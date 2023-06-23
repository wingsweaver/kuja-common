package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.ValueWrapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 带异常处理的组件的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractErrorHandlingComponent implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractErrorHandlingComponent.class);

    /**
     * 异常处理器管理器。
     */
    @Autowired
    private ErrorHandlingDelegate errorHandlingDelegate;

    /**
     * 处理异常。<br>
     * 允许各 Controller 定制自己的异常处理，而无需使用统一的异常处理器。
     *
     * @param businessContext                 业务上下文
     * @param error                           要处理的异常
     * @param errorHandlerContextValueWrapper 异常处理上下文的包装器
     * @return 处理结果
     * @throws Throwable 异常无法处理，继续交给后续异常处理器处理
     */
    public Object handleError(BusinessContext businessContext, Throwable error,
                              ValueWrapper<ErrorHandlerContext> errorHandlerContextValueWrapper) throws Throwable {
        try (BusinessContextHolder.TempHolder ignored = BusinessContextHolder.with(businessContext)) {
            // 执行异常处理
            ErrorHandlerContext context = this.errorHandlingDelegate.handleError(businessContext, error,
                    this::preProcessContext, this::postProcessContext);
            if (errorHandlerContextValueWrapper != null) {
                errorHandlerContextValueWrapper.setValue(context);
            }

            // 返回处理结果
            return this.resolveReturnValue(context);
        }
    }

    /**
     * 处理异常。<br>
     * 允许各 Controller 定制自己的异常处理，而无需使用统一的异常处理器。
     *
     * @param businessContext 业务上下文
     * @param error           要处理的异常
     * @return 处理结果
     * @throws Throwable 异常无法处理，继续交给后续异常处理器处理
     */
    public Object handleError(BusinessContext businessContext, Throwable error) throws Throwable {
        return this.handleError(businessContext, error, null);
    }

    /**
     * 计算返回结果。
     *
     * @param context 异常处理上下文
     * @return 返回结果
     * @throws Throwable 异常无法处理，继续交给后续异常处理器处理
     */
    protected Object resolveReturnValue(ErrorHandlerContext context) throws Throwable {
        if (context.isHandled()) {
            return this.normalizeReturnValue(context, context.getReturnValue());
        } else {
            LogUtil.trace(LOGGER, "error is not handled, rethrow error");
            throw this.resolveFinalError(context);
        }
    }

    /**
     * 计算最终的异常。
     *
     * @param context 异常处理上下文
     * @return 最终的异常
     */
    protected Throwable resolveFinalError(ErrorHandlerContext context) {
        Throwable outputError = context.getOutputError();
        return outputError != null ? outputError : context.getInputError();
    }

    /**
     * 处理返回结果。
     *
     * @param context     异常处理上下文
     * @param returnValue 原始的返回结果
     * @return 处理后的返回结果
     */
    protected Object normalizeReturnValue(ErrorHandlerContext context, Object returnValue) {
        return returnValue;
    }

    /**
     * 后处理错误上下文。
     *
     * @param context 错误处理上下文
     */
    protected void postProcessContext(ErrorHandlerContext context) {
        // 什么也不做
    }

    /**
     * 预处理错误上下文。
     *
     * @param context 错误处理上下文
     */
    protected void preProcessContext(ErrorHandlerContext context) {
        // 什么也不做
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("errorHandlingDelegate", this.getErrorHandlingDelegate());
    }
}
