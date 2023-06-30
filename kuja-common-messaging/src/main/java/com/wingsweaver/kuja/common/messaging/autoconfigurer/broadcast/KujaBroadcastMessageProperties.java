package com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.constants.KujaCommonMessagingBroadcastKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 广播消息的设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingBroadcastKeys.PREFIX_BROADCAST)
public class KujaBroadcastMessageProperties extends AbstractPojo {
    /**
     * 默认的目的地。
     */
    private String defaultDestination;
}
