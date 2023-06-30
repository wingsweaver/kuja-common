package com.wingsweaver.kuja.common.webflux.config;

import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
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
public class KujaWebFluxConfigurer extends AbstractComponent implements WebFluxConfigurer, InitializingBean {
    /**
     * WebServerProperties 实例。
     */
    private WebServerProperties properties;

    /**
     * ServerWebExchangeBusinessContextFactory 实例。
     */
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
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 properties
        this.initProperties();

        // 初始化 businessContextFactory
        this.initBusinessContextFactory();
    }

    /**
     * 初始化 businessContextFactory。
     */
    protected void initBusinessContextFactory() {
        if (this.businessContextFactory == null) {
            this.businessContextFactory = this.getBean(ServerWebExchangeBusinessContextFactory.class);
        }
    }

    /**
     * 初始化 properties。
     */
    protected void initProperties() {
        if (this.properties == null) {
            this.properties = this.getBean(WebServerProperties.class);
        }
    }
}
