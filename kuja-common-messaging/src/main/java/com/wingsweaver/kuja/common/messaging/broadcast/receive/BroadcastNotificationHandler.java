package com.wingsweaver.kuja.common.messaging.broadcast.receive;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 广播消息的处理器。
 *
 * @author wingsweaver
 */
public interface BroadcastNotificationHandler extends DefaultOrdered {
    /**
     * 处理广播消息。
     *
     * @param notification 广播消息
     * @throws Exception 处理过程中发生异常
     */
    void onBroadcastNotification(BroadcastNotification notification) throws Exception;
}
