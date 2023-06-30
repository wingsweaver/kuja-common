package com.wingsweaver.kuja.common.boot.appinfo.properties;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Deployment 相关属性。<br>
 * 参照 {@link com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes.Deployment}。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.App.DEPLOYMENT)
public class DeploymentProperties extends AbstractPojo {
    /**
     * 发布环境。<br>
     * 参照 <a href="https://en.wikipedia.org/wiki/environment">发布环境</a>。
     */
    private String environment;
}
