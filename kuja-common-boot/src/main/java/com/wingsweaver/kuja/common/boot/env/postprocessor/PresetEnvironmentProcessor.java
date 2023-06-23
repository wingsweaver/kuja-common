package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 预设环境设置的接口定义。
 *
 * @author wingsweaver
 */
public interface PresetEnvironmentProcessor extends DefaultOrdered {
    /**
     * 设置预设环境参数。
     *
     * @param environment 环境对象
     * @param application 应用对象
     */
    void process(ConfigurableEnvironment environment, SpringApplication application);
}
