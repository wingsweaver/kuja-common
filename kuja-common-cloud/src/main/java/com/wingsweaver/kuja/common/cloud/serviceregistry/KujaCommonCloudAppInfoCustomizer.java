package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoCustomizer;
import lombok.Data;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 用于将 {@link Registration} 中的信息写入到 {@link AppInfo} 中的工具类。
 *
 * @author wingsweaver
 */
@Data
public class KujaCommonCloudAppInfoCustomizer implements AppInfoCustomizer, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void customize(AppInfo appInfo) {
        try {
            Registration registration = this.applicationContext.getBean(Registration.class);
            appInfo.importMap(registration.getMetadata(), false);
        } catch (Exception ignored) {
            // 忽略此错误
        }
    }
}
