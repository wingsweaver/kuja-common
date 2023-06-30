package com.wingsweaver.kuja.common.webmvc.jee.config;

import com.wingsweaver.kuja.common.web.common.WebServerProperties;
import com.wingsweaver.kuja.common.webmvc.common.support.BusinessContextHandlerMethodArgumentResolver;
import com.wingsweaver.kuja.common.webmvc.jee.interceptor.BusinessContextHandlerInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KujaWebMvcConfigurerTest {

    @Test
    void configurePathMatch() {
        WebServerProperties properties = new WebServerProperties();
        properties.setCaseSensitive(true);
        properties.setTrailingSlash(true);

        KujaWebMvcConfigurer configurer = new KujaWebMvcConfigurer();
        configurer.setProperties(properties);

        PathMatchConfigurer pathMatchConfigurer = new PathMatchConfigurer();
        configurer.configurePathMatch(pathMatchConfigurer);
    }

    @Test
    void addInterceptors() {
        KujaWebMvcConfigurer configurer = new KujaWebMvcConfigurer();
        CustomInterceptorRegistry interceptorRegistry = new CustomInterceptorRegistry();
        configurer.addInterceptors(interceptorRegistry);
        List<Object> interceptors = interceptorRegistry.getInterceptors();
        assertEquals(1, interceptors.size());
        assertTrue(interceptors.get(0) instanceof BusinessContextHandlerInterceptor);
    }

    @Test
    void addArgumentResolvers() {
        KujaWebMvcConfigurer configurer = new KujaWebMvcConfigurer();
        List<HandlerMethodArgumentResolver> resolvers = new LinkedList<>();
        configurer.addArgumentResolvers(resolvers);
        assertEquals(1, resolvers.size());
        assertTrue(resolvers.get(0) instanceof BusinessContextHandlerMethodArgumentResolver);
    }

    @Test
    void afterPropertiesSet() throws Exception {
        KujaWebMvcConfigurer configurer = new KujaWebMvcConfigurer();
        assertNull(configurer.getProperties());
        WebServerProperties properties = new WebServerProperties();
        configurer.setProperties(properties);
        assertSame(properties, configurer.getProperties());
        configurer.afterPropertiesSet();
    }

    static class CustomInterceptorRegistry extends InterceptorRegistry {
        @Override
        public List<Object> getInterceptors() {
            return super.getInterceptors();
        }
    }
}