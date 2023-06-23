package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.cloud.common.ServiceInstanceUtil;
import com.wingsweaver.kuja.common.cloud.constants.KujaCommonCloudOrders;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.core.Ordered;

/**
 * 默认的 {@link RegistrationCustomizer} 实现类。
 *
 * @author wingsweaver
 */
public class DefaultRegistrationCustomizer implements InitializingBean, RegistrationCustomizer, Ordered {
    @Getter
    @Setter
    private ServiceRegistryProperties properties;

    @Override
    public void customize(Registration registration) {
        // 导入自定义的元数据字典
        try {
            ServiceInstanceUtil.importMetadata(registration, this.properties.getMetadata());
        } catch (Exception ignored) {
            // 忽略此错误
        }
    }

    @Override
    public int getOrder() {
        return KujaCommonCloudOrders.DEFAULT_REGISTRATION_CUSTOMIZER;
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("properties", this.getProperties());
    }
}
