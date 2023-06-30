package com.wingsweaver.kuja.common.messaging.autoconfigurer.errorreporting;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.common.SenderInfoResolver;
import com.wingsweaver.kuja.common.messaging.errorreporting.common.MessageErrorReporter;
import com.wingsweaver.kuja.common.messaging.errorreporting.send.ErrorRecordDestinationResolver;
import com.wingsweaver.kuja.common.messaging.errorreporting.send.ErrorRecordPayloadResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * kuja-common-messaging 的错误上报的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = KujaCommonMessagingErrorReportingKeys.PREFIX,
        name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({MessageErrorReportingProperties.class, MessageErrorReporterProperties.class,
        ErrorRecordPayloadResolverProperties.class})
public class KujaCommonMessagingErrorReportingConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = KujaCommonMessagingErrorReportingKeys.PREFIX_MESSAGE_ERROR_REPORTER,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public MessageErrorReporter kujaMessageErrorReporter() {
        return new MessageErrorReporter();
    }

    @Bean
    public ErrorRecordPayloadResolver kujaErrorRecordPayloadResolver(
            SenderInfoResolver senderInfoResolver,
            ErrorRecordPayloadResolverProperties properties) {
        ErrorRecordPayloadResolver payloadResolver = new ErrorRecordPayloadResolver();
        payloadResolver.setSenderInfoResolver(senderInfoResolver);
        payloadResolver.setErrorInfoInclude(properties.getErrorInfo());
        payloadResolver.setErrorTagsInclude(properties.getErrorTags());
        return payloadResolver;
    }

    @Bean
    public ErrorRecordDestinationResolver kujaErrorRecordDestinationResolver(MessageErrorReportingProperties properties) {
        ErrorRecordDestinationResolver destinationResolver = new ErrorRecordDestinationResolver();
        destinationResolver.setDefaultDestination(properties.getDefaultDestination());
        return destinationResolver;
    }
}
