package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlingDelegate;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.SpringBootExceptionReporter;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * kuja-common-boot 的异常上报器。
 *
 * @author wingsweaver
 */
@SuppressWarnings("PMD.GuardLogStatement")
public class KujaSpringBootExceptionReporter implements SpringBootExceptionReporter {
    private final ConfigurableApplicationContext applicationContext;

    public KujaSpringBootExceptionReporter(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean reportException(Throwable failure) {
        // 尝试按照 ReportingErrorHandler 的方式进行处理
        try {
            this.reportWithReportingErrorHandler(failure);
        } catch (Exception ignored) {
            this.reportWithDefault(failure);
        }
        return false;
    }

    /**
     * 默认的错误上报处理。
     *
     * @param failure 错误
     */
    private void reportWithDefault(Throwable failure) {
        String loggerName = LoggingErrorReporterProperties.DEFAULT_LOGGER_NAME;
        String markerName = null;
        try {
            ErrorReportingProperties properties = this.applicationContext.getBean(ErrorReportingProperties.class);
            LoggingErrorReporterProperties reporterProperties = properties.getReporter().getLogging();
            if (StringUtil.isNotEmpty(reporterProperties.getLoggerName())) {
                loggerName = reporterProperties.getLoggerName();
            }
            loggerName = reporterProperties.getLoggerName();
            if (StringUtil.isNotEmpty(reporterProperties.getMarkerName())) {
                markerName = reporterProperties.getMarkerName();
            }
        } catch (Exception ignored) {
            // 忽略错误
        }

        // 输出到日志文件中
        Logger logger = LoggerFactory.getLogger(loggerName);
        if (StringUtil.isNotEmpty(markerName)) {
            Marker marker = MarkerFactory.getMarker(markerName);
            logger.error(marker, failure.getMessage(), failure);
        } else {
            logger.error(failure.getMessage(), failure);
        }
    }

    /**
     * 使用 ReportingErrorHandler 的方式进行处理。
     *
     * @param failure 错误
     * @throws Exception 处理过程中发生错误
     */
    private void reportWithReportingErrorHandler(Throwable failure) throws Exception {
        ErrorHandlingDelegate errorHandlingDelegate = this.applicationContext.getBean(ErrorHandlingDelegate.class);
        ErrorHandlerContext context = errorHandlingDelegate.createErrorHandlerContext(BusinessContextHolder.getCurrent(),
                failure, null);

        ReportingErrorHandler errorHandler = this.applicationContext.getBean(ReportingErrorHandler.class);
        errorHandler.handle(context);
    }
}
