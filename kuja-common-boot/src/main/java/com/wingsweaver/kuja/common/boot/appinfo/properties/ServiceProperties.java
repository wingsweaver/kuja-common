package com.wingsweaver.kuja.common.boot.appinfo.properties;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Service 相关属性。<br>
 * 参照 {@link com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes.Service}。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.App.SERVICE)
public class ServiceProperties extends AbstractPojo {
    /**
     * 服务的（逻辑）名称。
     */
    private String name;

    /**
     * 服务的命名空间。
     */
    private String namespace;

    /**
     * 服务的所属群组。
     */
    private String group;

    /**
     * 服务的版本。
     */
    private String version;

    /**
     * 服务实例的信息。
     */
    @NestedConfigurationProperty
    private Instance instance;

    /**
     * 服务的权重。（自定义）
     */
    private Long weight;

    /**
     * 服务的蓝绿颜色。（自定义）
     */
    private String color;

    /**
     * 实例信息。
     */
    @Getter
    @Setter
    public static class Instance extends AbstractPojo {
        /**
         * 实例的 ID。
         */
        private String id;
    }
}
