package com.wingsweaver.kuja.common.messaging.autoconfigurer.common;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.core.send.common.AbstractMessageSendService;
import com.wingsweaver.kuja.common.messaging.core.send.common.MessagePredicate;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;

/**
 * 通用的 {@link AbstractMessageSendService} 配置类。
 *
 * @author wingsweaver
 */
public abstract class AbstractSendServiceConfiguration extends AbstractConfiguration {
    /**
     * 配置 {@link AbstractMessageSendService} 实例。
     *
     * @param sendService AbstractMessageSendService 实例
     * @param properties  配置属性
     */
    protected void configureSendService(AbstractMessageSendService<?> sendService,
                                        CommonSendServiceProperties properties) {
        // 设置 DefaultDestination
        if (sendService.getDefaultDestination() == null) {
            sendService.setDefaultDestination(properties.getDefaultDestination());
        }

        // 设置 MessagePredicate
        String messagePredicate = properties.getMessagePredicate();
        if (sendService.getMessagePredicate() == null && StringUtil.isNotEmpty(messagePredicate)) {
            sendService.setMessagePredicate(this.getBean(messagePredicate, MessagePredicate.class, true));
        }
    }
}
