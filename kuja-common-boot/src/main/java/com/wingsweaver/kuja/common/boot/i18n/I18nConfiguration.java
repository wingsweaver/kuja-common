package com.wingsweaver.kuja.common.boot.i18n;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.utils.constants.Orders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * i18n 功能的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
public class I18nConfiguration extends AbstractConfiguration {
    @Bean
    @Order(Orders.HIGHEST_PRECEDENCE)
    public MessageSourceHolder messageSourceHolder() {
        return new MessageSourceHolder();
    }

    @Bean
    public MessageHelper messageHelper() {
        return new MessageHelper();
    }
}
