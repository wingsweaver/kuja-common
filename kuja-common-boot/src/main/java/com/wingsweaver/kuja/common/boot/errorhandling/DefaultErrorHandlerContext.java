package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.attributes.MapMutableAttributes;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 默认的 {@link ErrorHandlerContext} 实现。
 *
 * @author wingsweaver
 */
public class DefaultErrorHandlerContext extends MapMutableAttributes<String> implements ErrorHandlerContext {
    public static final String KEY_BUSINESS_CONTEXT = ClassUtil.resolveKey(BusinessContext.class);

    public DefaultErrorHandlerContext(Map<String, ?> map) {
        super(map);
    }

    public DefaultErrorHandlerContext(int initCapacity) {
        this(new HashMap<>(initCapacity));
    }

    public DefaultErrorHandlerContext() {
        this(BufferSizes.SMALL);
    }

    @Override
    public BusinessContext getBusinessContext() {
        return this.getAttribute(KEY_BUSINESS_CONTEXT);
    }

    public void setBusinessContext(BusinessContext businessContext) {
        this.setAttribute(KEY_BUSINESS_CONTEXT, businessContext);
    }

    public static final String KEY_INPUT_ERROR = ClassUtil.resolveKey(DefaultErrorHandlerContext.class, "inputError");

    @Override
    public <E extends Throwable> E getInputError() {
        return this.getAttribute(KEY_INPUT_ERROR);
    }

    public void setInputError(Throwable error) {
        this.setAttribute(KEY_INPUT_ERROR, error);
    }

    public static final String KEY_OUTPUT_ERROR = ClassUtil.resolveKey(DefaultErrorHandlerContext.class, "outputError");

    @Override
    public <E extends Throwable> E getOutputError() {
        return this.getAttribute(KEY_OUTPUT_ERROR);
    }

    @Override
    public void setOutputError(Throwable error) {
        this.setAttribute(KEY_OUTPUT_ERROR, error);
    }

    public static final String KEY_HANDLED = ClassUtil.resolveKey(DefaultErrorHandlerContext.class, "handled");

    @Override
    public boolean isHandled() {
        return this.getAttribute(KEY_HANDLED, false);
    }

    @Override
    public void setHandled(boolean handled) {
        this.setAttribute(KEY_HANDLED, handled);
    }

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
