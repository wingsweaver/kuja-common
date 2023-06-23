package com.wingsweaver.kuja.common.web.common;

import com.wingsweaver.kuja.common.web.constants.KujaCommonWebKeys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 服务器设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonWebKeys.PREFIX_SERVER_PROPERTIES)
public class WebServerProperties {
    /**
     * 是否大小写敏感。
     */
    private Boolean caseSensitive;

    /**
     * 是否删去尾部的 /。
     */
    private Boolean trailingSlash;
}
