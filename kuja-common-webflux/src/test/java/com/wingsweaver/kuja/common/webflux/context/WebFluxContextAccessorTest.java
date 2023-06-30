package com.wingsweaver.kuja.common.webflux.context;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.MapBusinessContext;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class WebFluxContextAccessorTest {
    @Test
    void test() {
        BusinessContext businessContext = new MapBusinessContext();
        WebFluxContextAccessor accessor = new WebFluxContextAccessor(businessContext);

        // serverWebExchange
        {
            assertNull(accessor.getServerWebExchange());
            DummyServerWebExchange exchange = new DummyServerWebExchange();
            accessor.setServerWebExchange(exchange);
            assertSame(exchange, accessor.getServerWebExchange());
        }

        // serverHttpRequest
        {
            assertNull(accessor.getServerHttpRequest());
            DummyServerHttpRequest request = new DummyServerHttpRequest();
            accessor.setServerHttpRequest(request);
            assertSame(request, accessor.getServerHttpRequest());
        }

        // serverHttpResponse
        {
            assertNull(accessor.getServerHttpResponse());
            DummyServerHttpResponse response = new DummyServerHttpResponse();
            accessor.setServerHttpResponse(response);
            assertSame(response, accessor.getServerHttpResponse());
        }
    }

    static class DummyServerWebExchange implements ServerWebExchange {
        @Override
        public ServerHttpRequest getRequest() {
            return null;
        }

        @Override
        public ServerHttpResponse getResponse() {
            return null;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return null;
        }

        @Override
        public Mono<WebSession> getSession() {
            return null;
        }

        @Override
        public <T extends Principal> Mono<T> getPrincipal() {
            return null;
        }

        @Override
        public Mono<MultiValueMap<String, String>> getFormData() {
            return null;
        }

        @Override
        public Mono<MultiValueMap<String, Part>> getMultipartData() {
            return null;
        }

        @Override
        public LocaleContext getLocaleContext() {
            return null;
        }

        @Override
        public ApplicationContext getApplicationContext() {
            return null;
        }

        @Override
        public boolean isNotModified() {
            return false;
        }

        @Override
        public boolean checkNotModified(Instant lastModified) {
            return false;
        }

        @Override
        public boolean checkNotModified(String etag) {
            return false;
        }

        @Override
        public boolean checkNotModified(String etag, Instant lastModified) {
            return false;
        }

        @Override
        public String transformUrl(String url) {
            return null;
        }

        @Override
        public void addUrlTransformer(Function<String, String> transformer) {

        }

        @Override
        public String getLogPrefix() {
            return null;
        }
    }

    static class DummyServerHttpRequest implements ServerHttpRequest {

        @Override
        public String getId() {
            return null;
        }

        @Override
        public RequestPath getPath() {
            return null;
        }

        @Override
        public MultiValueMap<String, String> getQueryParams() {
            return null;
        }

        @Override
        public MultiValueMap<String, HttpCookie> getCookies() {
            return null;
        }

        @Override
        public String getMethodValue() {
            return null;
        }

        @Override
        public URI getURI() {
            return null;
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return null;
        }

        @Override
        public HttpHeaders getHeaders() {
            return null;
        }
    }

    static class DummyServerHttpResponse implements ServerHttpResponse {

        @Override
        public boolean setStatusCode(HttpStatus status) {
            return false;
        }

        @Override
        public HttpStatus getStatusCode() {
            return null;
        }

        @Override
        public MultiValueMap<String, ResponseCookie> getCookies() {
            return null;
        }

        @Override
        public void addCookie(ResponseCookie cookie) {

        }

        @Override
        public DataBufferFactory bufferFactory() {
            return null;
        }

        @Override
        public void beforeCommit(Supplier<? extends Mono<Void>> action) {

        }

        @Override
        public boolean isCommitted() {
            return false;
        }

        @Override
        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            return null;
        }

        @Override
        public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
            return null;
        }

        @Override
        public Mono<Void> setComplete() {
            return null;
        }

        @Override
        public HttpHeaders getHeaders() {
            return null;
        }
    }
}