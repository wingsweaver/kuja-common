package com.wingsweaver.kuja.common.messaging.core.send.rabbit;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;

/**
 * 用于计算 Rabbit Exchange 的路由 Key 的接口定义。
 *
 * @author wingsweaver
 */
public interface RoutingKeyResolver {
    /**
     * 解析 Rabbit Exchange 的路由 Key。
     *
     * @param context 消息发送的上下文
     * @return Rabbit Exchange 的路由 Key
     */
    String resolveRoutingKey(MessageSendContext context);
}
