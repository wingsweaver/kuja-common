package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.include.IncludeSettings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 返回值功能的相关设置。
 *
 * @author wingsweaver
 */
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_RETURN_VALUE_PROPERTIES)
@Getter
@Setter
public class ReturnValueProperties {
    /**
     * 是否启用返回值功能。
     */
    private boolean enabled = true;

    /**
     * 默认的成功返回值。
     */
    @NestedConfigurationProperty
    private ReturnValue success = new ReturnValue();

    /**
     * 默认的失败返回值。<br>
     * 默认情况下，不要设置 message，这样可以使用错误中的 message。
     */
    @NestedConfigurationProperty
    private ReturnValue fail = new ReturnValue();

    /**
     * 错误相关设置。
     */
    @NestedConfigurationProperty
    private IncludeSettings error = new IncludeSettings();

    /**
     * 处理者的 App 信息相关。
     */
    @NestedConfigurationProperty
    private IncludeSettings appInfo = new IncludeSettings();

}
