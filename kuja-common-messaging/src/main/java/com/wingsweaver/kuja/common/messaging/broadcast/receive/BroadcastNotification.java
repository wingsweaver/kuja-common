package com.wingsweaver.kuja.common.messaging.broadcast.receive;

import com.wingsweaver.kuja.common.boot.appinfo.matcher.AppInfoMatcherSpec;
import com.wingsweaver.kuja.common.messaging.broadcast.common.AbstractBroadcastData;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.json.JsonObjectWrapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 接收到的广播消息的类型定义。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BroadcastNotification extends AbstractBroadcastData {
    /**
     * 接收的时间戳。
     */
    private long receiveTime = System.currentTimeMillis();

    /**
     * 附加数据。
     */
    private Map<String, String> tags;

    /**
     * 发送者的信息。
     */
    private Map<String, String> sender;

    /**
     * 消息的接收者。
     */
    private AppInfoMatcherSpec target;

    /**
     * 消息内容。
     */
    private JsonObjectWrapper content;

    /**
     * 临时数据。
     */
    @Getter(AccessLevel.NONE)
    private transient Map<String, Object> temps;

    /**
     * 获取临时数据。
     *
     * @param createIfAbsent 如果不存在，是否创建
     * @return 临时数据
     */
    public Map<String, Object> getTemps(boolean createIfAbsent) {
        if (this.temps == null && createIfAbsent) {
            this.temps = new HashMap<>(BufferSizes.SMALL);
        }
        return this.temps;
    }
}
