package com.wingsweaver.kuja.common.webmvc.jakarta.config;

import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.web.common.WebServerProperties;
import com.wingsweaver.kuja.common.webmvc.common.support.BusinessContextHandlerMethodArgumentResolver;
import com.wingsweaver.kuja.common.webmvc.jakarta.interceptor.BusinessContextHandlerInterceptor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * kuja-common-webmvc-jakarta 的 {@link WebMvcConfigurer} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class KujaWebMvcConfigurer extends AbstractComponent implements WebMvcConfigurer {
    /**
     * WebServerProperties 实例。
     */
    private WebServerProperties properties;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        Boolean caseSensitive = this.properties.getCaseSensitive();
        if (caseSensitive != null) {
            AntPathMatcher pathMatcher = new AntPathMatcher();
            pathMatcher.setCaseSensitive(caseSensitive);
            configurer.setPathMatcher(pathMatcher);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BusinessContextHandlerInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new BusinessContextHandlerMethodArgumentResolver());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 properties
        this.initProperties();
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
