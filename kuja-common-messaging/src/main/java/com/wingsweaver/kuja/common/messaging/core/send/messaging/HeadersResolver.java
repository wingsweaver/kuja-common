package com.wingsweaver.kuja.common.messaging.core.send.messaging;

import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

import java.util.Map;

/**
 * 计算消息头的处理的接口定义。
 *
 * @author wingsweaver
 */
public interface HeadersResolver extends DefaultOrdered {
    /**
     * 解析消息头。
     *
     * @param context 消息发送的上下文
     * @param headers 消息头
     */
    void resolveHeaders(MessageSendContext context, Map<String, Object> headers);
}
