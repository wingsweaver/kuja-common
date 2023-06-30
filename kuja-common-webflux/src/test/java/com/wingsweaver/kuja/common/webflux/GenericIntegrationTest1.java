package com.wingsweaver.kuja.common.webflux;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextCustomizer;
import com.wingsweaver.kuja.common.boot.errordefinition.DefaultErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.exception.BusinessExceptionFactory;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringBuilder;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import com.wingsweaver.kuja.common.web.controller.AbstractController;
import com.wingsweaver.kuja.common.web.utils.LogContextConfigBuilder;
import com.wingsweaver.kuja.common.webflux.context.WebFluxContextAccessor;
import com.wingsweaver.kuja.common.webflux.support.HandlerResultWriter;
import com.wingsweaver.kuja.common.webflux.support.ServerWebExchangeBusinessContextFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@AutoConfigureWebTestClient(timeout = "PT10M")
@EnableKujaCommonWebFlux
@PropertySource(value = "classpath:application.properties")
@Import(GenericIntegrationTest1.TestConfiguration.class)
public class GenericIntegrationTest1 {
    @Autowired
    private WebTestClient webClient;

    @Test
    void test() {
        ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<Map<String, Object>>() {
        };
        WebTestClient.ResponseSpec responseSpec = webClient
                .post()
                .uri("/api/test/1?id=1234")
                .header(LogContextConfigBuilder.HEADER_LEVEL, "warn")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        responseSpec.expectStatus().is2xxSuccessful();

        responseSpec
                .expectBody(typeReference)
                .value(map -> {
                    System.out.println("map: ");
                    System.out.println(ToStringBuilder.toString(map));

                    assertNotNull(map.get("serverWebExchange"));
                    assertNotNull(map.get("serverHttpRequest"));
                    assertNotNull(map.get("serverHttpResponse"));
                    assertNotNull(map.get("originalRequest"));
                    assertNotNull(map.get("originalResponse"));
                    assertNotNull(map.get("webRequestWrapper"));
                    assertNotNull(map.get("webResponseWrapper"));
                    assertEquals("POST", map.get("method"));
                    assertEquals("/api/test/1", map.get("requestUri"));
                    assertEquals("/api/test/1?id=1234", map.get("requestFullPath"));
                    assertEquals("Web.Reactive.WebFlux", map.get("contextType"));
                });
    }

    @Test
    void test2() {
        // NOT_FOUND
        {
            WebTestClient.ResponseSpec responseSpec = webClient
                    .post()
                    .uri("/api/test2")
                    .exchange();
            responseSpec.expectStatus().is2xxSuccessful()
                    .expectBody(ReturnValue.class)
                    .value(returnValue -> {
                        System.out.println("returnValue: " + returnValue);
                        assertEquals("-1", returnValue.getCode());
                        assertTrue(returnValue.getMessage().contains(HttpStatus.NOT_FOUND.toString()));
                        assertEquals("failed", returnValue.getUserTip());
                    });
        }

        // 不支持的 Http Method
        {
            WebTestClient.ResponseSpec responseSpec = webClient
                    .post()
                    .uri("/api/test/2/1234")
                    .exchange();
            responseSpec.expectStatus().is2xxSuccessful()
                    .expectBody(ReturnValue.class)
                    .value(returnValue -> {
                        System.out.println("returnValue: " + returnValue);
                        assertEquals("-1", returnValue.getCode());
                        assertTrue(returnValue.getMessage().contains(HttpStatus.METHOD_NOT_ALLOWED.toString()));
                        assertEquals("failed", returnValue.getUserTip());
                    });
        }

        // 无效的输入参数
        {
            WebTestClient.ResponseSpec responseSpec = webClient
                    .get()
                    .uri("/api/test/2/abcd")
                    .exchange();
            responseSpec.expectStatus().is2xxSuccessful()
                    .expectBody(ReturnValue.class)
                    .value(returnValue -> {
                        System.out.println("returnValue: " + returnValue);
                        assertEquals("-1", returnValue.getCode());
                        assertTrue(returnValue.getMessage().contains(HttpStatus.BAD_REQUEST.toString()));
                        assertEquals("failed", returnValue.getUserTip());
                    });
        }

        // Controller 处理内部发生错误
        {
            WebTestClient.ResponseSpec responseSpec = webClient
                    .get()
                    .uri("/api/test/2/9876")
                    .exchange();
            responseSpec.expectStatus().is2xxSuccessful()
                    .expectBody(ReturnValue.class)
                    .value(returnValue -> {
                        System.out.println("returnValue: " + returnValue);
                        assertEquals("-1", returnValue.getCode());
                        assertEquals("custom exception 9876", returnValue.getMessage());
                        assertEquals("failed", returnValue.getUserTip());
                    });
        }
    }

    @Test
    void test3() {
        // 指定异常
        {
            WebTestClient.ResponseSpec responseSpec = webClient
                    .post()
                    .uri("/api/test/3")
                    .exchange();
            responseSpec
                    .expectStatus().isEqualTo(500)
                    .expectHeader().valueEquals("traceId", "12345678")
                    .expectBody(ReturnValue.class)
                    .value(returnValue -> {
                        System.out.println("returnValue: " + returnValue);
                        assertEquals("error.code.1", returnValue.getCode());
                        assertEquals("error.message.1", returnValue.getMessage());
                        assertEquals("error.userTip.1", returnValue.getUserTip());
                    });
        }
    }

    @Autowired
    private ServerWebExchangeBusinessContextFactory serverWebExchangeBusinessContextFactory;

