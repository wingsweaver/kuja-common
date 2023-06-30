package com.wingsweaver.kuja.common.boot;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * kuja-common-boot 的通用设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_KUJA_COMMON_BOOT)
public class CommonBootProperties extends AbstractPojo {
    /**
     * appinfo 相关设置。
     */
    private Map<String, String> app;
}
