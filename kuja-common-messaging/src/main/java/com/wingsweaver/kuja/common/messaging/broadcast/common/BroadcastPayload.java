package com.wingsweaver.kuja.common.messaging.broadcast.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 广播消息的实际载荷。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BroadcastPayload extends AbstractBroadcastData {
    /**
     * 发送者的信息。
     */
    private Map<String, String> sender;

    /**
     * 附加数据。
     */
    private Map<String, String> tags;

    /**
     * 消息内容的类名。
     */
    private String contentClassName;

    /**
     * 消息内容（JSON 编码）。
     */
    private String content;

    /**
     * 消息接收者（JSON 编码）。
     */
    private String target;
}
