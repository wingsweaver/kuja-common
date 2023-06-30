package com.wingsweaver.kuja.common.webflux.support;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextCustomizer;
import com.wingsweaver.kuja.common.boot.context.DefaultBusinessContextFactory;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ServerWebExchangeBusinessContextFactoryTest {
    @Test
    void test() throws Exception {
        BusinessContextCustomizer customizer = businessContext -> businessContext.setAttribute("custom-attribute", "1234567890");
        List<BusinessContextCustomizer> customizers = Collections.singletonList(customizer);

        ServerWebExchangeBusinessContextFactory businessContextFactory = new ServerWebExchangeBusinessContextFactory();

        assertNull(businessContextFactory.getBusinessContextFactory());
        DefaultBusinessContextFactory businessContextFactory2 = new DefaultBusinessContextFactory();
        businessContextFactory2.afterPropertiesSet();
        businessContextFactory.setBusinessContextFactory(businessContextFactory2);
        assertSame(businessContextFactory2, businessContextFactory.getBusinessContextFactory());

        assertNull(businessContextFactory.getBusinessContextCustomizers());
        businessContextFactory.setBusinessContextCustomizers(customizers);
        assertSame(customizers, businessContextFactory.getBusinessContextCustomizers());

        businessContextFactory.afterPropertiesSet();

        testCreateBusinessContext(businessContextFactory);
        testCreateBusinessContext2(businessContextFactory);
    }

    private void testCreateBusinessContext2(ServerWebExchangeBusinessContextFactory businessContextFactory) {
        BusinessContextCustomizer preProcessor = businessContext ->
                businessContext.setAttribute("pre-processor", "12345");
        BusinessContextCustomizer postProcessor = businessContext ->
                businessContext.setAttribute("post-processor", "98765");

        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        BusinessContext businessContext = businessContextFactory.createBusinessContext(exchange, preProcessor, postProcessor);
        assertNotNull(businessContext);
        assertEquals("12345", businessContext.getAttribute("pre-processor"));
        assertEquals("98765", businessContext.getAttribute("post-processor"));
        assertEquals("1234567890", businessContext.getAttribute("custom-attribute"));
    }

    private void testCreateBusinessContext(ServerWebExchangeBusinessContextFactory businessContextFactory) {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        BusinessContext businessContext = businessContextFactory.createBusinessContext(exchange);
        assertNotNull(businessContext);
        assertEquals("1234567890", businessContext.getAttribute("custom-attribute"));
    }
}