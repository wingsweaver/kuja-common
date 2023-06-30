package com.wingsweaver.kuja.common.boot.appinfo.properties;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Host 相关属性。<br>
 * 参照 {@link com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes.Host}。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.App.HOST)
public class HostProperties extends AbstractPojo {
    /**
     * 主机的唯一标识。<br>
     * 在云环境中，必须使用云服务商分配给主机的实例 ID 作为唯一标识。<br>
     * 在其他环境中，应该使用主机 ID。
     */
    private String id;

    /**
     * 主机的名称。
     */
    private String name;

    /**
     * 主机的 IP 地址。
     */
    private String address;

    /**
     * 主机的规格型号。
     */
    private String type;

    /**
     * 主机的架构。
     */
    private String arch;

    /**
     * 处理器的信息。
     */
    @NestedConfigurationProperty
    private Processor processor;

    /**
     * 主机的镜像信息。
     */
    @NestedConfigurationProperty
    private Image image;

    /**
     * 处理器信息。
     */
    @Getter
    @Setter
    public static class Processor extends AbstractPojo {
        /**
         * 处理器的架构。
         */
        private String arch;

        /**
         * 处理器的类型。
         */
        private String type;
    }

    /**
     * 镜像信息。
     */
    @Getter
    @Setter
    public static class Image extends AbstractPojo {
        /**
         * 镜像的名称。
         */
        private String name;

        /**
         * 镜像的 ID。
         */
        private String id;
    }
}
