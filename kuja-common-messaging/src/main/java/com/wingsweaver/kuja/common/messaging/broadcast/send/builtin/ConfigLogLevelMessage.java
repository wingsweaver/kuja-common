package com.wingsweaver.kuja.common.messaging.broadcast.send.builtin;

import com.wingsweaver.kuja.common.messaging.broadcast.common.BuiltInMessageTypes;
import com.wingsweaver.kuja.common.messaging.broadcast.send.BroadcastMessageT;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.logging.LogLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改日志等级。
 *
 * @author wingsweaver
 */
public class ConfigLogLevelMessage extends BroadcastMessageT<List<ConfigLogLevelMessage.Config>> {
    public ConfigLogLevelMessage() {
        this.setMessageType(BuiltInMessageTypes.CONFIG_LOG_LEVEL);
    }

    public List<ConfigLogLevelMessage.Config> getContent(boolean createIfAbsent) {
        List<ConfigLogLevelMessage.Config> content = this.getContent();
        if (content == null && createIfAbsent) {
            content = new ArrayList<>(BufferSizes.TINY);
            this.setContent(content);
        }
        return content;
    }

    @Getter
    @Setter
    public static class Config extends AbstractPojo {
        /**
         * Logger 名称。
         */
        private String name;

        /**
         * Log 级别。
         */
        private LogLevel logLevel;
    }
}
