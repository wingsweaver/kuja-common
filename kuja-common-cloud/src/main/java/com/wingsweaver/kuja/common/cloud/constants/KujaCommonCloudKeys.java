package com.wingsweaver.kuja.common.cloud.constants;

/**
 * kuja-common-cloud 中的 Bean Key 定义。
 *
 * @author wingsweaver
 */
public interface KujaCommonCloudKeys {
    /**
     * kuja-common-cloud 模块的配置属性前缀。
     */
    String PREFIX_KUJA_COMMON_CLOUD = "kuja.cloud";

    /**
     * {@link com.wingsweaver.kuja.common.cloud.serviceregistry.ServiceRegistryProperties}。
     */
    String PREFIX_SERVICE_REGISTRY_PROPERTIES = PREFIX_KUJA_COMMON_CLOUD + ".service-registry";

    /**
     * {@link com.wingsweaver.kuja.common.cloud.discovery.DiscoveryProperties}。
     */
    String PREFIX_DISCOVERY_PROPERTIES = PREFIX_KUJA_COMMON_CLOUD + ".discovery";
}
