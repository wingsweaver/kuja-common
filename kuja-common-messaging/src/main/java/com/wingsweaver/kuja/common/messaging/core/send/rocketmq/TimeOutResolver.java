package com.wingsweaver.kuja.common.messaging.core.send.rocketmq;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 计算消息目标的发送超时的接口定义。
 *
 * @author wingsweaver
 */
public interface TimeOutResolver extends DefaultOrdered {
    /**
     * 计算消息目标的发送超时。
     *
     * @param context 消息发送的上下文
     * @return 发送超时。
     */
    long resolveTimeout(MessageSendContext context);
}
