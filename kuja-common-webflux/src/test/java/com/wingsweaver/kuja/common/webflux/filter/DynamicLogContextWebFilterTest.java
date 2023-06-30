package com.wingsweaver.kuja.common.webflux.filter;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogContext;
import com.wingsweaver.kuja.common.webflux.constants.KujaCommonWebFluxOrders;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DynamicLogContextWebFilterTest {

    @Test
    void filter() {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080").build();
        ServerWebExchange exchange = new MockServerWebExchange.Builder(request).build();

        WebHandler webHandler = exchange1 -> {
            assertNull(LogContext.getConfig());
            return Mono.fromRunnable(() -> {
            });
        };
        WebFilterChain filterChain = new DefaultWebFilterChain(webHandler, Collections.emptyList());

        DynamicLogContextWebFilter filter = new DynamicLogContextWebFilter();
        filter.filter(exchange, filterChain).subscribe();
    }

    @Test
    void filter2() {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080")
                .header("X-LOGGING-LOGGER", "custom-logger")
                .header("X-LOGGING-MARKER", "test-marker")
                .header("X-LOGGING-LEVEL", "DEBUG")
                .build();
        ServerWebExchange exchange = new MockServerWebExchange.Builder(request).build();

        WebHandler webHandler = exchange1 -> {
            LogContext.Config config = LogContext.getConfig();
            assertNotNull(config);
            assertEquals("custom-logger", config.getLogger().getName());
            assertEquals("test-marker", config.getMarker().getName());
            assertEquals("DEBUG", config.getLevel().name());
            return Mono.fromRunnable(() -> {
            });
        };
        WebFilterChain filterChain = new DefaultWebFilterChain(webHandler, Collections.emptyList());

        DynamicLogContextWebFilter filter = new DynamicLogContextWebFilter();
        filter.filter(exchange, filterChain).subscribe();
    }

    @Test
    void afterPropertiesSet() throws Exception {
        DynamicLogContextWebFilter filter = new DynamicLogContextWebFilter();
        filter.afterPropertiesSet();
        assertEquals(KujaCommonWebFluxOrders.LOG_CONTEXT_WEB_FILTER, filter.getOrder());
    }
}