package com.wingsweaver.kuja.common.messaging.common;

import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import com.wingsweaver.kuja.common.utils.model.id.IdSetter;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AbstractMessage extends AbstractPojo implements IdSetter<String> {
    /**
     * 消息 ID。
     */
    private String id;

    /**
     * 创建时间 (UTC)。
     */
    private long creationTime;
}
