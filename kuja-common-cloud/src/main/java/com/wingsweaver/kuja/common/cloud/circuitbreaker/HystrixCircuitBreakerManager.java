package com.wingsweaver.kuja.common.cloud.circuitbreaker;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandMetrics;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于 Hystrix 的 {@link CircuitBreakerManager} 实现类。
 *
 * @author wingsweaver
 */
public class HystrixCircuitBreakerManager implements CircuitBreakerManager {
    @Override
    public Map<String, CircuitBreakerStatus> all() {
        Collection<HystrixCommandMetrics> metricsCollection = HystrixCommandMetrics.getInstances();
        Map<String, CircuitBreakerStatus> map = new HashMap<>(metricsCollection.size(), 1);

        for (HystrixCommandMetrics metrics : metricsCollection) {
            HystrixCircuitBreaker circuitBreaker = HystrixCircuitBreaker.Factory
                    .getInstance(metrics.getCommandKey());
            if (circuitBreaker != null) {
                CircuitBreakerStatus status = this.resolveStatus(metrics, circuitBreaker);
                map.put(status.getName(), status);
            }
        }

        // 返回
        return map;
    }

    /**
     * 计算 CircuitBreaker 状态。
     *
     * @param metrics        HystrixCommandMetrics 实例
     * @param circuitBreaker HystrixCircuitBreaker 实例
     * @return CircuitBreaker 状态
     */
    private CircuitBreakerStatus resolveStatus(HystrixCommandMetrics metrics, HystrixCircuitBreaker circuitBreaker) {
        CircuitBreakerStatus status = new CircuitBreakerStatus();

        // Key 相关信息
        HystrixCommandKey commandKey = metrics.getCommandKey();
        if (commandKey != null) {
            status.setName(commandKey.name());
        }
        HystrixThreadPoolKey threadPoolKey = metrics.getThreadPoolKey();
        if (threadPoolKey != null) {
            status.setName(threadPoolKey.name());
        }

        // CircuitBreaker 相关信息
        status.setAllowRequest(circuitBreaker.allowRequest());
        status.setOpen(circuitBreaker.isOpen());

        // 返回
        return status;
    }

    @Override
    public void reset() {
        Hystrix.reset();
        try {
            Method method = ReflectionUtils.findMethod(HystrixCircuitBreaker.Factory.class, "reset");
            if (method != null) {
                method.invoke(HystrixCircuitBreaker.Factory.class);
            }
        } catch (Exception ignored) {
            // 忽略此错误
        }
    }
}
