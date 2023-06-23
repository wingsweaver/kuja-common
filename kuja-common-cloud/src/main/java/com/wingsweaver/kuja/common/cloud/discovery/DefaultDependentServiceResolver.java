package com.wingsweaver.kuja.common.cloud.discovery;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 默认的{@link DependentServiceResolver}实现类。<br>
 * 使用 {@link DiscoveryProperties} 中的服务名称。
 *
 * @author wingsweaver
 */
public class DefaultDependentServiceResolver implements DependentServiceResolver, InitializingBean, DefaultOrdered {
    @Getter
    @Setter
    private DiscoveryProperties properties;

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("properties", this.getProperties());
    }

    @Override
    public void resolve(Collection<String> serviceNames) {
        List<String> tempServiceNames = this.properties.getServiceNames();
        if (!CollectionUtils.isEmpty(tempServiceNames)) {
            serviceNames.addAll(tempServiceNames);
        }
    }
}
