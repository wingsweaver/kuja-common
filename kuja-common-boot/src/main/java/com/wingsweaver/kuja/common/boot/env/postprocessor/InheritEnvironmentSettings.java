package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 继承的属性的设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_INHERIT_ENVIRONMENT_SETTINGS)
public class InheritEnvironmentSettings {
    private static final String[] DEFAULT_INCLUDES = new String[]{
            "spring.application.[a-zA-Z0-9\\-_]*", "spring.cloud.(.)*", "-spring.cloud.client.(.)*"
    };

    private static final String[] DEFAULT_EXCLUDES = new String[]{
            "spring.cloud.client.(.)*"
    };

    /**
     * 是否启用。
     */
    private boolean enabled = false;

    /**
     * 需要继承复制到配置更新处理中的属性的名称的数组。
     */
    private String[] includes = DEFAULT_INCLUDES;

    /**
     * 不需要继承复制到配置更新处理中的属性的名称的数组。
     */
    private String[] excludes = DEFAULT_EXCLUDES;
}
