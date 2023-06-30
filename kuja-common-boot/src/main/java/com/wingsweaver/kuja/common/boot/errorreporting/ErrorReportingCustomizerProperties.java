package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * {@link ErrorRecordCustomizer} 相关设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.ErrorReporting.PREFIX_CUSTOMIZER)
public class ErrorReportingCustomizerProperties extends AbstractPojo {
    /**
     * {@link AppInfoErrorRecordCustomizer} 设置。
     */
    @NestedConfigurationProperty
    private CommonErrorRecordCustomizerProperties app = new CommonErrorRecordCustomizerProperties();

    /**
     * {@link ErrorInfoErrorRecordCustomizer} 设置。
     */
    @NestedConfigurationProperty
    private CommonErrorRecordCustomizerProperties error = new CommonErrorRecordCustomizerProperties();

    /**
     * {@link ThreadInfoErrorRecordCustomizer} 设置。
     */
    @NestedConfigurationProperty
    private CommonErrorRecordCustomizerProperties thread = new CommonErrorRecordCustomizerProperties();
}
