package com.wingsweaver.kuja.common.messaging.core.send;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 消息发送服务的接口定义。
 *
 * @author wingsweaver
 */
public interface MessageSendService extends DefaultOrdered {
    /**
     * 检查是否支持指定类型的消息目的地。
     *
     * @param destination 消息目的地
     * @return 是否支持
     */
    boolean supportDestination(Object destination);

    /**
     * 检查是否支持指定类型的消息。
     *
     * @param message 消息内容
     * @return 是否支持
     */
    boolean supportMessage(Object message);

    /**
     * 使用指定的消息发送上下文，发送消息。
     *
     * @param context  消息发送的上下文
     * @param callback 发送消息的回调处理
     */
    void send(MessageSendContext context, MessageSendCallback callback);
}
