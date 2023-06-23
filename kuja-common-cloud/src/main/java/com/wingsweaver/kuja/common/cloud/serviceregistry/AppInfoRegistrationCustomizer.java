package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import lombok.Data;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * 用于将 {@link AppInfo} 中的数据写入 {@link Registration} 中的工具类。
 *
 * @author wingsweaver
 */
@Data
public class AppInfoRegistrationCustomizer implements RegistrationCustomizer, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void customize(Registration registration) {
        try {
            AppInfo appInfo = this.applicationContext.getBean(AppInfo.class);
            Map<String, String> metadata = registration.getMetadata();
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
