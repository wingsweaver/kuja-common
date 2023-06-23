package com.wingsweaver.kuja.common.messaging.autoconfigurer.sender;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 消息发送配置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingKeys.PREFIX_MESSAGE_SENDER)
public class KujaMessageSenderProperties extends AbstractPojo {
    /**
     * {@code DefaultMessageSender#getContextIdGenerator()} 的 Bean 名称。
     */
    private String contextIdGenerator;
}
