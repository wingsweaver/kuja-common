package com.wingsweaver.kuja.common.webflux.config;

import com.wingsweaver.kuja.common.web.common.WebServerProperties;
import com.wingsweaver.kuja.common.webflux.support.ServerWebExchangeBusinessContextFactory;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KujaWebFluxConfigurerTest {

    @Test
    void afterPropertiesSet() throws Exception {
        KujaWebFluxConfigurer configurer = new KujaWebFluxConfigurer();

        assertNull(configurer.getBusinessContextFactory());
        ServerWebExchangeBusinessContextFactory businessContextFactory = new ServerWebExchangeBusinessContextFactory();
        configurer.setBusinessContextFactory(businessContextFactory);
        assertSame(businessContextFactory, configurer.getBusinessContextFactory());

        assertNull(configurer.getProperties());
        WebServerProperties properties = new WebServerProperties();
        configurer.setProperties(properties);
        assertSame(properties, configurer.getProperties());

        configurer.afterPropertiesSet();
    }

    @Test
    void configurePathMatching() {
        WebServerProperties properties = new WebServerProperties();
        properties.setCaseSensitive(true);
        properties.setTrailingSlash(true);

        KujaWebFluxConfigurer configurer = new KujaWebFluxConfigurer();
        configurer.setProperties(properties);

        CustomPathMatchConfigurer pathMatchConfigurer = new CustomPathMatchConfigurer();
        configurer.configurePathMatching(pathMatchConfigurer);

        assertTrue(pathMatchConfigurer.isUseCaseSensitiveMatch());
        assertTrue(pathMatchConfigurer.isUseTrailingSlashMatch());
    }

    static class CustomPathMatchConfigurer extends PathMatchConfigurer {
        @Override
        public Boolean isUseCaseSensitiveMatch() {
            return super.isUseCaseSensitiveMatch();
        }

        @Override
        public Boolean isUseTrailingSlashMatch() {
            return super.isUseTrailingSlashMatch();
        }
    }

    @Test
    void configureArgumentResolvers() {
        KujaWebFluxConfigurer configurer = new KujaWebFluxConfigurer();
        ArgumentResolverConfigurer argumentResolverConfigurer = new ArgumentResolverConfigurer();
        configurer.configureArgumentResolvers(argumentResolverConfigurer);
    }
}