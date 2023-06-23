package com.wingsweaver.kuja.common.messaging.core.send;

import com.wingsweaver.kuja.common.utils.model.AbstractTagsTemps;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 消息发送的上下文。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MessageSendContext extends AbstractTagsTemps {
    /**
     * 上下文的 ID。
     */
    private String contextId;

    /**
     * 创建时间。
     */
    private Date creationTimeUtc;

    /**
     * 原始的消息。
     */
    private Object originalMessage;
}
