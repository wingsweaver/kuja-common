package com.wingsweaver.kuja.common.cloud.serviceregistry;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.CloseableWrapper;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 基于 ByteBuddy 的 {@link ServiceRegistry} 封装辅助工具类。
 *
 * @author wingsweaver
 */
public class ByteBuddyServiceRegistryHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteBuddyServiceRegistryHelper.class);

    public static final String METHOD_NAME_REGISTER = "register";

    /**
     * 初始化 ByteBuddy Builder 的接口定义。
     */
    public interface BuilderInitializer {
        /**
         * 初始化 ByteBuddy Builder。
         *
         * @param builder ByteBuddy Builder
         * @return 初始化后的 ByteBuddy Builder
         */
        DynamicType.Builder<?> initialize(DynamicType.Builder<?> builder);
    }

    private final ApplicationContext applicationContext;

    private final ServiceRegistry<? extends Registration> serviceRegistry;

    private final Class<?> serviceRegistryClass;

    /**
     * {@link #serviceRegistry} 对应的类型是否有默认构造函数。<br>
     * 如果没有的话，就需要为 Proxy 类型构建默认的构造函数。
     */
    private final boolean hasDefaultConstructor;

    public ByteBuddyServiceRegistryHelper(ApplicationContext applicationContext, ServiceRegistry<? extends Registration> serviceRegistry) {
        this.applicationContext = applicationContext;
        this.serviceRegistry = serviceRegistry;
        this.serviceRegistryClass = serviceRegistry.getClass();
        this.hasDefaultConstructor = ClassUtils.hasConstructor(this.serviceRegistryClass);
    }

    /**
     * 为 {@link #serviceRegistry} 实例生成代理类。
     *
     * @param builderInitializers Proxy 类型的初始化处理的实例
     * @return 代理类的实例
     * @throws IOException                  发生 I/O 错误
     * @throws ReflectiveOperationException 发生反射错误
     */
    public Object wrapServiceRegistry(BuilderInitializer... builderInitializers)
            throws IOException, ReflectiveOperationException {
        // 生成子类
        // 具体处理，由 builderInitializers 完成
        DynamicType.Builder<?> builder = new ByteBuddy().subclass(this.serviceRegistryClass);
        for (BuilderInitializer builderInitializer : builderInitializers) {
            if (builderInitializer != null) {
                builder = builderInitializer.initialize(builder);
            }
        }

        // 生成实例
        try (CloseableWrapper<DynamicType.Unloaded<?>> unloaded = new CloseableWrapper<>(builder.make())) {
            try (CloseableWrapper<DynamicType.Loaded<?>> loaded =
                         new CloseableWrapper<>(unloaded.getDelegate().load(applicationContext.getClassLoader()))) {
                return loaded.getDelegate().getLoaded().getDeclaredConstructor().newInstance();
            }
        }
    }

    /**
     * 生成代理类的 Method 。
     *
     * @param builder ByteBuddy Builder
     * @return 更新后的 ByteBuddy Builder
     */
    public DynamicType.Builder<?> wrapMethods(DynamicType.Builder<?> builder) {
        return builder.method(ElementMatchers.isDeclaredBy(serviceRegistryClass))
                .intercept(MethodDelegation.to(this));
    }

    /**
     * 代理类的 Method 调用的拦截器。
     *
     * @param instance 代理类的实例
     * @param method   被调用的 Method
     * @param args     调用参数
     * @return 调用结果
     * @throws Exception 发生错误
     */
    @RuntimeType
    public Object interceptor(@This Object instance, @Origin Method method, @AllArguments Object[] args) throws Exception {
        Object returnValue = null;
        if (METHOD_NAME_REGISTER.equals(method.getName()) && method.getParameterCount() == 1) {
            // 检查是否要延迟注册服务
            Registration registration = (Registration) args[0];
            if (canRegister(registration)) {
                LogUtil.trace(LOGGER, "Can register service. Registry = {}, registration = {}", serviceRegistry, registration);
                returnValue = method.invoke(serviceRegistry, args);
            } else {
                LogUtil.trace(LOGGER, "Can NOT register service now. Registry = {}, registration = {}", serviceRegistry, registration);
            }
        } else {
            returnValue = method.invoke(serviceRegistry, args);
        }

        return returnValue;
    }

    /**
     * 检查是否可以注册服务。
     *
     * @param registration 注册信息
     * @return 是否可以注册服务
     */
    protected boolean canRegister(Registration registration) {
        boolean result = false;
        try {
            SmartRegisterServiceManager deferRegisterServiceManager = applicationContext.getBean(SmartRegisterServiceManager.class);
            result = deferRegisterServiceManager.canRegister(serviceRegistry, registration);
        } catch (Exception ex) {
            // 什么也不做
        }

        // 返回
        return result;
    }

    /**
     * 为代理类生成默认的构造函数。
     *
     * @param builder ByteBuddy Builder
     * @return 更新后的 ByteBuddy Builder
     */
    public DynamicType.Builder<?> wrapDefaultConstructor(DynamicType.Builder<?> builder) {
        if (hasDefaultConstructor) {
            // 如果有默认的构造函数，那么无需生成构造函数
            return builder;
        } else {
            Constructor<?> constructor = serviceRegistryClass.getConstructors()[0];
            return builder.defineConstructor(Visibility.PUBLIC)
                    .intercept(
                            MethodCall.invoke(constructor).onSuper().with(this.resolveParameters(constructor)));
        }
    }

    /**
     * 计算构造函数的参数列表。
     *
     * @param executable 构造函数
     * @return 参数列表
     */
    private Object[] resolveParameters(Executable executable) {
        Parameter[] parameters = executable.getParameters();
        Object[] values = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            if (type.isPrimitive()) {
                if (type == boolean.class) {
                    values[i] = false;
                } else if (type == byte.class) {
                    values[i] = (byte) 0;
                } else if (type == short.class) {
                    values[i] = (short) 0;
                } else if (type == int.class) {
                    values[i] = 0;
                } else if (type == long.class) {
                    values[i] = 0L;
                } else if (type == float.class) {
                    values[i] = 0.0f;
                } else if (type == double.class) {
                    values[i] = 0.0d;
                } else if (type == char.class) {
                    values[i] = '\u0000';
                }
            } else {
                values[i] = null;
            }
        }
        return values;
    }
}

