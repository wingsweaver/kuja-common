package com.wingsweaver.kuja.common.boot.appinfo.properties;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Device 相关属性。<br>
 * 参照 {@link com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes.Device}。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.App.DEVICE)
public class DeviceProperties extends AbstractPojo {
    /**
     * 设备的唯一标识。
     */
    private String id;

    /**
     * 设备制造商的名称。
     */
    private String manufacturer;

    /**
     * 设备机型的信息。
     */
    @NestedConfigurationProperty
    private Model model;

    /**
     * 设备机型的信息。
     */
    @Getter
    @Setter
    public static class Model extends AbstractPojo {
        /**
         * 设备机型的标识。
         */
        private String identifier;

        /**
         * 设备机型的名称。
         */
        private String name;
    }
}
