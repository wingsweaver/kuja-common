package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import org.springframework.cloud.client.serviceregistry.Registration;

/**
 * 用于对 {@link Registration} 进行定制的接口定义。
 *
 * @author wingsweaver
 */
public interface RegistrationCustomizer extends DefaultOrdered {
    /**
     * 对 {@link Registration} 进行定制。
     *
     * @param registration Registration 实例
     */
    void customize(Registration registration);
}
