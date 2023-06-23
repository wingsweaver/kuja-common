package com.wingsweaver.kuja.common.cloud.circuitbreaker;

import lombok.Getter;
import lombok.Setter;

/**
 * CircuitBreaker 的状态。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class CircuitBreakerStatus {
    /**
     * 名称。
     */
    private String name;

    /**
     * 是否可以接受请求。
     */
    private boolean allowRequest;

    /**
     * 断路器是否打开。
     */
    private boolean open;
}
