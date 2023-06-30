package com.wingsweaver.kuja.common.messaging.broadcast.receive;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;

/**
 * 检测是否需要处理指定消息通知的接口定义。<br>
 * 除了校验本服务实例是否在处理消息通知的目标范围内，还可以校验消息通知的内容是否符合要求。<br>
 * 比如，可以定制一个校验器，检查 {@code 停止服务/Stop} 消息是否有有效签名。
 *
 * @author wingsweaver
 */
public interface BroadcastNotificationPredicate extends DefaultOrdered {
    /**
     * 检测是否需要处理指定消息通知。
     *
     * @param notification 消息通知
     * @return 是否需要处理
     */
    boolean shouldDispatch(BroadcastNotification notification);
}
