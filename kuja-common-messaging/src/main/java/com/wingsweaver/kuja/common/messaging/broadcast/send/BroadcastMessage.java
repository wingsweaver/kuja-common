package com.wingsweaver.kuja.common.messaging.broadcast.send;

import com.wingsweaver.kuja.common.boot.appinfo.matcher.AppInfoMatcherSpec;
import com.wingsweaver.kuja.common.messaging.broadcast.common.AbstractBroadcastData;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.tags.Tags;
import lombok.Getter;
import lombok.Setter;

/**
 * 发送的广播消息。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BroadcastMessage extends AbstractBroadcastData {
    /**
     * 附加数据。
     */
    private Tags tags;

    /**
     * 消息的接收者。
     */
    private AppInfoMatcherSpec target;

    /**
     * 获取附加数据。
     *
     * @param createIfAbsent 如果不存在，是否创建
     * @return 附加数据
     */
    public Tags getTags(boolean createIfAbsent) {
        if (this.tags == null && createIfAbsent) {
            this.tags = Tags.of(BufferSizes.SMALL);
        }
        return this.tags;
    }
}
