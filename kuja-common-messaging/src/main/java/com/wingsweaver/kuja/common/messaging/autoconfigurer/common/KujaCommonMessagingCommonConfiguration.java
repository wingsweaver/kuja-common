package com.wingsweaver.kuja.common.messaging.autoconfigurer.common;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.send.SenderInfoResolverProperties;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.messaging.common.DefaultSenderInfoResolver;
import com.wingsweaver.kuja.common.messaging.common.SenderInfoResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * kuja-common-messaging 的广播机制的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = KujaCommonMessagingKeys.PREFIX_KUJA_COMMON_MESSAGING,
        name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SenderInfoResolverProperties.class)
public class KujaCommonMessagingCommonConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SenderInfoResolver kujaSenderInfoResolver(AppInfo appInfo, SenderInfoResolverProperties properties) {
        DefaultSenderInfoResolver resolver = new DefaultSenderInfoResolver();
        resolver.setAppInfo(appInfo);
        resolver.setExportItems(properties.getExportItems());
        return resolver;
    }
}
