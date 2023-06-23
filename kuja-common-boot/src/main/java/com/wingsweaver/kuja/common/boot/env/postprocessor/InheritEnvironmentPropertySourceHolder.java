package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.env.PropertySourceUtil;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.matcher.StringMatcher;
import com.wingsweaver.kuja.common.utils.support.matcher.string.RegexBlackWhiteMatcher;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析和存储继承 PropertySource 的辅助工具类。
 *
 * @author wingsweaver
 */
final class InheritEnvironmentPropertySourceHolder implements ApplicationListener<ApplicationContextInitializedEvent> {
    private static PropertySource<?> inheritEnvironmentPropertySource;

    public static PropertySource<?> getInheritEnvironmentPropertySource() {
        return inheritEnvironmentPropertySource;
    }

    public static void setInheritEnvironmentPropertySource(PropertySource<?> propertySource) {
        inheritEnvironmentPropertySource = propertySource;
    }

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        try {
            PropertySource<?> propertySource = this.resolveInheritEnvironmentPropertySource(event.getApplicationContext());
            if (propertySource != null) {
                setInheritEnvironmentPropertySource(propertySource);
            }
        } catch (Exception ignored) {
            // 忽略此处的错误
        }
    }

    /**
     * 计算继承属性的 PropertySource。
     *
     * @param applicationContext 应用上下文
     * @return 继承属性的 PropertySource
     */
    private PropertySource<?> resolveInheritEnvironmentPropertySource(ConfigurableApplicationContext applicationContext) {
        // 获取继承属性的设置
        InheritEnvironmentSettings settings = Binder.get(applicationContext.getEnvironment())
                .bind(KujaCommonBootKeys.PREFIX_INHERIT_ENVIRONMENT_SETTINGS, InheritEnvironmentSettings.class)
                .get();
        if (!settings.isEnabled()) {
            return null;
        }

        // 提取需要继承的数据
        RegexBlackWhiteMatcher envNameMatcher = new RegexBlackWhiteMatcher(true);
        envNameMatcher.setDefaultResult(true);
        envNameMatcher.getWhiteMatcher().addStrings(settings.getIncludes());
        envNameMatcher.getBlackMatcher().addStrings(settings.getExcludes());
        Map<String, Object> inheritMap = this.resolveInheritMap(applicationContext.getEnvironment(), envNameMatcher);

        // 返回
        return new MapPropertySource(KujaCommonBootKeys.PropertySourceNames.INHERITED, inheritMap);
    }

    /**
     * 计算继承的属性的字典。
     *
     * @param environment    应用的 environment 实例
     * @param envNameMatcher 用于判定继承的 BlackWhiteListChecker
     * @return 继承的属性的字典
     */
    private Map<String, Object> resolveInheritMap(ConfigurableEnvironment environment, StringMatcher envNameMatcher) {

        // 将 ConfigurableEnvironment 铺开成 Map
        Map<String, Object> fullMap = new HashMap<>(BufferSizes.MEDIUM);
        PropertySourceUtil.copyTo(environment.getPropertySources(), fullMap, true);

        // 提取其中有效的部分
        int size = MapUtil.hashInitCapacity(fullMap.size() + 1, MapUtil.FULL_LOAD_FACTOR);
        Map<String, Object> inheritMap = new HashMap<>(size);
        for (Map.Entry<String, Object> entry : fullMap.entrySet()) {
            String name = entry.getKey();

            // 检查是否应该继承该属性
            if (!envNameMatcher.matches(name)) {
                continue;
            }

            // 如果需要继承的话，那么加入到字典中
            Object value = entry.getValue();
            if (value instanceof OriginTrackedValue) {
                value = ((OriginTrackedValue) value).getValue();
            }
            if (value instanceof CharSequence) {
                value = environment.resolvePlaceholders(value.toString());
            }
            inheritMap.put(name, value);
        }

        // 返回
        return inheritMap;
    }
}
