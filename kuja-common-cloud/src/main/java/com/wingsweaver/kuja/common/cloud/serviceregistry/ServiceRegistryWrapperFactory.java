package com.wingsweaver.kuja.common.cloud.serviceregistry;

/**
 * {@link ServiceRegistryWrapper} 工厂类的接口。
 *
 * @author wingsweaver
 */
public interface ServiceRegistryWrapperFactory {
    /**
     * 创建 {@link ServiceRegistryWrapper} 实例。
     *
     * @return {@link ServiceRegistryWrapper} 实例
     */
    ServiceRegistryWrapper create();
}
