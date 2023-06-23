package com.wingsweaver.kuja.common.messaging.core.send.rocketmq;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 计算消息目标的顺序发送的 HashKey 的接口定义。
 *
 * @author wingsweaver
 */
public interface OrderHashKeyResolver extends DefaultOrdered {
    /**
     * 计算消息目标的顺序发送的 HashKey。
     *
     * @param context 消息发送的上下文
     * @return 顺序发送的 HashKey。
     */
    String resolveOrderHashKey(MessageSendContext context);
}
