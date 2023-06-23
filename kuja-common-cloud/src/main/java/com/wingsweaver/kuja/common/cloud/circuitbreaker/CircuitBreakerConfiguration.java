package com.wingsweaver.kuja.common.cloud.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 断路器相关设置。
 *
 * @author wingsweaver
 */
public class CircuitBreakerConfiguration {
    @Configuration
    @ConditionalOnClass(name = "com.netflix.hystrix.HystrixCircuitBreaker")
    public static class HystrixConfiguration {
        @Bean
        @ConditionalOnMissingBean(CircuitBreakerManager.class)
        public CircuitBreakerManager circuitBreakerManager() {
            return new HystrixCircuitBreakerManager();
        }
    }

    @Configuration
    @ConditionalOnClass(name = "io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry")
    public static class Resilience4jConfiguration {
        @Bean
        @ConditionalOnMissingBean(CircuitBreakerManager.class)
        @ConditionalOnBean(CircuitBreakerRegistry.class)
        public CircuitBreakerManager circuitBreakerManager(CircuitBreakerRegistry circuitBreakerRegistry) {
            return new Resilience4jCircuitBreakerManager(circuitBreakerRegistry);
        }
    }
}
