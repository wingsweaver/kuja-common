package com.wingsweaver.kuja.common.messaging.broadcast.send;

import lombok.Getter;
import lombok.Setter;

/**
 * 发送的广播消息。
 *
 * @param <T> 消息内容的类型
 * @author wingsweaver
 */
@Getter
@Setter
public class BroadcastMessageT<T> extends BroadcastMessage {
    /**
     * 消息内容。
     */
    private T content;
}
