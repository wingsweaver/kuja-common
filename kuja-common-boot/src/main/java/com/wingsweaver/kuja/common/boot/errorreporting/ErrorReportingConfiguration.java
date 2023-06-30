package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

/**
 * 异常处理功能的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ErrorReportingProperties.class, ErrorReportingCustomizerProperties.class,
        ErrorReportingReporterProperties.class, LoggingErrorReporterProperties.class})
@ConditionalOnProperty(prefix = KujaCommonBootKeys.ErrorReporting.PREFIX, name = "enabled",
        havingValue = "true", matchIfMissing = true)
public class ErrorReportingConfiguration extends AbstractConfiguration {
    @Bean
    public ReportingErrorHandler reportingErrorHandler(ErrorReportingProperties properties) {
        ReportingErrorHandler errorHandler = new ReportingErrorHandler();
        String reportExecutor = StringUtil.trimToEmpty(properties.getReportExecutor());
        if (!reportExecutor.isEmpty()) {
            errorHandler.setReportExecutor(this.getBean(reportExecutor, Executor.class, true));
        }
        errorHandler.setReporterInParallel(properties.isReporterInParallel());
        return errorHandler;
    }

    @Bean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.ErrorReporting.PREFIX_CUSTOMIZER_ERROR,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ErrorInfoErrorRecordCustomizer errorInfoErrorRecordCustomizer(ErrorReportingCustomizerProperties properties) {
        ErrorInfoErrorRecordCustomizer recordCustomizer = new ErrorInfoErrorRecordCustomizer();
        recordCustomizer.setSettings(properties.getError().getInclude());
        return recordCustomizer;
    }

    @Bean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.ErrorReporting.PREFIX_CUSTOMIZER_APP,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public AppInfoErrorRecordCustomizer appInfoErrorRecordCustomizer(ErrorReportingCustomizerProperties properties) {
        AppInfoErrorRecordCustomizer recordCustomizer = new AppInfoErrorRecordCustomizer();
        recordCustomizer.setSettings(properties.getApp().getInclude());
        return recordCustomizer;
    }

    @Bean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.ErrorReporting.PREFIX_CUSTOMIZER_THREAD,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public ThreadInfoErrorRecordCustomizer threadInfoErrorRecordCustomizer(ErrorReportingCustomizerProperties properties) {
        ThreadInfoErrorRecordCustomizer recordCustomizer = new ThreadInfoErrorRecordCustomizer();
        recordCustomizer.setSettings(properties.getThread().getInclude());
        return recordCustomizer;
    }

    @Bean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.ErrorReporting.PREFIX_REPORTER_LOGGING,
            name = "enabled", havingValue = "true", matchIfMissing = true)
    public LoggingErrorReporter loggingErrorReporter(LoggingErrorReporterProperties properties) {
        LoggingErrorReporter reporter = new LoggingErrorReporter();
        reporter.setLoggerName(properties.getLoggerName());
        reporter.setMarkerName(properties.getMarkerName());
        reporter.setLevel(properties.getLevel());
        reporter.setPrefix(properties.getPrefix());
        reporter.setSuffix(properties.getSuffix());
        return reporter;
    }
}
