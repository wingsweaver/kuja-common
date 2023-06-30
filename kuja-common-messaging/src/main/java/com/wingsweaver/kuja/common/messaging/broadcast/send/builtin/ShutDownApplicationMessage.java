package com.wingsweaver.kuja.common.messaging.broadcast.send.builtin;

import com.wingsweaver.kuja.common.messaging.broadcast.common.BuiltInMessageTypes;
import com.wingsweaver.kuja.common.messaging.broadcast.send.BroadcastMessage;

/**
 * 优雅地关闭应用程序的消息。
 *
 * @author wingsweaver
 */
public class ShutDownApplicationMessage extends BroadcastMessage {
    public ShutDownApplicationMessage() {
        this.setMessageType(BuiltInMessageTypes.SHUTDOWN_APPLICATION);
    }
}
