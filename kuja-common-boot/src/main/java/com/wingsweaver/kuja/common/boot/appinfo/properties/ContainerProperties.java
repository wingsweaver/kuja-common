package com.wingsweaver.kuja.common.boot.appinfo.properties;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Container 相关属性。<br>
 * 参照 {@link com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes.Container}。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.App.CONTAINER)
public class ContainerProperties extends AbstractPojo {
    /**
     * 容器名称。
     */
    private String name;

    /**
     * 容器 ID。
     */
    private String id;

    /**
     * 容器运行时。
     */
    private String runtime;

    /**
     * 容器镜像的信息。
     */
    @NestedConfigurationProperty
    private Image image;

    /**
     * 容器镜像的信息。
     */
    @Getter
    @Setter
    public static class Image extends AbstractPojo {
        /**
         * 容器镜像的名称。
         */
        private String name;

        /**
         * 容器镜像的版本。
         */
        private String tag;
    }
}
