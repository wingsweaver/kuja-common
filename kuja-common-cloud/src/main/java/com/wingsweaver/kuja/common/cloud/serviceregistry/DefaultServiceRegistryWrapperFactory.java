package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

/**
 * {@link DefaultServiceRegistryWrapper} 的工厂类。
 *
 * @author wingsweaver
 */
public class DefaultServiceRegistryWrapperFactory implements ServiceRegistryWrapperFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServiceRegistryWrapperFactory.class);

    public static final String CLASS_NAME_NET_BYTEBUDDY_BYTE_BUDDY = "net.bytebuddy.ByteBuddy";

    @Override
    public ServiceRegistryWrapper create() {
        if (!ClassUtils.isPresent(CLASS_NAME_NET_BYTEBUDDY_BYTE_BUDDY, this.getClass().getClassLoader())) {
            LogUtil.trace(LOGGER, "ByteBuddy is not present, so we cannot use DefaultServiceRegistryWrapper.");
            return null;
        }

        return new DefaultServiceRegistryWrapper();
    }
}
