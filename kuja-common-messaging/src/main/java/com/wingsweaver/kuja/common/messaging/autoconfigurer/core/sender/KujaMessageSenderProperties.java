package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.sender;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
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

    /**
     * 用于发送消息的 Executor 的 Bean 名称。<br>
     * 如果设置的话，消息发送将会在该 Executor 中异步执行，否则将会在当前线程中同步执行。
     */
    private String sendExecutor;
}
