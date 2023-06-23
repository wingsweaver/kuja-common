package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.boot.CommonBootProperties;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 默认的 {@link AppInfoCustomizer} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultAppInfoCustomizer implements AppInfoCustomizer, ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    private CommonBootProperties properties;

    @Override
    public void customize(AppInfo appInfo) {
        // 导入默认的一些设置信息
        this.loadDefaultAppInfo(appInfo);

        // 将 CommonBootProperties 中的 app 信息导入到 AppInfo 中
        appInfo.importMap(this.properties.getApp(), true);
    }

    private void loadDefaultAppInfo(AppInfo appInfo) {
        Environment environment = this.applicationContext.getEnvironment();

        // 主机信息
        String hostName = environment.getProperty("spring.cloud.client.hostname", SystemUtils.getHostName());
        appInfo.setAttributeIfAbsent(ResourceAttributes.Host.KEY_NAME, hostName);
        String hostAddress = environment.getProperty("spring.cloud.client.ip-address");
        if (StringUtil.isEmpty(hostAddress)) {
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                // 忽略此错误
            }
        }
        appInfo.setAttributeIfAbsent(ResourceAttributes.Host.KEY_ADDRESS, hostAddress);

        // 进程信息
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String pidText = runtime.getName().split("@")[0];
        appInfo.setAttributeIfAbsent(ResourceAttributes.Process.KEY_PID, Long.valueOf(pidText));

        // 主机信息
        appInfo.setAttributeIfAbsent(ResourceAttributes.Service.KEY_NAME,
                environment.getProperty("spring.application.name"));
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("applicationContext", this.getApplicationContext());
        AssertState.Named.notNull("properties", this.getProperties());
    }
}
