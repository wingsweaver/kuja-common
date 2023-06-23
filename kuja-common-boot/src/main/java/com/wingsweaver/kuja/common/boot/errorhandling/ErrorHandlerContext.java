package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.model.attributes.MutableAttributes;

import java.util.function.Supplier;

/**
 * 错误处理上下文的接口定义。
 *
 * @author wingsweaver
 */
public interface ErrorHandlerContext extends MutableAttributes<String> {
    /**
     * 获取业务上下文。
     *
     * @return 业务上下文
     */
    BusinessContext getBusinessContext();

    /**
     * 获取要处理的错误。
     *
     * @return 要处理的错误
     */
    <E extends Throwable> E getInputError();

    /**
     * 获取要输出的错误。
     *
     * @param <E> 要输出的错误类型
     * @return 要输出的错误
     */
    <E extends Throwable> E getOutputError();

    /**
     * 设置要输出的错误。
     *
     * @param error 要输出的错误
     */
    void setOutputError(Throwable error);

    /**
     * 获取是否已经处理。
     *
     * @return 如果已经处理则返回 true，否则返回 false
     */
    boolean isHandled();

    /**
     * 设置是否已经处理。
     *
     * @param handled 是否已经处理
     */
    void setHandled(boolean handled);

    /**
     * 获取返回值。
     *
     * @param <T> 返回值类型
     * @return 返回值
     */
    <T> T getReturnValue();

    /**
     * 设置返回值。
     *
     * @param returnValue 返回值
     */
    void setReturnValue(Object returnValue);

    /**
     * 设置返回值，如果不存在则设置。
     *
     * @param supplier 返回值的提供者
     * @param <T>      返回值类型
     * @return 返回值
     */
    <T> T setReturnValueIfAbsent(Supplier<T> supplier);
}
