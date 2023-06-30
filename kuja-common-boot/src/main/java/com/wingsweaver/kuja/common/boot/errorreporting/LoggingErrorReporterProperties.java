package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link LoggingErrorReporter} 相关设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.ErrorReporting.PREFIX_REPORTER_LOGGING)
public class LoggingErrorReporterProperties extends AbstractPojo {
    /**
     * 默认的 Logger 名称。
     */
    public static final String DEFAULT_LOGGER_NAME = "error";

    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * Logger 名称。
     */
    private String loggerName = DEFAULT_LOGGER_NAME;

    /**
     * Marker 名称。
     */
    private String markerName;

    /**
     * 日志级别。
     */
    private Level level = Level.ERROR;

    /**
     * 日志前缀。
     */
    private String prefix = "->>>- Begin of Error Report -----\n\n";

    /**
     * 日志后缀。
     */
    private String suffix = "-<<<- End of Error Report -----\n\n";
}
