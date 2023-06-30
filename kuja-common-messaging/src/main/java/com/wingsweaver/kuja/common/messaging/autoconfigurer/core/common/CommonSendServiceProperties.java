package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.common;

import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用的 {@link com.wingsweaver.kuja.common.messaging.core.send.common.AbstractMessageSendService} 设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class CommonSendServiceProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * 默认的消息目的地。
     */
    private Object defaultDestination;

    /**
     * {@link com.wingsweaver.kuja.common.messaging.core.send.common.MessagePredicate} Bean 的名称。
     */
    private String messagePredicate;
}
