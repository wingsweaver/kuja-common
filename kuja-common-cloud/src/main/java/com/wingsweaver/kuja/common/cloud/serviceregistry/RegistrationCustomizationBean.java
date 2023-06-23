package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.cloud.constants.KujaCommonCloudOrders;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

/**
 * 用于初始化 {@link Registration} 的 Bean。<br>
 * 实现 {@link SmartInitializingSingleton}，在应用程序启动之前执行定制化操作。
 *
 * @author wingsweaver
 */
public class RegistrationCustomizationBean implements InitializingBean, SmartInitializingSingleton, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationCustomizationBean.class);

    @Getter
    @Setter
    private ApplicationContext applicationContext;

    @Override
    public void afterSingletonsInstantiated() {
        Registration registration = this.applicationContext.getBean(Registration.class);
        ObjectProvider<RegistrationCustomizer> registrationCustomizers = this.applicationContext.getBeanProvider(RegistrationCustomizer.class);
        registrationCustomizers.orderedStream().forEach(registrationCustomizer -> {
            LogUtil.trace(LOGGER, "customizing registration {} with {} ...", registration, registrationCustomizer);
            registrationCustomizer.customize(registration);
        });
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("applicationContext", this.getApplicationContext());
    }

    @Override
    public int getOrder() {
        return KujaCommonCloudOrders.REGISTRATION_CUSTOMIZATION_BEAN;
    }
}
