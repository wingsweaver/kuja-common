package com.wingsweaver.kuja.common.messaging.autoconfigurer.errorreporting;

import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * kuja-common-messaging 中错误上报相关的配置属性。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingErrorReportingKeys.PREFIX)
public class MessageErrorReportingProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = false;

    /**
     * 默认的目的地。
     */
    private String defaultDestination;
}
