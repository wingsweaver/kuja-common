package com.wingsweaver.kuja.common.messaging.broadcast.common;

/**
 * 内置消息的类型定义。
 *
 * @author wingsweaver
 */
public interface BuiltInMessageTypes {
    /**
     * {@link com.wingsweaver.kuja.common.messaging.broadcast.send.builtin.ShutDownApplicationMessage}。
     */
    String SHUTDOWN_APPLICATION = "shutdown-application";

    /**
     * {@link com.wingsweaver.kuja.common.messaging.broadcast.send.builtin.RefreshApplicationContextMessage}。
     */
    String REFRESH_APPLICATION_CONTEXT = "refresh-application-context";

    /**
     * {@link com.wingsweaver.kuja.common.messaging.broadcast.send.builtin.RevokeCacheMessage}。
     */
    String REVOKE_CACHE = "revoke-cache";

    /**
     * {@link com.wingsweaver.kuja.common.messaging.broadcast.send.builtin.ConfigLogLevelMessage}。
     */
    String CONFIG_LOG_LEVEL = "config-log-level";
}
