package com.wingsweaver.kuja.common.boot.errordefinition;

/**
 * 用于获取错误定义的处理接口。
 *
 * @author wingsweaver
 */
public interface ErrorDefinitionAttributes {
    /**
     * 计算错误消息。
     *
     * @param cause           异常原因
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @return 错误消息
     */
    String resolveMessage(Throwable cause, ErrorDefinition errorDefinition, Object[] args);

    /**
     * 计算用户提示。
     *
     * @param cause           异常原因
     * @param errorDefinition 错误定义
     * @param args            消息格式化参数
     * @return 用户提示
     */
    String resolveUserTip(Throwable cause, ErrorDefinition errorDefinition, Object[] args);
}
