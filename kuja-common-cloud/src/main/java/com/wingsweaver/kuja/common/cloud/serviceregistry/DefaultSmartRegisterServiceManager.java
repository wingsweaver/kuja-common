package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.boot.warmup.WarmUpCompleteEvent;
import com.wingsweaver.kuja.common.boot.warmup.WarmUpEvent;
import com.wingsweaver.kuja.common.boot.warmup.WarmUpProperties;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 默认的 {@link SmartRegisterServiceManager} 实现类。<br>
 * 其基于预热事件的监听，来判断是否可以注册服务。
 *
 * @author wingsweaver
 */
public class DefaultSmartRegisterServiceManager implements SmartRegisterServiceManager, ApplicationListener<WarmUpEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSmartRegisterServiceManager.class);

    private final ApplicationContext applicationContext;

    private Boolean isReady = null;

    private final List<ServiceRegistrationRequest<? extends Registration>> requests = new LinkedList<>();

    public DefaultSmartRegisterServiceManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public synchronized boolean canRegister(ServiceRegistry<? extends Registration> serviceRegistry, Registration registration) {
        // 初始化 isReady
        // 如果禁用了预热，那么无法延迟注册，直接返回 true
        if (this.isReady == null) {
            try {
                WarmUpProperties warmUpProperties = this.applicationContext.getBean(WarmUpProperties.class);
                if (warmUpProperties.isEnabled()) {
                    this.isReady = false;
                }
            } catch (Exception ignored) {
                LogUtil.info(LOGGER, "WarmUpProperties is not found, disable warm up");
                this.isReady = true;
            }
        }

        // 检查服务是否就绪
        if (this.isReady) {
            LogUtil.trace(LOGGER, "Service is ready, register service: registry = {}, registration = {}", serviceRegistry, registration);
            return true;
        }

        // 如果服务尚未就绪，那么需要先保存提交的注册请求，等待服务就绪后再进行注册。
        this.requests.add(new ServiceRegistrationRequest<Registration>((ServiceRegistry) serviceRegistry, registration));
        LogUtil.trace(LOGGER, "Defer register service: registry = {}, registration = {}", serviceRegistry, registration);
        return false;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onApplicationEvent(WarmUpEvent event) {
        if (event instanceof WarmUpCompleteEvent) {
            this.notifyReady();
        }
    }

    /**
     * 通知服务就绪，可以注册服务了。
     */
    public synchronized void notifyReady() {
        LogUtil.trace(LOGGER, "Notify ready, preparing to register services queued");
        this.isReady = true;
        this.requests.forEach(ServiceRegistrationRequest::register);
        this.requests.clear();
    }

    @Data
    private static class ServiceRegistrationRequest<R extends Registration> {
        private final ServiceRegistry<R> serviceRegistry;

        private final R registration;

        private ServiceRegistrationRequest(ServiceRegistry<R> serviceRegistry, R registration) {
            this.serviceRegistry = serviceRegistry;
            this.registration = registration;
        }

        void register() {
            LogUtil.trace(LOGGER, "Register service (delayed): registry = {}, registration = {}", serviceRegistry, registration);
            this.serviceRegistry.register(this.registration);
        }
    }
}
