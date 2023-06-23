package com.wingsweaver.kuja.common.cloud.discovery;

import com.wingsweaver.kuja.common.cloud.constants.KujaCommonCloudKeys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * 服务发现相关的设置。
 *
 * @author wingsweaver
 */
@ConfigurationProperties(prefix = KujaCommonCloudKeys.PREFIX_DISCOVERY_PROPERTIES)
@Data
public class DiscoveryProperties {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * 使用的服务的名称。
     */
    private List<String> serviceNames;

    /**
     * 仓库相关的设置。
     */
    @NestedConfigurationProperty
    private RepoProperties repo = new RepoProperties();

    /**
     * 仓库相关的设置。
     */
    @Data
    public static class RepoProperties {
        /**
         * Executor 的名称。
         */
        private String executor;
    }
}
