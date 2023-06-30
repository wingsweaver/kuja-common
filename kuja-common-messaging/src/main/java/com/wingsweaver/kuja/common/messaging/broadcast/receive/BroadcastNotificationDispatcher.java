package com.wingsweaver.kuja.common.messaging.broadcast.receive;

import com.wingsweaver.kuja.common.messaging.broadcast.common.BroadcastPayload;

/**
 * 接收消息的分发器的接口定义。
 *
 * @author wingsweaver
 */
public interface BroadcastNotificationDispatcher {
    /**
     * 分发通知消息。
     *
     * @param notification 通知消息
     */
    void dispatch(BroadcastNotification notification);

    /**
     * 分发通知消息。
     *
     * @param payload 通知消息的正文
     */
    void dispatch(BroadcastPayload payload);
}
