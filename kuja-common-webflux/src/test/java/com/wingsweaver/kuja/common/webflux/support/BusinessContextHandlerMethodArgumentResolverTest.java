package com.wingsweaver.kuja.common.webflux.support;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextCustomizer;
import com.wingsweaver.kuja.common.boot.context.DefaultBusinessContextFactory;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.webflux.util.ServerWebExchangeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessContextHandlerMethodArgumentResolverTest {

    @Test
    void supportsParameter() throws Exception {
        BusinessContextHandlerMethodArgumentResolver resolver = new BusinessContextHandlerMethodArgumentResolver();
        assertTrue(resolver.supportsParameter(buildMethodParameter("foo")));
        assertFalse(resolver.supportsParameter(buildMethodParameter("bar")));
    }

    MethodParameter buildMethodParameter(String methodName) throws NoSuchMethodException {
        Method method = this.getClass().getDeclaredMethod(methodName);
        return new MethodParameter(method, -1);
    }

    BusinessContext foo() {
        return null;
    }

    ReturnValue bar() {
        return null;
    }

    @Test
    void resolveArgument() throws NoSuchMethodException {
        BusinessContextCustomizer customizer = businessContext -> businessContext.setAttribute("custom-attribute", "1234567890");

        ServerWebExchangeBusinessContextFactory businessContextFactory = new ServerWebExchangeBusinessContextFactory();
        businessContextFactory.setBusinessContextFactory(new DefaultBusinessContextFactory());
        businessContextFactory.setBusinessContextCustomizers(Collections.singletonList(customizer));

        BusinessContextHandlerMethodArgumentResolver resolver = new BusinessContextHandlerMethodArgumentResolver();
        resolver.setBusinessContextFactory(businessContextFactory);
        MethodParameter methodParameter = this.buildMethodParameter("foo");

        testResolveArgument(resolver, methodParameter);
        testResolveArgument2(resolver, methodParameter);
        testResolveArgument3(resolver, methodParameter);
    }

    private void testResolveArgument3(BusinessContextHandlerMethodArgumentResolver resolver, MethodParameter methodParameter) {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080").build();
        MockServerWebExchange exchange = new MockServerWebExchange.Builder(request).build();
        resolver.resolveArgument(methodParameter, null, exchange)
                .subscribe(object -> {
                    assertNotNull(object);
                    BusinessContext businessContext = (BusinessContext) object;
                    assertEquals("1234567890", businessContext.getAttribute("custom-attribute"));
                });
    }

    private void testResolveArgument2(BusinessContextHandlerMethodArgumentResolver resolver, MethodParameter methodParameter) {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080").build();
        MockServerWebExchange exchange = new MockServerWebExchange.Builder(request).build();
        BusinessContext businessContext = new MapBusinessContext();

        Mono.deferContextual(contextView -> Mono.just(contextView.get(BusinessContext.class))
                        .then(resolver.resolveArgument(methodParameter, null, exchange)))
                .subscribe(object -> {
                    assertSame(businessContext, object);
                    assertEquals("1234567890", businessContext.getAttribute("custom-attribute"));
                });
    }

    private void testResolveArgument(BusinessContextHandlerMethodArgumentResolver resolver, MethodParameter methodParameter) {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080").build();
        MockServerWebExchange exchange = new MockServerWebExchange.Builder(request).build();
        BusinessContext businessContext = new MapBusinessContext();
        ServerWebExchangeUtil.setBusinessContext(exchange, businessContext);

        resolver.resolveArgument(methodParameter, null, exchange)
                .subscribe(object -> {
                    assertSame(businessContext, object);
                    assertEquals("1234567890", businessContext.getAttribute("custom-attribute"));
                });
    }

    @Test
    void afterPropertiesSet() throws Exception {
        BusinessContextHandlerMethodArgumentResolver resolver = new BusinessContextHandlerMethodArgumentResolver();
        assertNull(resolver.getBusinessContextFactory());
        ServerWebExchangeBusinessContextFactory businessContextFactory = new ServerWebExchangeBusinessContextFactory();
        resolver.setBusinessContextFactory(businessContextFactory);
        assertSame(businessContextFactory, resolver.getBusinessContextFactory());
        resolver.afterPropertiesSet();
    }
}