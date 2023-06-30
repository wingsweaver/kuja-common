package com.wingsweaver.kuja.common.messaging.errorreporting.common;

import com.wingsweaver.kuja.common.messaging.common.AbstractMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 错误记录消息的有效内容。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ErrorRecordPayload extends AbstractMessage {
    /**
     * 发送者。
     */
    private Map<String, String> sender;

    /**
     * 错误信息。
     */
    private Map<String, Object> error;

    /**
     * 错误的附加信息。
     */
    private Map<String, Object> errorTags;

    @Override
    public String toString() {
        return this.getClass().getTypeName() + "#" + this.getId();
    }
}
