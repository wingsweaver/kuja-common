package com.wingsweaver.kuja.common.boot.appinfo.properties;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Cloud 相关属性。<br>
 * 参照 {@link com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes.Cloud}。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.App.CLOUD)
public class CloudProperties extends AbstractPojo {
    /**
     * 云服务商的名称。
     */
    private String provider;

    /**
     * 账户信息。
     */
    @NestedConfigurationProperty
    private Account account;

    /**
     * 云服务所在的区域。
     */
    private String region;

    /**
     * 云资源的 ID。
     */
    private String resourceId;

    /**
     * 云服务所在的可用区。
     */
    private String availabilityZone;

    /**
     * 使用的云平台。<br>
     * 取值可以参照 {@link com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes.Cloud.Platforms}。
     */
    private String platform;

    /**
     * 账户信息。
     */
    @Getter
    @Setter
    public static class Account extends AbstractPojo {
        /**
         * 账户 ID。
         */
        private String id;
    }
}
