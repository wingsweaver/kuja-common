package com.wingsweaver.kuja.common.boot.exception;

import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;

/**
 * {@link BusinessException} 的工厂类的接口定义。
 *
 * @author wingsweaver
 */
public interface BusinessExceptionFactory {
    /**
     * 创建一个 {@link BusinessException}。
     *
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @return PreDefinedBusinessException 实例
     */
    BusinessException create(ErrorDefinition errorDefinition, Object... args);

    /**
     * 创建一个 {@link BusinessException}。
     *
     * @param cause           异常原因
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @return PreDefinedBusinessException 实例
     */
    BusinessException create(Throwable cause, ErrorDefinition errorDefinition, Object... args);

    /**
     * 创建一个 {@link BusinessException}。
     *
     * @param errorDefinitionCode 错误定义的编码
     * @param args                消息格式化参数
     * @return PreDefinedBusinessException 实例
     */
    BusinessException create(String errorDefinitionCode, Object... args);

    /**
     * 创建一个 {@link BusinessException}。
     *
     * @param cause               异常原因
     * @param errorDefinitionCode 错误定义的编码
     * @param args                消息格式化参数
     * @return PreDefinedBusinessException 实例
     */
    BusinessException create(Throwable cause, String errorDefinitionCode, Object... args);
}
