package com.wingsweaver.kuja.common.messaging.broadcast.receive.built;

import com.wingsweaver.kuja.common.messaging.broadcast.common.BuiltInMessageTypes;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotification;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.refresh.ContextRefresher;

/**
 * 内置 Spring Cloud 消息的处理器。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@SuppressWarnings("PMD.GuardLogStatement")
public class CloudContextBroadcastNotificationHandler extends AbstractBuiltInNotificationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudContextBroadcastNotificationHandler.class);

    @Override
    public void onBroadcastNotification(BroadcastNotification notification) throws Exception {
        String messageType = notification.getMessageType();
        if (StringUtil.isEmpty(messageType)) {
            return;
        }

        if (BuiltInMessageTypes.REFRESH_APPLICATION_CONTEXT.equals(messageType)) {
            this.refreshApplicationContext(notification);
        }
    }

    private void refreshApplicationContext(BroadcastNotification notification) {
        // 检查是否被禁用
        if (this.isDisabled(notification)) {
            return;
        }

        LogUtil.trace(LOGGER, "Refreshing application context on notification #{}", notification.getId());
        ContextRefresher contextRefresher = this.getBean(ContextRefresher.class, false);
        contextRefresher.refresh();
    }
}
