package com.wingsweaver.kuja.common.cloud.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 Resilience4j 的 {@link CircuitBreakerManager} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class Resilience4jCircuitBreakerManager implements CircuitBreakerManager {
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public Resilience4jCircuitBreakerManager(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    public Map<String, CircuitBreakerStatus> all() {
        List<CircuitBreaker> circuitBreakerList = circuitBreakerRegistry.getAllCircuitBreakers().asJavaMutable();
        Map<String, CircuitBreakerStatus> map = new HashMap<>(circuitBreakerList.size(), 1);
        for (CircuitBreaker circuitBreaker : circuitBreakerList) {
            CircuitBreakerStatus status = this.resolveStatus(circuitBreaker);
            map.put(status.getName(), status);
        }
        return map;
    }

    /**
     * 计算 CircuitBreaker 状态。
     *
     * @param circuitBreaker CircuitBreaker 实例
     * @return CircuitBreaker 状态
     */
    private CircuitBreakerStatus resolveStatus(CircuitBreaker circuitBreaker) {
        CircuitBreakerStatus status = new CircuitBreakerStatus();
        status.setName(circuitBreaker.getName());
        switch (circuitBreaker.getState()) {
            case OPEN:
                status.setAllowRequest(true);
                status.setOpen(true);
                break;
            case CLOSED:
            case DISABLED:
            case HALF_OPEN:
            case METRICS_ONLY:
                status.setAllowRequest(true);
                status.setOpen(false);
                break;
            case FORCED_OPEN:
                status.setAllowRequest(false);
                status.setOpen(true);
                break;
            default:
                break;
        }
        return status;
    }

    @Override
    public void reset() {
        List<CircuitBreaker> circuitBreakerList = circuitBreakerRegistry.getAllCircuitBreakers().asJavaMutable();
        circuitBreakerList.forEach(CircuitBreaker::reset);
        circuitBreakerList.forEach(circuitBreaker -> circuitBreakerRegistry.remove(circuitBreaker.getName()));
    }
}
