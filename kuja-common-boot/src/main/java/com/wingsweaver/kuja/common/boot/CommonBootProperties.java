package com.wingsweaver.kuja.common.boot;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * kuja-common-boot 的通用设置。
 *
 * @author wingsweaver
 */
@Data
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_KUJA_COMMON_BOOT)
public class CommonBootProperties {
    /**
     * appinfo 相关设置。
     */
    private Map<String, String> app;
}
