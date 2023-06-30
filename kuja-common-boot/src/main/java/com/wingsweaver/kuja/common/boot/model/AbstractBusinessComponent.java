package com.wingsweaver.kuja.common.boot.model;

import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorDefinition2ReturnValuePatcher;
import com.wingsweaver.kuja.common.boot.exception.BusinessException;
import com.wingsweaver.kuja.common.boot.exception.BusinessExceptionFactory;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueT;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 业务组件的基类。<br><br>
 * 此基类提供以下基础能力：<br>
 * 1. 基于 {@link ReturnValueFactory} 的创建返回结果的能力。
 * 2. 基于 {@link BusinessExceptionFactory} 创建和抛出 {@link BusinessException} 的能力。<br>
 * <br>
 * 其实现类，一般被 {@link org.springframework.stereotype.Component} 注解标记。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractBusinessComponent extends AbstractComponent {
    /**
     * ReturnValueFactory 实例。
     */
    private ReturnValueFactory returnValueFactory;

    /**
     * BusinessExceptionFactory 实例。
     */
    private BusinessExceptionFactory businessExceptionFactory;

    /**
     * ErrorDefinition2ReturnValuePatcher 实例的列表。
     */
    private List<ErrorDefinition2ReturnValuePatcher> errorDefinition2ReturnValuePatchers;

    /**
     * 生成一个成功的返回值。
     *
     * @return 返回值
     */
    protected ReturnValue success() {
        return this.getReturnValueFactory().success();
    }

    /**
     * 生成一个成功的返回值。
     *
     * @param data 返回值中的数据
     * @param <T>  返回值中的数据类型
     * @return 返回值
     */
    protected <T> ReturnValueT<T> success(T data) {
        return this.getReturnValueFactory().success(data);
    }

    /**
     * 补全返回值中的成功信息。
     *
     * @param returnValue 返回值
     */
    protected void patchSuccess(ReturnValue returnValue) {
        this.getReturnValueFactory().patchSuccess(returnValue);
    }

    /**
     * 生成一个失败的返回值。
     *
     * @return 返回值
     */
    protected ReturnValue fail() {
        return this.getReturnValueFactory().fail();
    }

    /**
     * 生成一个失败的返回值。
     *
     * @param error 发生的错误
     * @return 返回值
     */
    protected ReturnValue fail(Throwable error) {
        ReturnValue returnValue = new ReturnValue();
        this.patchFail(returnValue, error);
        return returnValue;
    }

    /**
     * 补全返回值中的成功信息。
     *
     * @param returnValue 返回值
     * @param error       发生的错误
     */
    protected void patchFail(ReturnValue returnValue, Throwable error) {
        // 针对 BusinessException，先使用 errorDefinition2ReturnValuePatchers 补全返回值
        if (error instanceof BusinessException) {
            ErrorDefinition errorDefinition = ((BusinessException) error).getErrorDefinition();
            if (errorDefinition != null) {
                this.errorDefinition2ReturnValuePatchers.forEach(patcher -> patcher.patch(errorDefinition, returnValue));
            }
        }

        // 使用 returnValueFactory 默认的补全逻辑
        this.getReturnValueFactory().patchFail(returnValue, error);
    }

    /**
     * 生成一个失败的返回值。
     *
     * @param cause           发生的错误的根因
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @return 返回值
     */
    protected ReturnValue fail(Throwable cause, ErrorDefinition errorDefinition, Object... args) {
        AssertArgs.Named.notNull("errorDefinition", errorDefinition);
        BusinessException businessException = this.createBusinessException(cause, errorDefinition, args);
        return this.fail(businessException);
    }

    /**
     * 生成一个失败的返回值。
     *
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @return 返回值
     */
    protected ReturnValue fail(ErrorDefinition errorDefinition, Object... args) {
        return fail(null, errorDefinition, args);
    }

    /**
     * 生成一个失败的返回值。
     *
     * @param error               发生的错误
     * @param errorDefinitionCode 错误定义代码
     * @param args                消息格式化参数
     * @return 返回值
     */
    protected ReturnValue fail(Throwable error, String errorDefinitionCode, Object... args) {
        AssertArgs.Named.notEmpty("errorDefinitionCode", errorDefinitionCode);
        BusinessException businessException = this.createBusinessException(error, errorDefinitionCode, args);
        return this.fail(businessException);
    }

    /**
     * 生成一个失败的返回值。
     *
     * @param errorDefinitionCode 错误定义代码
     * @param args                消息格式化参数
     * @return 返回值
     */
    protected ReturnValue fail(String errorDefinitionCode, Object... args) {
        return fail(null, errorDefinitionCode, args);
    }

    /**
     * 创建一个 {@link BusinessException}。
     *
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @return PreDefinedBusinessException 实例
     */
    protected BusinessException createBusinessException(ErrorDefinition errorDefinition, Object... args) {
        return this.businessExceptionFactory.create(errorDefinition, args);
    }

    /**
     * 创建一个 {@link BusinessException}。
     *
     * @param cause           异常原因
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @return PreDefinedBusinessException 实例
     */
    protected BusinessException createBusinessException(Throwable cause, ErrorDefinition errorDefinition, Object... args) {
        return this.businessExceptionFactory.create(cause, errorDefinition, args);
    }

    /**
     * 创建一个 {@link BusinessException}。
     *
     * @param errorDefinitionCode 错误定义的编码
     * @param args                消息格式化参数
     * @return PreDefinedBusinessException 实例
     */
    protected BusinessException createBusinessException(String errorDefinitionCode, Object... args) {
        return this.businessExceptionFactory.create(errorDefinitionCode, args);
    }

    /**
     * 创建一个 {@link BusinessException}。
     *
     * @param cause               异常原因
     * @param errorDefinitionCode 错误定义的编码
     * @param args                消息格式化参数
     * @return PreDefinedBusinessException 实例
     */
    protected BusinessException createBusinessException(Throwable cause, String errorDefinitionCode, Object... args) {
        return this.businessExceptionFactory.create(cause, errorDefinitionCode, args);
    }

    /**
     * 抛出一个 {@link BusinessException}。
     *
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @throws BusinessException 发生业务异常
     */
    protected void throwBusinessException(ErrorDefinition errorDefinition, Object... args) throws BusinessException {
        throw this.createBusinessException(errorDefinition, args);
    }

    /**
     * 抛出一个 {@link BusinessException}。
     *
     * @param cause           异常原因
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @throws BusinessException 发生业务异常
     */
    protected void throwBusinessException(Throwable cause, ErrorDefinition errorDefinition, Object... args) throws BusinessException {
        throw this.createBusinessException(cause, errorDefinition, args);
    }

    /**
     * 抛出一个 {@link BusinessException}。
     *
     * @param errorDefinitionCode 错误定义的编码
     * @param args                消息格式化参数
     * @throws BusinessException 发生业务异常
     */
    protected void throwBusinessException(String errorDefinitionCode, Object... args) throws BusinessException {
        throw this.createBusinessException(errorDefinitionCode, args);
    }

    /**
     * 抛出一个 {@link BusinessException}。
     *
     * @param cause               异常原因
     * @param errorDefinitionCode 错误定义的编码
     * @param args                消息格式化参数
     * @throws BusinessException 发生业务异常
     */
    protected void throwBusinessException(Throwable cause, String errorDefinitionCode, Object... args) throws BusinessException {
        throw this.createBusinessException(cause, errorDefinitionCode, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 returnValueFactory
        this.initReturnValueFactory();

        // 初始化 businessExceptionFactory
        this.initBusinessExceptionFactory();

        // 初始化 errorDefinition2ReturnValuePatchers
        this.initErrorDefinition2ReturnValuePatchers();
    }

    /**
     * 初始化 errorDefinition2ReturnValuePatchers。
     */
    protected void initErrorDefinition2ReturnValuePatchers() {
        if (this.errorDefinition2ReturnValuePatchers == null) {
            this.errorDefinition2ReturnValuePatchers = this.getBeansOrdered(ErrorDefinition2ReturnValuePatcher.class);
        }
    }

    /**
     * 初始化 businessExceptionFactory。
     */
    protected void initBusinessExceptionFactory() {
        if (this.businessExceptionFactory == null) {
            this.businessExceptionFactory = this.getBean(BusinessExceptionFactory.class);
        }
    }

    /**
     * 初始化 returnValueFactory。
     */
    protected void initReturnValueFactory() {
        if (this.returnValueFactory == null) {
            this.returnValueFactory = this.getBean(ReturnValueFactory.class);
        }
    }
}
