package com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.receive;

import com.wingsweaver.kuja.common.boot.EnableKujaCommonBoot;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotificationDispatcher;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotificationEventPublisher;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotificationHandler;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotificationPredicate;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.DefaultBroadcastNotificationDispatcher;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.DefaultBroadcastNotificationPredicate;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.built.BuiltInBroadcastNotificationHandler;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.built.CloudContextBroadcastNotificationHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * kuja-common-messaging 的广播机制的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableKujaCommonBoot
@EnableConfigurationProperties(BuiltInBroadcastNotificationHandlerProperties.class)
public class KujaCommonMessagingBroadcastReceiveConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public BroadcastNotificationPredicate kujBroadcastNotificationPredicate(AppInfo appInfo) {
        DefaultBroadcastNotificationPredicate notificationPredicate = new DefaultBroadcastNotificationPredicate();
        notificationPredicate.setAppInfo(appInfo);
        return notificationPredicate;
    }

    @Bean
    @ConditionalOnMissingBean
    public BroadcastNotificationDispatcher kujaBroadcastNotificationDispatcher(
            BroadcastNotificationPredicate broadcastNotificationPredicate,
            ObjectProvider<BroadcastNotificationHandler> handlers) {
        DefaultBroadcastNotificationDispatcher dispatcher = new DefaultBroadcastNotificationDispatcher();
        dispatcher.setNotificationPredicate(broadcastNotificationPredicate);
        dispatcher.setHandlers(handlers.orderedStream().collect(Collectors.toList()));
        return dispatcher;
    }

    @Bean
    public BroadcastNotificationEventPublisher kujaBroadcastNotificationEventPublisher() {
        return new BroadcastNotificationEventPublisher();
    }

    @Bean
    public BuiltInBroadcastNotificationHandler builtInBroadcastNotificationHandler(
            BuiltInBroadcastNotificationHandlerProperties properties) {
        BuiltInBroadcastNotificationHandler notificationHandler = new BuiltInBroadcastNotificationHandler();
        notificationHandler.setDisabledMessageTypes(properties.getDisabledMessageTypes());
        return notificationHandler;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.springframework.cloud.context.refresh.ContextRefresher")
    @EnableConfigurationProperties(BuiltInBroadcastNotificationHandlerProperties.class)
    public static class CloudContextConfiguration extends AbstractConfiguration {
        @Bean
        public CloudContextBroadcastNotificationHandler cloudContextBroadcastNotificationHandler(
                BuiltInBroadcastNotificationHandlerProperties properties) {
            CloudContextBroadcastNotificationHandler notificationHandler = new CloudContextBroadcastNotificationHandler();
            notificationHandler.setDisabledMessageTypes(properties.getDisabledMessageTypes());
            return notificationHandler;
        }
    }
}
