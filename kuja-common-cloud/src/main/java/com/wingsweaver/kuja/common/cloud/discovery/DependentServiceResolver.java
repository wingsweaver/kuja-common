package com.wingsweaver.kuja.common.cloud.discovery;

import java.util.Collection;

/**
 * 用于解析依赖服务的接口。
 *
 * @author wingsweaver
 */
public interface DependentServiceResolver {
    /**
     * 解析依赖的服务名称，并且保存到指定集合中。
     *
     * @param serviceNames 保存服务名称的集合
     */
    void resolve(Collection<String> serviceNames);
}
