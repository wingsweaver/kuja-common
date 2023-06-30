package com.wingsweaver.kuja.common.messaging.broadcast.receive;

import com.wingsweaver.kuja.common.boot.appinfo.matcher.AppInfoMatcherSpecUtil;
import com.wingsweaver.kuja.common.messaging.broadcast.common.BroadcastPayload;
import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.support.json.JsonObjectWrapper;

/**
 * {@link BroadcastNotification} 转换工具类。
 *
 * @author wingsweaver
 */
public final class BroadcastNotificationConverter {
    private BroadcastNotificationConverter() {
        // 禁止实例化
    }

    /**
     * 将 {@link BroadcastPayload} 转换成 {@link BroadcastNotification} 实例。
     *
     * @param payload BroadcastPayload 实例
     * @return BroadcastNotification 实例
     */
    public static BroadcastNotification from(BroadcastPayload payload) {
        if (payload == null) {
            return null;
        }

        try {
            BroadcastNotification notification = new BroadcastNotification();

            // 复制数据
            notification.setCreationTime(payload.getCreationTime());
            notification.setId(payload.getId());
            notification.setMessageType(payload.getMessageType());
            notification.setContent(new JsonObjectWrapper(payload.getContent(), payload.getContentClassName()));
            notification.setSender(payload.getSender());
            notification.setTags(payload.getTags());
            notification.setTarget(AppInfoMatcherSpecUtil.fromJsonString(payload.getTarget()));

            // 返回
            return notification;
        } catch (Exception ex) {
            throw new ExtendedRuntimeException("Failed to create notification.", ex)
                    .withExtendedAttribute("payload", payload);
        }
    }
}
