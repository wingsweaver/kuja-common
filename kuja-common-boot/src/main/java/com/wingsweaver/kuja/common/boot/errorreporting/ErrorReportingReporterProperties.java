package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * {@link ErrorReporter} 相关设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.ErrorReporting.PREFIX_REPORTER)
public class ErrorReportingReporterProperties extends AbstractPojo {
    /**
     * {@link LoggingErrorReporter} 设置。
     */
    @NestedConfigurationProperty
    private LoggingErrorReporterProperties logging = new LoggingErrorReporterProperties();
}
