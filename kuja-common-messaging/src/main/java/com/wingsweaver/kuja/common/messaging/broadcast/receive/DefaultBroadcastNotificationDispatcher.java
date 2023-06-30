package com.wingsweaver.kuja.common.messaging.broadcast.receive;

import com.wingsweaver.kuja.common.messaging.broadcast.common.BroadcastPayload;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 默认的 {@link BroadcastNotificationDispatcher} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultBroadcastNotificationDispatcher extends AbstractComponent implements BroadcastNotificationDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBroadcastNotificationDispatcher.class);

    /**
     * BroadcastNotificationPredicate 的实例。
     */
    private BroadcastNotificationPredicate notificationPredicate;

    /**
     * 广播消息的处理器的实例的列表。
     */
    private List<BroadcastNotificationHandler> handlers;

    @Override
    public void dispatch(BroadcastNotification notification) {
        // 检查是否应该处理
        if (!this.shouldDispatch(notification)) {
            LogUtil.trace(LOGGER, "The notification should not be dispatched. notification={}", notification);
            return;
        }

        // 遍历 handlers，调用 handler 的 onBroadcastNotification 方法
        for (BroadcastNotificationHandler handler : this.handlers) {
            try {
                handler.onBroadcastNotification(notification);
            } catch (Exception ex) {
                LogUtil.error(LOGGER, ex, "Failed to handle broadcast notification: {} with handler {}", notification, handler);
            }
        }
    }

    /**
     * 检查是否应该处理指定的广播消息。<br>
     * 主要是检查广播消息的 target 是否匹配当前应用程序的信息。<br>
     * 没有做发送者是否时当前应用程序的检查，因为不确定发送者是否不需要处理。
     *
     * @param notification 广播消息
     * @return 是否应该处理
     */
    protected boolean shouldDispatch(BroadcastNotification notification) {
        return this.notificationPredicate.shouldDispatch(notification);
    }

    @Override
    public void dispatch(BroadcastPayload payload) {
        BroadcastNotification notification = this.createNotification(payload);
        this.dispatch(notification);
    }

    /**
     * 根据广播消息的 payload 创建广播消息。
     *
     * @param payload 广播消息的 payload
     * @return 广播消息
     */
    protected BroadcastNotification createNotification(BroadcastPayload payload) {
        return BroadcastNotificationConverter.from(payload);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 notificationPredicate
        this.initNotificationPredicate();

        // 初始化 handlers
        this.initHandlers();
    }

    /**
     * 初始化 notificationPredicate。
     */
    protected void initNotificationPredicate() {
        if (this.notificationPredicate == null) {
            this.notificationPredicate = this.getBean(BroadcastNotificationPredicate.class);
        }
    }

    /**
     * 初始化 handlers。
     */
    protected void initHandlers() {
        if (this.handlers == null) {
            this.handlers = this.getBeansOrdered(BroadcastNotificationHandler.class);
        }
    }
}
