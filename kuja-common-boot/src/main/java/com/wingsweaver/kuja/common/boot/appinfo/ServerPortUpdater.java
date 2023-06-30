package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.model.semconv.SemanticAttributes;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * 用于更新 {@code server.port} 的处理器。<br>
 * 即 {@code server.port} 设置为 0 时，Web 服务器会随机分配一个端口，此时需要相关数据。
 *
 * @author wingsweaver
 */
public class ServerPortUpdater extends AbstractComponent implements ApplicationListener<WebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        // 检查是否需要处理
        try {
            ServerProperties serverProperties = this.getBean(ServerProperties.class);
            Integer port = serverProperties.getPort();
            if (port != 0) {
                return;
            }
        } catch (Exception ignored) {
            // 忽略此错误
        }

        // 获取实际使用的 Web 服务器端口
        int port = event.getWebServer().getPort();

        // 更新 AppInfo
        AppInfo appInfo = this.getBean(AppInfo.class);
        appInfo.setAttribute(SemanticAttributes.Net.KEY_HOST_PORT, port);

        // 更新环境变量
        ConfigurableEnvironment environment = ObjectUtil.cast(this.getApplicationContext().getEnvironment(),
                ConfigurableEnvironment.class);
        if (environment != null) {
            MapPropertySource propertySource = ObjectUtil.cast(
                    environment.getPropertySources().get(KujaCommonBootKeys.PropertySourceNames.APP_INFO_FALLBACK),
                    MapPropertySource.class);
            if (propertySource != null) {
                propertySource.getSource().put(SemanticAttributes.Net.KEY_HOST_PORT, port);
                propertySource.getSource().put(KujaCommonBootKeys.PREFIX_APP + "." + SemanticAttributes.Net.KEY_HOST_PORT, port);
            }
        }
    }
}
