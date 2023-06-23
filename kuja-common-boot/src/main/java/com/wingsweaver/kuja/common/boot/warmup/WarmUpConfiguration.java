package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 预热处理的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(WarmUpProperties.class)
@ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_WARM_UP_PROPERTIES, name = "enabled", havingValue = "true", matchIfMissing = true)
public class WarmUpConfiguration extends AbstractConfiguration {
    @Bean
    public WarmUpTriggerBean warmUpTriggerBean(ApplicationContext applicationContext, WarmUpProperties warmUpProperties) {
        WarmUpTriggerBean warmUpTriggerBean = new WarmUpTriggerBean();
        warmUpTriggerBean.setApplicationContext(applicationContext);
        warmUpTriggerBean.setProperties(warmUpProperties);
        return warmUpTriggerBean;
    }
}
