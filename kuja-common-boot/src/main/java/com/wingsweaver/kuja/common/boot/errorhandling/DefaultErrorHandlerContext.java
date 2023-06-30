package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.model.context.MapContext;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 默认的 {@link ErrorHandlerContext} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultErrorHandlerContext extends MapContext implements ErrorHandlerContext {
    public DefaultErrorHandlerContext(Map<String, ?> map) {
        super(map);
    }

    public DefaultErrorHandlerContext(int initCapacity) {
        super(initCapacity);
    }

    public DefaultErrorHandlerContext() {
        // 什么也不做
    }

    public DefaultErrorHandlerContext(ErrorHandlerContext context) {
        super(context);
    }

    /**
     * Key: BusinessContext。
     */
    public static final String KEY_BUSINESS_CONTEXT = ClassUtil.resolveKey(BusinessContext.class);

    @Override
    public BusinessContext getBusinessContext() {
        return this.getAttribute(KEY_BUSINESS_CONTEXT);
    }

    /**
     * 设置业务上下文。
     *
     * @param businessContext 业务上下文
     */
    public void setBusinessContext(BusinessContext businessContext) {
        this.setAttribute(KEY_BUSINESS_CONTEXT, businessContext);
    }

    /**
     * Key: 输入的原始错误。
     */
    public static final String KEY_INPUT_ERROR = ClassUtil.resolveKey(DefaultErrorHandlerContext.class, "inputError");

    @Override
    public <E extends Throwable> E getInputError() {
        return this.getAttribute(KEY_INPUT_ERROR);
    }

    /**
     * 设置输入的原始错误。
     *
     * @param error 输入的原始错误
     */
    public void setInputError(Throwable error) {
        this.setAttribute(KEY_INPUT_ERROR, error);
    }

    /**
     * Key: 输出的待抛出的错误。
     */
    public static final String KEY_OUTPUT_ERROR = ClassUtil.resolveKey(DefaultErrorHandlerContext.class, "outputError");

    @Override
    public <E extends Throwable> E getOutputError() {
        return this.getAttribute(KEY_OUTPUT_ERROR);
    }

    @Override
    public void setOutputError(Throwable error) {
        this.setAttribute(KEY_OUTPUT_ERROR, error);
    }

    /**
     * Key: 是否已经处理。
     */
    public static final String KEY_HANDLED = ClassUtil.resolveKey(DefaultErrorHandlerContext.class, "handled");

    @Override
    public boolean isHandled() {
        return this.getAttribute(KEY_HANDLED, false);
    }

    @Override
    public void setHandled(boolean handled) {
        this.setAttribute(KEY_HANDLED, handled);
    }

    /**
     * Key: 返回结果。
     */
    public static final String KEY_RETURN_VALUE = ClassUtil.resolveKey(DefaultErrorHandlerContext.class, "returnValue");

    @Override
    public <T> T getReturnValue() {
        return this.getAttribute(KEY_RETURN_VALUE);
    }

    @Override
    public void setReturnValue(Object returnValue) {
        this.setAttribute(KEY_RETURN_VALUE, returnValue);
    }

    @Override
    public <T> T setReturnValueIfAbsent(Supplier<T> supplier) {
        return this.updateAttribute(KEY_RETURN_VALUE, (k, v) -> v != null ? v : supplier.get());
    }
}
