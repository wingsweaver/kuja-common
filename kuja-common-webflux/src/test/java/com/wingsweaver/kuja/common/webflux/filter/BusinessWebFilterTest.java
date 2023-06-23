package com.wingsweaver.kuja.common.webflux.filter;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextFactory;
import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.webflux.constants.KujaCommonWebFluxOrders;
import com.wingsweaver.kuja.common.webflux.support.ServerWebExchangeBusinessContextFactory;
import com.wingsweaver.kuja.common.webflux.util.ServerWebExchangeUtil;
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
import static org.junit.jupiter.api.Assertions.assertSame;

class BusinessWebFilterTest {

    @Test
    void filter() {
        ServerWebExchangeBusinessContextFactory businessContextFactory = new ServerWebExchangeBusinessContextFactory();
        businessContextFactory.setBusinessContextFactory(BusinessContextFactory.DEFAULT);
        businessContextFactory.setBusinessContextCustomizers(Collections.emptyList());
        businessContextFactory.afterPropertiesSet();

        BusinessWebFilter filter = new BusinessWebFilter();
        filter.setBusinessContextFactory(businessContextFactory);

        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080").build();
        ServerWebExchange exchange = new MockServerWebExchange.Builder(request).build();

        WebHandler webHandler = exchange1 -> {
            BusinessContext businessContext = ServerWebExchangeUtil.getBusinessContext(exchange1);
            assertNotNull(businessContext);
            assertSame(businessContext, BusinessContextHolder.getCurrent());
            return Mono.fromRunnable(() -> {
            });
        };
        WebFilterChain filterChain = new DefaultWebFilterChain(webHandler, Collections.emptyList());
        filter.filter(exchange, filterChain).subscribe();
    }

    @Test
    void afterPropertiesSet() {
        BusinessWebFilter filter = new BusinessWebFilter();
        assertNull(filter.getBusinessContextFactory());
        ServerWebExchangeBusinessContextFactory businessContextFactory = new ServerWebExchangeBusinessContextFactory();
        filter.setBusinessContextFactory(businessContextFactory);
        assertSame(businessContextFactory, filter.getBusinessContextFactory());
        filter.afterPropertiesSet();
        assertEquals(KujaCommonWebFluxOrders.BUSINESS_CONTEXT_WEB_FILTER, filter.getOrder());
    }
}