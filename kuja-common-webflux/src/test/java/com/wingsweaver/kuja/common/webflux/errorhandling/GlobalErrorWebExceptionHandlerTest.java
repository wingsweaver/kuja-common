package com.wingsweaver.kuja.common.webflux.errorhandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandler;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.web.errorhandling.ResponseData;
import com.wingsweaver.kuja.common.web.errorhandling.WebErrorHandlerContextAccessor;
import com.wingsweaver.kuja.common.webflux.EnableKujaCommonWebFlux;
import com.wingsweaver.kuja.common.webflux.constants.KujaCommonWebFluxOrders;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@AutoConfigureWebTestClient(timeout = "PT10M")
@EnableKujaCommonWebFlux
@PropertySource(value = "classpath:application.properties")
@Import(GlobalErrorWebExceptionHandlerTest.TestConfiguration.class)
class GlobalErrorWebExceptionHandlerTest {
    @Autowired
    private GlobalErrorWebExceptionHandler exceptionHandler;

    @Test
    void afterPropertiesSet() {
        assertNotNull(exceptionHandler);
        assertNotNull(exceptionHandler.getErrorHandlingDelegate());
        assertNotNull(exceptionHandler.getErrorAttributes());
        assertEquals(KujaCommonWebFluxOrders.GLOBAL_ERROR_WEB_EXCEPTION_HANDLER, exceptionHandler.getOrder());
        assertNotNull(exceptionHandler.getReturnValueFactory());
        assertNotNull(exceptionHandler.getBusinessContextFactory());
    }

    @Test
    void handle() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        Exception error = new Exception("some-error");
        this.exceptionHandler.handle(exchange, error).block();

        MockServerHttpResponse response = exchange.getResponse();
        String content = response.getBodyAsString().block();
        ReturnValue returnValue = objectMapper.readValue(content, ReturnValue.class);
        assertEquals("-1", returnValue.getCode());
        assertEquals("some-error", returnValue.getMessage());
        assertEquals("failed", returnValue.getUserTip());
    }

    @Autowired
    private CustomErrorHandler customErrorHandler;

    @Test
    void handle2() throws Exception {
        ResponseData responseData = new ResponseData();
        responseData.setStatus(HttpStatus.BAD_GATEWAY);
        responseData.getHeaders(true).set("X-CUSTOM-HEADER", "123456789");
        this.customErrorHandler.setResponseData(responseData);

        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        Exception error = new Exception("some-error");
        this.exceptionHandler.handle(exchange, error).block();
        this.customErrorHandler.setResponseData(null);

        MockServerHttpResponse response = exchange.getResponse();
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals("123456789", response.getHeaders().getFirst("X-CUSTOM-HEADER"));

        String content = response.getBodyAsString().block();
        ObjectMapper objectMapper = new ObjectMapper();
        ReturnValue returnValue = objectMapper.readValue(content, ReturnValue.class);
        assertEquals("-1", returnValue.getCode());
        assertEquals("some-error", returnValue.getMessage());
        assertEquals("failed", returnValue.getUserTip());

    }

    static class EmptyContext implements ServerResponse.Context {
        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return null;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return null;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class TestConfiguration extends AbstractConfiguration {
        @Bean
        public CustomErrorHandler customErrorHandler() {
            return new CustomErrorHandler();
        }
    }

    @Getter
    @Setter
    static class CustomErrorHandler implements ErrorHandler {
        private ResponseData responseData;

        @Override
        public boolean handle(ErrorHandlerContext context) {
            if (responseData != null) {
                WebErrorHandlerContextAccessor accessor = new WebErrorHandlerContextAccessor(context);
                accessor.setResponseData(responseData);
            }

            return false;
        }
    }
}