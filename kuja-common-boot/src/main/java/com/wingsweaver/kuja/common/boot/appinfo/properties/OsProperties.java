package com.wingsweaver.kuja.common.boot.appinfo.properties;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OS 相关属性。<br>
 * 参照 {@link com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes.OS}。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.App.OS)
public class OsProperties extends AbstractPojo {
    /**
     * 操作系统的类型。
     */
    private String type;

    /**
     * 操作系统的描述。
     */
    private String description;

    /**
     * 操作系统的名称。
     */
    private String name;

    /**
     * 操作系统的版本。
     */
    private String version;
}
