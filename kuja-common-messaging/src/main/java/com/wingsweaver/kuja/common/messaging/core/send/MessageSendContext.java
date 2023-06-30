package com.wingsweaver.kuja.common.messaging.core.send;

import com.wingsweaver.kuja.common.utils.model.AbstractTagsTemps;
import com.wingsweaver.kuja.common.utils.model.id.IdSetter;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息发送的上下文。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MessageSendContext extends AbstractTagsTemps implements IdSetter<String> {
    /**
     * 上下文的 ID。
     */
    private String id;

    /**
     * 创建时间。
     */
    private long creationTime = System.currentTimeMillis();

    /**
     * 原始的消息。
     */
    private Object originalMessage;
}
