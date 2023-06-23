package com.wingsweaver.kuja.common.messaging.core.send.common;

/**
 * 用于判定是否支持指定消息的处理逻辑的接口定义。
 *
 * @author wingsweaver
 */
public interface MessagePredicate {
    /**
     * 判定是否支持指定消息的处理逻辑。
     *
     * @param message 消息内容
     * @return 是否支持
     */
    boolean supportMessage(Object message);
}
