package com.wingsweaver.kuja.common.messaging.autoconfigurer.errorreporting;

import com.wingsweaver.kuja.common.boot.include.IncludeAttribute;
import com.wingsweaver.kuja.common.boot.include.IncludeSettings;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * {@link com.wingsweaver.kuja.common.messaging.errorreporting.send.ErrorRecordPayloadResolver} 的设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonMessagingErrorReportingKeys.PREFIX_ERROR_RECORD_PAYLOAD_RESOLVER)
public class ErrorRecordPayloadResolverProperties extends AbstractPojo {
    /**
     * 错误信息的导出设置。
     */
    @NestedConfigurationProperty
    private IncludeSettings errorInfo = new IncludeSettings(IncludeAttribute.ALWAYS);

    /**
     * 错误附加信息的导出设置。
     */
    @NestedConfigurationProperty
    private IncludeSettings errorTags = new IncludeSettings(IncludeAttribute.ALWAYS);
}
