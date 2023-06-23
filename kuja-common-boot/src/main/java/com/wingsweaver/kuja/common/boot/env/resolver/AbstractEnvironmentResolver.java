package com.wingsweaver.kuja.common.boot.env.resolver;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessorApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * {@link EnvironmentResolver} 的实现类的基类。
 *
 * @author wingsweaver
 */
public abstract class AbstractEnvironmentResolver implements EnvironmentResolver {
    private static final String[] EMPTY_ARGUMENTS = new String[0];

    /**
     * 获取设置用的 ConfigurableEnvironment 的初始化处理。
     */
    private UnaryOperator<ConfigurableEnvironment> environmentInitializer;

    /**
     * 获取设置用的 SpringApplicationBuilder 的初始化处理。
     */
    private UnaryOperator<SpringApplicationBuilder> springApplicationBuilderInitializer;

    /**
     * 获取设置用的 SpringApplication 的初始化处理。
     */
    private UnaryOperator<SpringApplication> springApplicationInitializer = this::defaultSpringApplicationInitializer;

    /**
     * 获取设置用的 SpringApplication 的启动参数。
     */
    private String[] arguments = EMPTY_ARGUMENTS;

    /**
     * 预处理器的列表。
     */
    private List<EnvironmentPostProcessor> preProcessors;

    /**
     * 后处理器的列表。
     */
    private List<EnvironmentPostProcessor> postProcessors;

    protected AbstractEnvironmentResolver() {
        this.getPreProcessors(true).add(new PreEnvironmentResolverProcessor());
        this.getPostProcessors(true).add(new PostEnvironmentResolverProcessor());
    }

    @Override
    public ConfigurableEnvironment resolve() {
        // 生成模拟用的环境实例
        ConfigurableEnvironment environment = this.createEnvironment();

        // 生成环境实例
        SpringApplicationBuilder springApplicationBuilder = this.createSpringApplicationBuilder(environment);
        SpringApplication springApplication = this.createSpringApplication(springApplicationBuilder, environment);
        this.preHandleEnvironment(springApplication, environment);
        environment = this.resolveInternal(springApplication, environment);
        this.postHandleEnvironment(springApplication, environment);

        // 返回
        return environment;
    }

    /**
     * 默认的 {@link SpringApplication} 初始化处理。
     *
     * @param springApplication SpringApplication 实例
     * @return 处理后的 SpringApplication 实例
     */
    protected SpringApplication defaultSpringApplicationInitializer(SpringApplication springApplication) {
        springApplication.setListeners(this.resolveApplicationListeners(springApplication));
        return springApplication;
    }

    /**
     * 计算所需的 {@link ApplicationListener} 实例。
     *
     * @param springApplication SpringApplication 实例
     * @return 处理后的 ApplicationListener 实例的列表
     */
    protected Collection<? extends ApplicationListener<?>> resolveApplicationListeners(SpringApplication springApplication) {
        Set<ApplicationListener<?>> oldListeners = springApplication.getListeners();
        List<ApplicationListener<?>> newListeners = new ArrayList<>(oldListeners.size());
        for (ApplicationListener<?> applicationListener : oldListeners) {
            if ((applicationListener instanceof EnvironmentPostProcessorApplicationListener)
                    || (applicationListener.getClass().getName().startsWith("org.springframework.cloud.bootstrap."))) {
                newListeners.add(applicationListener);
            }
        }
        return newListeners;
    }

    /**
     * 对环境进行后处理。<br>
     * 如：删除预处理中注入的临时设置。
     *
     * @param springApplication 解析用的 SpringApplication 实例
     * @param environment       解析用的 Environment 实例
     */
    protected void postHandleEnvironment(SpringApplication springApplication, ConfigurableEnvironment environment) {
        if (!CollectionUtils.isEmpty(this.postProcessors)) {
            this.postProcessors.forEach(environmentPostProcessor -> environmentPostProcessor.postProcessEnvironment(environment, springApplication));
        }
    }

    /**
     * 实际的解析应用设置的处理。
     *
     * @param springApplication 解析用的 SpringApplication 实例
     * @param environment       解析用的 Environment 实例
     * @return 解析结果
     */
    protected abstract ConfigurableEnvironment resolveInternal(SpringApplication springApplication, ConfigurableEnvironment environment);

