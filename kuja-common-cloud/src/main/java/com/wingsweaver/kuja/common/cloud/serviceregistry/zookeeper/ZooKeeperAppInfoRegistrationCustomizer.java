package com.wingsweaver.kuja.common.cloud.serviceregistry.zookeeper;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.cloud.serviceregistry.RegistrationCustomizer;
import lombok.Data;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * 用于将 {@link AppInfo} 中的数据写入 {@link Registration} 中的工具类。
 *
 * @author wingsweaver
 */
@Data
public class ZooKeeperAppInfoRegistrationCustomizer implements RegistrationCustomizer, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void customize(Registration registration) {
        try {
            AppInfo appInfo = this.applicationContext.getBean(AppInfo.class);
            ZookeeperDiscoveryProperties properties = this.applicationContext.getBean(ZookeeperDiscoveryProperties.class);
            Map<String, String> metadata = properties.getMetadata();
            appInfo.asMap().forEach((key, value) -> {
                if (value != null) {
                    metadata.putIfAbsent(key, value.toString());
                }
            });
        } catch (Exception ignored) {
            // 忽略此错误
        }
    }
}
