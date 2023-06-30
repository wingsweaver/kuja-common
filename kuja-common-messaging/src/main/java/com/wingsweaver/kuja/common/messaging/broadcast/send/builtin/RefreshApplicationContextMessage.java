package com.wingsweaver.kuja.common.messaging.broadcast.send.builtin;

import com.wingsweaver.kuja.common.messaging.broadcast.common.BuiltInMessageTypes;
import com.wingsweaver.kuja.common.messaging.broadcast.send.BroadcastMessage;

/**
 * 刷新上下文的消息。
 *
 * @author wingsweaver
 */
public class RefreshApplicationContextMessage extends BroadcastMessage {
    public RefreshApplicationContextMessage() {
        this.setMessageType(BuiltInMessageTypes.REFRESH_APPLICATION_CONTEXT);
    }
}
