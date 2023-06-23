package com.wingsweaver.kuja.common.boot;

import com.wingsweaver.kuja.common.boot.appinfo.EnableKujaCommonAppInfo;
import com.wingsweaver.kuja.common.boot.context.BusinessContextFactory;
import com.wingsweaver.kuja.common.boot.errordefinition.EnableKujaErrorDefinition;
import com.wingsweaver.kuja.common.boot.errorhandling.EnableKujaErrorHandling;
import com.wingsweaver.kuja.common.boot.exception.EnableKujaException;
import com.wingsweaver.kuja.common.boot.i18n.EnableKujaI18n;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.boot.returnvalue.EnableKujaReturnValue;
import com.wingsweaver.kuja.common.boot.warmup.EnableKujaWarmUp;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * kuja-common-boot 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CommonBootProperties.class)
@EnableKujaI18n
@EnableKujaCommonAppInfo
@EnableKujaWarmUp
@EnableKujaReturnValue
@EnableKujaErrorDefinition
@EnableKujaException
@EnableKujaErrorHandling
public class KujaCommonBootConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public BusinessContextFactory businessContextFactory() {
        return BusinessContextFactory.DEFAULT;
    }
}
