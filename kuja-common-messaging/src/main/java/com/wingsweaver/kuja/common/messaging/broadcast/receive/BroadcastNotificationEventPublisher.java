package com.wingsweaver.kuja.common.messaging.broadcast.receive;

import com.wingsweaver.kuja.common.utils.model.AbstractComponent;

/**
 * 使用 {@link org.springframework.context.ApplicationContext#publishEvent(Object)} 发布广播消息的处理实现类。
 *
 * @author wingsweaver
 */
public class BroadcastNotificationEventPublisher extends AbstractComponent implements BroadcastNotificationHandler {
    @Override
    public void onBroadcastNotification(BroadcastNotification notification) {
        this.getApplicationContext().publishEvent(notification);
    }
}
