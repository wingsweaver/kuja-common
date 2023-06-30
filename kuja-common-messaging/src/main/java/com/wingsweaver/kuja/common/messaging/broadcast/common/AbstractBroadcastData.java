package com.wingsweaver.kuja.common.messaging.broadcast.common;

import com.wingsweaver.kuja.common.messaging.common.AbstractMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * 广播数据的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractBroadcastData extends AbstractMessage {
    /**
     * 消息类型。
     */
    private String messageType;
}
