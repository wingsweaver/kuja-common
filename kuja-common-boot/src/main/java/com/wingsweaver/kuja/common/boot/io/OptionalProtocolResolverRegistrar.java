package com.wingsweaver.kuja.common.boot.io;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ProtocolResolver;

import java.util.Collection;

/**
 * {@link OptionalProtocolResolver} 的注册工具类。
 *
 * @author wingsweaver
 */
public class OptionalProtocolResolverRegistrar implements ApplicationListener<ApplicationContextInitializedEvent> {
    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        if (applicationContext instanceof DefaultResourceLoader) {
            DefaultResourceLoader resourceLoader = (DefaultResourceLoader) applicationContext;
            Collection<ProtocolResolver> protocolResolvers = resourceLoader.getProtocolResolvers();
            if (protocolResolvers.stream()
                    .noneMatch(protocolResolver -> protocolResolver instanceof OptionalProtocolResolver)) {
                protocolResolvers.add(new OptionalProtocolResolver());
            }
        }
    }

}
