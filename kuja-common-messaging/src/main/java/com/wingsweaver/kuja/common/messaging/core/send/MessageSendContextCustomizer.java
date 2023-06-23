package com.wingsweaver.kuja.common.messaging.core.send;

/**
 * {@link MessageSendContext} 的定制器的接口定义。
 *
 * @author wingsweaver
 */
public interface MessageSendContextCustomizer {
    /**
     * 定制 {@link MessageSendContext}。
     *
     * @param context MessageSendContext 实例
     */
    void customize(MessageSendContext context);
}