    @Test
    void testServerWebExchangeBusinessContextFactory() {
        BusinessContextCustomizer preProcessor = businessContext -> {
            System.out.println("preProcessor");
            businessContext.setAttribute("preProcessor", "preProcessor");
        };
        BusinessContextCustomizer postProcessor = businessContext -> {
            System.out.println("postProcessor");
            businessContext.setAttribute("postProcessor", "postProcessor");
        };
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test/1?id=1234"));
        BusinessContext businessContext = serverWebExchangeBusinessContextFactory.createBusinessContext(exchange, preProcessor, postProcessor);
        assertEquals("preProcessor", businessContext.getAttribute("preProcessor"));
        assertEquals("custom-processor", businessContext.getAttribute("custom-processor"));
        assertEquals("postProcessor", businessContext.getAttribute("postProcessor"));
    }

    @Test
    void testHandlerResultWriter() {
        {
            WebTestClient.ResponseSpec responseSpec = webClient
                    .post()
                    .uri("/api/test/1")
                    .header("custom-filter", "foo")
                    .exchange();
            responseSpec.expectStatus().is2xxSuccessful()
                    .expectBody(ReturnValue.class)
                    .value(returnValue -> {
                        System.out.println("returnValue: " + returnValue);
                        assertEquals("0", returnValue.getCode());
                        assertEquals("operation done successfully", returnValue.getMessage());
                        assertEquals("success", returnValue.getUserTip());
                    });
        }

        {
            WebTestClient.ResponseSpec responseSpec = webClient
                    .post()
                    .uri("/api/test/1")
                    .header("custom-filter", "bar")
                    .exchange();
            responseSpec.expectStatus().is2xxSuccessful()
                    .expectBody(ReturnValue.class)
                    .value(returnValue -> {
                        System.out.println("returnValue: " + returnValue);
                        assertEquals("-1", returnValue.getCode());
                        assertTrue(returnValue.getMessage().startsWith("Could not resolve view with name "));
                        assertEquals("failed", returnValue.getUserTip());
                    });
        }
    }

    @Configuration
    static class TestConfiguration {
        @Bean
        public TestController testController() {
            return new TestController();
        }

        @Bean
        public CustomBusinessContextCustomizer customBusinessContextCustomizer() {
            return new CustomBusinessContextCustomizer();
        }

        @Bean
        public CustomWebFilter customWebFilter(HandlerResultWriter handlerResultWriter) {
            return new CustomWebFilter();
        }
    }

    static class CustomWebFilter implements WebFilter {
        @Autowired
        private HandlerResultWriter handlerResultWriter;

        @Autowired
        private ReturnValueFactory returnValueFactory;

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            String value = exchange.getRequest().getHeaders().getFirst("custom-filter");
            if (StringUtil.isEmpty(value)) {
                return chain.filter(exchange);
            } else if ("foo".equals(value)) {
                return handlerResultWriter.writeReturnValue(exchange, this.returnValueFactory.success());
            } else {
                return handlerResultWriter.writeResult(exchange, this.resolveHandlerResult(value));
            }
        }

        private HandlerResult resolveHandlerResult(String value) {
            try {
                Method method = this.getClass().getDeclaredMethod(value);
                return new HandlerResult(this, returnValueFactory.success(), new MethodParameter(method, -1));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        private ReturnValue bar() {
            return null;
        }
    }

    static class CustomBusinessContextCustomizer implements BusinessContextCustomizer {

        @Override
        public void customize(BusinessContext businessContext) {
            businessContext.setAttribute("custom-processor", "custom-processor");
        }
    }

    @RestController
    @RequestMapping("/api/test")
    static class TestController extends AbstractController {
        @Autowired
        private BusinessExceptionFactory businessExceptionFactory;

        @RequestMapping("/1")
        public Map<String, Object> test1(BusinessContext businessContext) {
            Map<String, Object> map = new HashMap<>(BufferSizes.TINY);
            WebFluxContextAccessor accessor = new WebFluxContextAccessor(businessContext);

            // 存入 WebFluxContextAccessor 级别的内容
            map.put("serverWebExchange", accessor.getServerWebExchange().toString());
            map.put("serverHttpRequest", accessor.getServerHttpRequest().toString());
            map.put("serverHttpResponse", accessor.getServerHttpResponse().toString());

            // 存入 WebContextAccessor 级别的内容
            map.put("originalRequest", accessor.getOriginalRequest().toString());
            map.put("originalResponse", accessor.getOriginalResponse().toString());
            map.put("webRequestWrapper", accessor.getRequestWrapper().toString());
            map.put("webResponseWrapper", accessor.getResponseWrapper().toString());
            map.put("method", accessor.getServerHttpRequest().getMethodValue());
            map.put("requestUri", accessor.getRequestUri());
            map.put("requestFullPath", accessor.getRequestFullPath());

            // 存入 BusinessContextAccessor 级别的内容
            map.put("contextType", businessContext.getContextType().toString());

            // 返回
            return map;
        }

        @GetMapping("/2/{id}")
        public void test2(@PathVariable("id") int id) {
            throw new RuntimeException("custom exception " + id);
        }

        private ErrorDefinition errorDefinition = DefaultErrorDefinition.builder()
                .code("error.code.1")
                .message("error.message.1")
                .userTip("error.userTip.1")
                .temps(MapUtil.from("response-data.status", "500",
                        "response-data.headers.traceId", 12345678))
                .build();

        @RequestMapping("/3")
        public void test3() {
            throw this.businessExceptionFactory.create(errorDefinition);
        }
    }
}
