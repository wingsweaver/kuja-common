package com.wingsweaver.kuja.common.webflux.config;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.web.common.WebServerProperties;
import com.wingsweaver.kuja.common.webflux.support.BusinessContextHandlerMethodArgumentResolver;
import com.wingsweaver.kuja.common.webflux.support.ServerWebExchangeBusinessContextFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

/**
 * kuja-common-webflux 的 {@link WebFluxConfigurer} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class KujaWebFluxConfigurer implements WebFluxConfigurer, InitializingBean {
    private WebServerProperties properties;

    private ServerWebExchangeBusinessContextFactory businessContextFactory;

    @Override
    public void configurePathMatching(PathMatchConfigurer configurer) {
        configurer.setUseCaseSensitiveMatch(this.properties.getCaseSensitive());
        configurer.setUseTrailingSlashMatch(this.properties.getTrailingSlash());
    }

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        BusinessContextHandlerMethodArgumentResolver argumentResolver = new BusinessContextHandlerMethodArgumentResolver();
        argumentResolver.setBusinessContextFactory(this.businessContextFactory);
        configurer.addCustomResolver(argumentResolver);
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("properties", this.getProperties());
        AssertState.Named.notNull("businessContextFactory", this.getBusinessContextFactory());
    }
}
