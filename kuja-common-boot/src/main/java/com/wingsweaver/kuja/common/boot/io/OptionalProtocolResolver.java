package com.wingsweaver.kuja.common.boot.io;

import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 可选（非必须） Resource 的 {@link ProtocolResolver} 实现类。
 *
 * @author wingsweaver
 */
public class OptionalProtocolResolver implements ProtocolResolver {
    public static final String PREFIX_OPTIONAL = OptionalResource.PREFIX_OPTIONAL;

    @SuppressWarnings("ConstantConditions")
    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        // 检查前缀
        if (!location.startsWith(PREFIX_OPTIONAL)) {
            return null;
        }

        // 加载有效的 Resource
        String realLocation = location.substring(PREFIX_OPTIONAL.length());
        Resource resource = resourceLoader.getResource(realLocation);

        // 返回
        return (resource != null) ? new OptionalResource(resource) : null;
    }
}
