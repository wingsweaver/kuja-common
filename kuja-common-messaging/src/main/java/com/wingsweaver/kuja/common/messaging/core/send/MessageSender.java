package com.wingsweaver.kuja.common.messaging.core.send;

/**
 * 消息发送器的接口定义。
 *
 * @author wingsweaver
 */
public interface MessageSender {
    /**
     * 发送消息。
     *
     * @param message  消息内容
     * @param callback 发送消息的回调处理
     */
    void send(Object message, MessageSendCallback callback);

    /**
     * 发送消息。
     *
     * @param message 消息内容
     */
    void send(Object message);

    /**
     * 向指定目的地发送消息。
     *
     * @param destination 目的地
     * @param message     消息内容
     * @param callback    发送消息的回调处理
     */
    void send(Object destination, Object message, MessageSendCallback callback);

    /**
     * 向默认目的地发送消息。
     *
     * @param destination 目的地
     * @param message     消息内容
     */
    void send(Object destination, Object message);
}
