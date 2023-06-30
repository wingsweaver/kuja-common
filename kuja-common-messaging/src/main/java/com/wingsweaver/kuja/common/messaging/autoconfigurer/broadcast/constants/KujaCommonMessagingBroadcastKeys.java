package com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.constants;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.receive.BuiltInBroadcastNotificationHandlerProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;

/**
 * kuja-common-messaging 中广播机制的 Key 定义。
 *
 * @author wingsweaver
 */
public interface KujaCommonMessagingBroadcastKeys {
    /**
     * kuja-common-messaging 中广播机制的 Key 前缀。
     */
    String PREFIX_BROADCAST = KujaCommonMessagingKeys.PREFIX_KUJA_COMMON_MESSAGING + ".broadcast";

    /**
     * {@link KujaCommonMessagingKeys}。
     */
    String PREFIX_BROADCAST_SENDER_INFO = PREFIX_BROADCAST + ".sender-info";

    /**
     * {@link BuiltInBroadcastNotificationHandlerProperties}。
     */
    String BUILT_IN_BROADCAST_NOTIFICATION_HANDLER = PREFIX_BROADCAST + ".built-in-notification-handler";
}