    /**
     * 对环境进行预处理。<br>
     * 如：添加一些设置，以跳过远程配置中心（如果需要的话）。
     *
     * @param springApplication 解析用的 SpringApplication 实例
     * @param environment       解析用的 Environment 实例
     */
    protected void preHandleEnvironment(SpringApplication springApplication, ConfigurableEnvironment environment) {
        if (!CollectionUtils.isEmpty(this.preProcessors)) {
            this.preProcessors.forEach(environmentPostProcessor -> environmentPostProcessor.postProcessEnvironment(environment, springApplication));
        }
    }

    /**
     * 生成解析用的 SpringApplication 实例。
     *
     * @param springApplicationBuilder SpringApplication 生成器
     * @param environment              解析用的 Environment 实例
     * @return 解析用的 SpringApplication 实例
     */
    @SuppressWarnings("unused")
    protected SpringApplication createSpringApplication(SpringApplicationBuilder springApplicationBuilder,
                                                        ConfigurableEnvironment environment) {
        SpringApplication springApplication = springApplicationBuilder.build(this.arguments);

        // 对 SpringApplication 实例进行处理
        if (this.springApplicationInitializer != null) {
            springApplication = this.springApplicationInitializer.apply(springApplication);
        }

        // 返回
        return springApplication;
    }

    /**
     * 生成解析用的 SpringApplication 生成器的实例。
     *
     * @param environment 解析用的 Environment 实例
     * @return 解析用的 SpringApplication 生成器的实例
     */
    protected SpringApplicationBuilder createSpringApplicationBuilder(ConfigurableEnvironment environment) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(EnvironmentResolver.class);

        // 默认设置
        springApplicationBuilder.environment(environment);
        springApplicationBuilder.bannerMode(Banner.Mode.OFF);
        springApplicationBuilder.web(WebApplicationType.NONE);
        springApplicationBuilder.registerShutdownHook(false);
        springApplicationBuilder.logStartupInfo(false);

        // 执行指定的初始化处理
        if (this.springApplicationBuilderInitializer != null) {
            springApplicationBuilder = this.springApplicationBuilderInitializer.apply(springApplicationBuilder);
        }

        // 返回
        return springApplicationBuilder;
    }

    /**
     * 生成解析用的 Environment 实例。
     *
     * @return 解析用的 Environment 实例
     */
    protected ConfigurableEnvironment createEnvironment() {
        // 新建一个 StandardEnvironment
        ConfigurableEnvironment environment = new StandardEnvironment();

        // 执行自定义的初始化操作
        if (this.environmentInitializer != null) {
            environment = this.environmentInitializer.apply(environment);
        }

        // 返回
        return environment;
    }

    public UnaryOperator<ConfigurableEnvironment> getEnvironmentInitializer() {
        return environmentInitializer;
    }

    public void setEnvironmentInitializer(UnaryOperator<ConfigurableEnvironment> environmentInitializer) {
        this.environmentInitializer = environmentInitializer;
    }

    public UnaryOperator<SpringApplicationBuilder> getSpringApplicationBuilderInitializer() {
        return springApplicationBuilderInitializer;
    }

    public void setSpringApplicationBuilderInitializer(
            UnaryOperator<SpringApplicationBuilder> springApplicationBuilderInitializer) {
        this.springApplicationBuilderInitializer = springApplicationBuilderInitializer;
    }

    public UnaryOperator<SpringApplication> getSpringApplicationInitializer() {
        return springApplicationInitializer;
    }

    public void setSpringApplicationInitializer(UnaryOperator<SpringApplication> springApplicationInitializer) {
        this.springApplicationInitializer = springApplicationInitializer;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public List<EnvironmentPostProcessor> getPreProcessors() {
        return preProcessors;
    }

    public List<EnvironmentPostProcessor> getPreProcessors(boolean createIfAbsent) {
        if (this.preProcessors == null && createIfAbsent) {
            this.preProcessors = new LinkedList<>();
        }
        return this.preProcessors;
    }

    public void setPreProcessors(List<EnvironmentPostProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    public List<EnvironmentPostProcessor> getPostProcessors() {
        return postProcessors;
    }

    public List<EnvironmentPostProcessor> getPostProcessors(boolean createIfAbsent) {
        if (this.postProcessors == null && createIfAbsent) {
            this.postProcessors = new LinkedList<>();
        }
        return this.postProcessors;
    }

    public void setPostProcessors(List<EnvironmentPostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }
}
