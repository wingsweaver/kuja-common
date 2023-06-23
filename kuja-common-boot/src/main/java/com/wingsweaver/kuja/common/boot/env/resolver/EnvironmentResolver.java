package com.wingsweaver.kuja.common.boot.env.resolver;

import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 解析当前 Environment 的接口。
 *
 * @author wingsweaver
 */
public interface EnvironmentResolver {
    /**
     * 解析当前应用上下文中的 Environment 设置。<br>
     * 模拟了 ContextRefresher 中的刷新动作。
     *
     * @return Environment 设置
     */
    @SuppressWarnings("unused")
    ConfigurableEnvironment resolve();
}
