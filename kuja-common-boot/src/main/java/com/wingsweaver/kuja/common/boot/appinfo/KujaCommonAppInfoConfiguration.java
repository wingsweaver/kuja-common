package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.boot.CommonBootProperties;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * kuja-common-appinfo 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
public class KujaCommonAppInfoConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AppInfo appInfo() {
        return new DefaultAppInfo();
    }

    @Bean
    public DefaultAppInfoCustomizer defaultAppInfoCustomizer(CommonBootProperties properties) {
        DefaultAppInfoCustomizer appInfoCustomizer = new DefaultAppInfoCustomizer();
        appInfoCustomizer.setProperties(properties);
        return appInfoCustomizer;
    }

    @Bean
    public AppInfoCustomizationBean appInfoCustomizationBean(AppInfo appInfo,
                                                             ObjectProvider<AppInfoCustomizer> appInfoCustomizers) {
        AppInfoCustomizationBean customization = new AppInfoCustomizationBean();
        customization.setAppInfo(appInfo);
        customization.setAppInfoCustomizers(appInfoCustomizers.orderedStream().collect(Collectors.toList()));
        return customization;
    }
}
