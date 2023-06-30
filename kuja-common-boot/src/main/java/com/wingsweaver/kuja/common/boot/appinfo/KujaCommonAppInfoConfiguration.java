package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.boot.CommonBootProperties;
import com.wingsweaver.kuja.common.boot.appinfo.properties.CloudProperties;
import com.wingsweaver.kuja.common.boot.appinfo.properties.ContainerProperties;
import com.wingsweaver.kuja.common.boot.appinfo.properties.DeploymentProperties;
import com.wingsweaver.kuja.common.boot.appinfo.properties.DeviceProperties;
import com.wingsweaver.kuja.common.boot.appinfo.properties.HostProperties;
import com.wingsweaver.kuja.common.boot.appinfo.properties.OsProperties;
import com.wingsweaver.kuja.common.boot.appinfo.properties.ServiceProperties;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * kuja-common-appinfo 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CloudProperties.class, ContainerProperties.class, DeploymentProperties.class,
        DeviceProperties.class, HostProperties.class, OsProperties.class, ServiceProperties.class})
public class KujaCommonAppInfoConfiguration extends AbstractConfiguration {
    /**
     * 创建 AppInfo 对象。
     *
     * @return AppInfo 对象
     */
    @Bean
    @ConditionalOnMissingBean
    public AppInfo appInfo() {
        return new DefaultAppInfo();
    }

    /**
     * 创建 DefaultAppInfoCustomizer 对象。
     *
     * @param properties CommonBootProperties 对象
     * @return DefaultAppInfoCustomizer 对象
     */
    @Bean
    public DefaultAppInfoCustomizer defaultAppInfoCustomizer(CommonBootProperties properties) {
        DefaultAppInfoCustomizer appInfoCustomizer = new DefaultAppInfoCustomizer();
        appInfoCustomizer.setProperties(properties);
        return appInfoCustomizer;
    }

    /**
     * 创建 AppInfoCustomizationBean 对象。
     *
     * @param appInfo            AppInfo 对象
     * @param appInfoCustomizers AppInfoCustomizer 数组
     * @return AppInfoCustomizationBean 对象
     */
    @Bean
    public AppInfoCustomizationBean appInfoCustomizationBean(AppInfo appInfo,
                                                             ObjectProvider<AppInfoCustomizer> appInfoCustomizers) {
        AppInfoCustomizationBean customization = new AppInfoCustomizationBean();
        customization.setAppInfo(appInfo);
        customization.setAppInfoCustomizers(appInfoCustomizers.orderedStream().collect(Collectors.toList()));
        return customization;
    }

    /**
     * 创建 ServerPortUpdater 对象。
     *
     * @return ServerPortUpdater 对象
     */
    @Bean
    public ServerPortUpdater serverPortUpdater() {
        return new ServerPortUpdater();
    }
}
