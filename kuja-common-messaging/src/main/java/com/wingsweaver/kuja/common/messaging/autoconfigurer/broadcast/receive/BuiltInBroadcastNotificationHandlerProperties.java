package com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.receive;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.constants.KujaCommonMessagingBroadcastKeys;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.built.BuiltInBroadcastNotificationHandler;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * {@link BuiltInBroadcastNotificationHandler} 的设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingBroadcastKeys.BUILT_IN_BROADCAST_NOTIFICATION_HANDLER)
public class BuiltInBroadcastNotificationHandlerProperties extends AbstractPojo {
    /**
     * 禁用的消息类型。
     */
    private List<String> disabledMessageTypes;
}
