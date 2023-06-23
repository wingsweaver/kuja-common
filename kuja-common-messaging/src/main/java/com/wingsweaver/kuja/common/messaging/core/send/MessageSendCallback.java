package com.wingsweaver.kuja.common.messaging.core.send;

/**
 * 发送消息的回调处理。
 *
 * @author wingsweaver
 */
public interface MessageSendCallback {
    /**
     * 开始发送。
     *
     * @param context 消息发送的上下文
     */
    void onStart(MessageSendContext context);

    /**
     * 发送成功。
     *
     * @param context 消息发送的上下文
     */
    void onSuccess(MessageSendContext context);

    /**
     * 发送失败。
     *
     * @param context 消息发送的上下文
     * @param error   失败原因
     */
    void onFail(MessageSendContext context, Throwable error);
}
