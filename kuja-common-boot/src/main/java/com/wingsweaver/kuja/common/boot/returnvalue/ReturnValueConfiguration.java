package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.KujaCommonBootConfiguration;
import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.i18n.EnableKujaI18n;
import com.wingsweaver.kuja.common.boot.i18n.MessageHelper;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * 返回值功能的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ReturnValueProperties.class)
@EnableKujaI18n
@ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_RETURN_VALUE_PROPERTIES, name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(KujaCommonBootConfiguration.class)
public class ReturnValueConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReturnValueFactory returnValueFactory(MessageHelper messageHelper, ReturnValueProperties properties,
                                                 ObjectProvider<ReturnValueCustomizer> returnValueCustomizers) {
        DefaultReturnValueFactory factory = new DefaultReturnValueFactory();
        factory.setMessageHelper(messageHelper);
        factory.setSuccess(properties.getSuccess());
        factory.setFail(properties.getFail());
        factory.setCustomizers(returnValueCustomizers.orderedStream().collect(Collectors.toList()));
        return factory;
    }

    @Bean
    public ErrorReturnValueCustomizer errorReturnValueCustomizer(ReturnValueProperties properties) {
        ErrorReturnValueCustomizer customizer = new ErrorReturnValueCustomizer();
        customizer.setSettings(properties.getError());
        return customizer;
    }

    @Bean
    public AppInfoReturnValueCustomizer appInfoReturnValueCustomizer(ReturnValueProperties properties) {
        AppInfoReturnValueCustomizer customizer = new AppInfoReturnValueCustomizer();
        customizer.setSettings(properties.getAppInfo());
        return customizer;
    }
}
