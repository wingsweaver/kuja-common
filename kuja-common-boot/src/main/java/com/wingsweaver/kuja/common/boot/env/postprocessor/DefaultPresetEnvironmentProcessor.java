package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 默认的 {@link PresetEnvironmentProcessor} 实现类。
 *
 * @author wingsweaver
 */
public class DefaultPresetEnvironmentProcessor extends AbstractEnvironmentProcessor implements PresetEnvironmentProcessor {
    /**
     * kuja.boot.env.preset 属性的键。
     */
    public static final String KEY_KUJA_BOOT_ENV_PRESET = "kuja.boot.env.preset";

    /**
     * kuja.boot.env.preset 属性的值。
     */
    public static final String VALUE_KUJA_BOOT_ENV_PRESET = "classpath:/kuja-env-preset.properties";

    @Override
    public void process(ConfigurableEnvironment environment, SpringApplication application) {
        String propertyValue = environment.getProperty(KEY_KUJA_BOOT_ENV_PRESET, VALUE_KUJA_BOOT_ENV_PRESET).trim();
        this.processInternal(environment, application, propertyValue);
    }

    @Override
    protected String getPropertySourceName() {
        return KujaCommonBootKeys.PropertySourceNames.PRESET;
    }
}
