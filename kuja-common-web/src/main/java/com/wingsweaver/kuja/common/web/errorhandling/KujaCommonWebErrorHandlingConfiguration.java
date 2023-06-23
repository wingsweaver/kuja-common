package com.wingsweaver.kuja.common.web.errorhandling;

import com.wingsweaver.kuja.common.boot.EnableKujaCommonBoot;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlingDelegate;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.web.constants.KujaCommonWebKeys;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * kuja-common-web 错误处理的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableKujaCommonBoot
@EnableConfigurationProperties({ErrorHandlingProperties.class})
public class KujaCommonWebErrorHandlingConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonWebKeys.PREFIX_GLOBAL_ERROR_ADVICE_PROPERTIES, name = "enabled",
            havingValue = "true", matchIfMissing = true)
    public GlobalErrorAdvice globalErrorAdvice(ErrorHandlingDelegate errorHandlingDelegate) {
        GlobalErrorAdvice errorAdvice = new GlobalErrorAdvice();
        errorAdvice.setErrorHandlingDelegate(errorHandlingDelegate);
        return errorAdvice;
    }

    @Bean
    public ResponseDataErrorHandler responseDataErrorHandler() {
        return new ResponseDataErrorHandler();
    }
}
