package com.wingsweaver.kuja.common.messaging.broadcast.receive.built;

import com.wingsweaver.kuja.common.messaging.broadcast.common.BuiltInMessageTypes;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotification;
import com.wingsweaver.kuja.common.messaging.broadcast.send.builtin.ConfigLogLevelMessage;
import com.wingsweaver.kuja.common.messaging.broadcast.send.builtin.RevokeCacheMessage;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ObjectUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ThreadUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LoggerGroup;
import org.springframework.boot.logging.LoggerGroups;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 内置消息的处理器。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@SuppressWarnings({"PMD.GuardLogStatement", "PMD.AvoidManuallyCreateThreadRule"})
public class BuiltInBroadcastNotificationHandler extends AbstractBuiltInNotificationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuiltInBroadcastNotificationHandler.class);

    /**
     * 是否包含 ContextRefresher。
     */
    private boolean hasContextRefresher;

    @Override
    public void onBroadcastNotification(BroadcastNotification notification) throws Exception {
        String messageType = notification.getMessageType();
        if (StringUtil.isEmpty(messageType)) {
            return;
        }

        switch (messageType) {
            case BuiltInMessageTypes.SHUTDOWN_APPLICATION:
                this.shutDownApplication(notification);
                break;

            case BuiltInMessageTypes.REFRESH_APPLICATION_CONTEXT:
                this.refreshApplicationContext(notification);
                break;

            case BuiltInMessageTypes.REVOKE_CACHE:
                this.revokeCache(notification);
                break;

            case BuiltInMessageTypes.CONFIG_LOG_LEVEL:
                this.configLoveLevel(notification);
                break;

            default:
                break;
        }
    }

    private void configLoveLevel(BroadcastNotification notification) {
        // 检查是否被禁用
        if (this.isDisabled(notification)) {
            return;
        }

        List<ConfigLogLevelMessage.Config> configs = notification.getContent().asList(ConfigLogLevelMessage.Config.class);
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }

        LoggingSystem loggingSystem = this.getApplicationContext().getBean(LoggingSystem.class);
        LoggerGroups loggerGroups = this.getApplicationContext().getBeanProvider(LoggerGroups.class)
                .getIfAvailable(LoggerGroups::new);
        for (ConfigLogLevelMessage.Config config : configs) {
            LoggerGroup group = loggerGroups.get(config.getName());
            if (group != null && group.hasMembers()) {
                LogUtil.trace(LOGGER, "Configuring log level for group {} to {} on notification #{}",
                        config.getName(), config.getLogLevel(), notification.getId());
                group.configureLogLevel(config.getLogLevel(), loggingSystem::setLogLevel);
            } else {
                LogUtil.trace(LOGGER, "Configuring log level for logger {} to {} on notification #{}",
                        config.getName(), config.getLogLevel(), notification.getId());
                loggingSystem.setLogLevel(config.getName(), config.getLogLevel());
            }
        }
    }

    private void revokeCache(BroadcastNotification notification) {
        // 检查是否被禁用
        if (this.isDisabled(notification)) {
            return;
        }

        List<RevokeCacheMessage.Config> configs = notification.getContent().asList(RevokeCacheMessage.Config.class);
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }

        for (RevokeCacheMessage.Config config : configs) {
            // 查找对应的缓存
            Cache cache = this.resolveCache(config.getCacheName());
            if (cache == null) {
                LogUtil.trace(LOGGER, "Cannot find cache with name {}, notification message id = {}",
                        config.getCacheName(), notification.getId());
                continue;
            }

            // 清空缓存中的内容
            if (CollectionUtils.isEmpty(config.getKeys())) {
                LogUtil.trace(LOGGER, "Clearing cache content {} on notification #{}", config.getCacheName(), notification.getId());
                cache.clear();
            } else {
                for (String key : config.getKeys()) {
                    LogUtil.trace(LOGGER, "Evicting cache {} with key {} on notification #{}",
                            config.getCacheName(), key, notification.getId());
                    cache.evict(key);
                }
            }
        }
    }

    private Cache resolveCache(String cacheName) {
        Cache cache = null;

        // 尝试按照 Bean 来查找
        try {
            cache = this.getApplicationContext().getBean(cacheName, Cache.class);
        } catch (Exception ignored) {
            // 忽略此错误
        }

        // 再尝试从 CacheManager 中查找
        try {
            CacheManager cacheManager = this.getApplicationContext().getBean(CacheManager.class);
            cache = cacheManager.getCache(cacheName);
        } catch (Exception ignored) {
            // 忽略此错误
        }

        // 返回结果
        return cache;
    }

    private void refreshApplicationContext(BroadcastNotification notification) {
        // 检查是否被禁用
        if (this.isDisabled(notification)) {
            return;
        }

        if (this.hasContextRefresher) {
            // 如果含有 ContextRefresher，那么由 ContextRefresher 进行处理
            return;
        }

        ConfigurableApplicationContext applicationContext = ObjectUtil.cast(this.getApplicationContext(), ConfigurableApplicationContext.class);
        if (applicationContext == null) {
            LogUtil.trace(LOGGER, "Application context is not ConfigurableApplicationContext, cannot refresh context");
            return;
        }

        Thread thread = new Thread(() -> this.refreshApplicationContextProc(notification));
        thread.setContextClassLoader(getClass().getClassLoader());
        thread.start();
    }

    private void refreshApplicationContextProc(BroadcastNotification notification) {
        try {
            LogUtil.trace(LOGGER, "Refreshing application context on notification #{}", notification.getId());
            ((ConfigurableApplicationContext) this.getApplicationContext()).refresh();
        } catch (Exception ex) {
            LogUtil.error(LOGGER, ex, "Failed to refresh application context");
        }
    }

    private void shutDownApplication(BroadcastNotification notification) {
        // 检查是否被禁用
        if (this.isDisabled(notification)) {
            return;
        }

        ConfigurableApplicationContext applicationContext = ObjectUtil.cast(this.getApplicationContext(), ConfigurableApplicationContext.class);
        if (applicationContext == null) {
            LogUtil.trace(LOGGER, "Application context is not ConfigurableApplicationContext, cannot shutdown");
            return;
        }

        Thread thread = new Thread(() -> this.shutDownApplicationProc(notification));
        thread.setContextClassLoader(getClass().getClassLoader());
        thread.start();
    }

    private void shutDownApplicationProc(BroadcastNotification notification) {
        ThreadUtil.sleep(1000);
        try {
            LogUtil.trace(LOGGER, "Shutting down application on notification #{}", notification.getId());
            ((ConfigurableApplicationContext) this.getApplicationContext()).close();
        } catch (Exception ex) {
            LogUtil.error(LOGGER, ex, "Failed to shutdown application");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 isSpringCloud
        this.hasContextRefresher = ClassUtil.exists("org.springframework.cloud.context.refresh.ContextRefresher");
    }
}
