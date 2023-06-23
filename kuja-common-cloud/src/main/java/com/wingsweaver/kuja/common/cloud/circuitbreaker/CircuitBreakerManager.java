package com.wingsweaver.kuja.common.cloud.circuitbreaker;

import java.util.Map;

/**
 * CircuitBreaker 管理工具。
 *
 * @author wingsweaver
 */
public interface CircuitBreakerManager {
    /**
     * 获取当前所有 CircuitBreaker 状态的快照。
     *
     * @return 当前所有 CircuitBreaker 状态的快照。
     */
    Map<String, CircuitBreakerStatus> all();

    /**
     * 清除所有的 CircuitBreaker 。
     */
    void reset();
}
