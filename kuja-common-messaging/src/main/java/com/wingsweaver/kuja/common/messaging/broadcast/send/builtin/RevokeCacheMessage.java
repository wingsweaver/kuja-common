package com.wingsweaver.kuja.common.messaging.broadcast.send.builtin;

import com.wingsweaver.kuja.common.messaging.broadcast.common.BuiltInMessageTypes;
import com.wingsweaver.kuja.common.messaging.broadcast.send.BroadcastMessageT;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 清空缓存的消息。
 *
 * @author wingsweaver
 */
public class RevokeCacheMessage extends BroadcastMessageT<List<RevokeCacheMessage.Config>> {
    public RevokeCacheMessage() {
        this.setMessageType(BuiltInMessageTypes.REVOKE_CACHE);
    }

    public List<Config> getContent(boolean createIfAbsent) {
        List<Config> content = this.getContent();
        if (content == null && createIfAbsent) {
            content = new ArrayList<>(BufferSizes.TINY);
            this.setContent(content);
        }
        return content;
    }

    /**
     * 清空缓存的设置。
     */
    @Getter
    @Setter
    public static class Config extends AbstractPojo {
        /**
         * 缓存的名称。
         */
        private String cacheName;

        /**
         * 要清空的 key 的列表。<br>
         * 不指定的话，清空对应缓存中的所有数据。
         */
        private List<String> keys;
    }
}
