package com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.send;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.constants.KujaCommonMessagingBroadcastKeys;
import com.wingsweaver.kuja.common.messaging.common.DefaultSenderInfoResolver;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * {@link DefaultSenderInfoResolver} 的设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingBroadcastKeys.PREFIX_BROADCAST_SENDER_INFO)
public class SenderInfoResolverProperties extends AbstractPojo {
    /**
     * 默认的导出项。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public static final List<String> DEFAULT_EXPORT_ITEMS = CollectionUtil.listOf(
            ResourceAttributes.Host.KEY_NAME,
            ResourceAttributes.Host.KEY_ADDRESS,
            ResourceAttributes.Process.KEY_PID,
            ResourceAttributes.Service.KEY_NAMESPACE,
            ResourceAttributes.Service.KEY_GROUP,
            ResourceAttributes.Service.KEY_NAME,
            ResourceAttributes.Service.KEY_VERSION,
            ResourceAttributes.Service.KEY_INSTANCE_ID
    );

    /**
     * 需要导出的数据。
     */
    private List<String> exportItems = DEFAULT_EXPORT_ITEMS;
}
