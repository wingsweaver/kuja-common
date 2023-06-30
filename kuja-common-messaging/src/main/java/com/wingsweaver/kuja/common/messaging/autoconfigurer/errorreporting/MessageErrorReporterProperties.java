package com.wingsweaver.kuja.common.messaging.autoconfigurer.errorreporting;

import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link com.wingsweaver.kuja.common.messaging.errorreporting.common.MessageErrorReporter} 的配置属性。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingErrorReportingKeys.PREFIX_MESSAGE_ERROR_REPORTER)
public class MessageErrorReporterProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = false;
}
