package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.cloud.constants.KujaCommonCloudKeys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 服务注册相关的配置属性。
 *
 * @author wingsweaver
 */
@Data
@ConfigurationProperties(prefix = KujaCommonCloudKeys.PREFIX_SERVICE_REGISTRY_PROPERTIES)
public class ServiceRegistryProperties {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * 服务注册的元数据字典。
     */
    private Map<String, String> metadata;

    /**
     * 预热处理完成之后才注册本服务到服务注册中心。
     */
    private boolean smartRegister = true;

    /**
     * 是否将数据从 {@link org.springframework.cloud.client.serviceregistry.Registration} 中
     * 拷贝到 {@link com.wingsweaver.kuja.common.boot.appinfo.AppInfo} 中。
     */
    private boolean copyToAppInfo = true;

    /**
     * 是否将数据从 {@link com.wingsweaver.kuja.common.boot.appinfo.AppInfo} 中
     * 拷贝到 {@link org.springframework.cloud.client.serviceregistry.Registration} 中。
     */
    private boolean copyFromAppInfo = true;
}
