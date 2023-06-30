package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 将错误记录到日志中的 {@link ErrorReporter} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@SuppressWarnings("PMD.GuardLogStatement")
public class LoggingErrorReporter extends AbstractComponent implements ErrorReporter {
    /**
     * Logger 名称。
     */
    private String loggerName = LoggingErrorReporterProperties.DEFAULT_LOGGER_NAME;

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
    private String prefix;

    /**
     * 日志后缀。
     */
    private String suffix;

    /**
     * Logger 实例。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Logger logger;

    /**
     * Marker 实例。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Marker marker;

    /**
     * LogHelper 实例。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private LogHelper logHelper;

    @Override
    public void report(ErrorRecord record) {
        // 检查是否可以输出
        if (!this.logHelper.canLog(this.logger, this.marker)) {
            return;
        }

        // 输出前缀
        this.reportPrefix(record);

        // 输出错误信息
        this.reportError(record.getError());

        // 输出附加数据
        this.reportTags(record.getTags());

        // 输出临时数据
        this.reportTemps(record.getTemps(false));

        // 输出后缀
        this.reportSuffix(record);
    }

    /**
     * 输出错误信息的后缀。
     *
     * @param record 错误记录
     */
    protected void reportSuffix(ErrorRecord record) {
        if (StringUtil.isNotEmpty(this.suffix)) {
            this.logHelper.log(this.logger, this.marker, suffix);
        }
    }

    /**
     * 输出错误信息的前缀。
     *
     * @param record 错误记录
     */
    protected void reportPrefix(ErrorRecord record) {
        if (StringUtil.isNotEmpty(this.prefix)) {
            this.logHelper.log(this.logger, this.marker, prefix);
        }
    }

    /**
     * 输出临时数据。
     *
     * @param temps 临时数据
     */
    protected void reportTemps(Map<String, Object> temps) {
        if (CollectionUtils.isEmpty(temps)) {
            return;
        }

        logHelper.log(this.logger, this.marker, "Temps: ");
        temps.forEach((key, value) -> {
            logHelper.log(this.logger, this.marker, "\t{} = {}", key, value);
        });
    }

    /**
     * 输出附加数据。
     *
     * @param tags 附加数据
     */
    protected void reportTags(Map<String, Object> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return;
        }

        this.logHelper.log(this.logger, this.marker, "Tags: ");
        tags.forEach((key, value) -> {
            logHelper.log(this.logger, this.marker, "\t{} = {}", key, value);
        });
    }

    /**
     * 输出错误信息。
     *
     * @param error 错误
     */
    private void reportError(Throwable error) {
        this.logHelper.log(this.logger, this.marker, error);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 检查 level 是否有效
        AssertState.Named.notNull("level", this.level);

        // 初始化 Logger
        this.initLogger();

        // 初始化 Marker
        this.initMarker();

        // 初始化 LogHelper
        this.initLogHelper();
    }

    /**
     * 初始化 LogHelper。
     */
    protected void initLogHelper() {
        this.logHelper = TRACE;
        switch (this.level) {
            case DEBUG:
                logHelper = DEBUG;
                break;
            case INFO:
                logHelper = INFO;
                break;
            case WARN:
                logHelper = WARN;
                break;
            case ERROR:
                logHelper = ERROR;
                break;
            default:
                break;
        }
    }

    /**
     * 初始化 Marker。
     */
    protected void initMarker() {
        if (StringUtil.isNotEmpty(this.markerName)) {
            this.marker = MarkerFactory.getMarker(this.markerName);
        }
    }

    /**
     * 初始化 Logger。
     */
    protected void initLogger() {
        if (StringUtil.isEmpty(this.loggerName)) {
            this.logger = LoggerFactory.getLogger(LoggingErrorReporterProperties.DEFAULT_LOGGER_NAME);
        } else {
            this.logger = LoggerFactory.getLogger(this.loggerName);
        }
    }

    /**
     * 日志输出辅助接口。
     */
    public interface LogHelper {
        /**
         * 是否可以输出日志。
         *
         * @param logger Logger 实例
         * @param marker Marker 实例
         * @return 是否可以输出日志
         */
        boolean canLog(Logger logger, Marker marker);

        /**
         * 输出错误信息。
         *
         * @param logger Logger 实例
         * @param marker Marker 实例
         * @param error  错误信息
         */
        void log(Logger logger, Marker marker, Throwable error);

        /**
         * 输出指定信息。
         *
         * @param logger Logger 实例
         * @param marker Marker 实例
         * @param format 格式
         * @param args   参数
         */
        void log(Logger logger, Marker marker, String format, Object... args);
    }

    /**
     * {@link Level#TRACE} 对应的 {@link LogHelper} 实现。
     */
    public static final LogHelper TRACE = new LogHelper() {
        @Override
        public boolean canLog(Logger logger, Marker marker) {
            return logger.isTraceEnabled(marker);
        }

        @Override
        public void log(Logger logger, Marker marker, Throwable error) {
            logger.trace(marker, error.getMessage(), error);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... args) {
            logger.trace(marker, format, args);
        }
    };

    /**
     * {@link Level#DEBUG} 对应的 {@link LogHelper} 实现。
     */
    public static final LogHelper DEBUG = new LogHelper() {
        @Override
        public boolean canLog(Logger logger, Marker marker) {
            return logger.isDebugEnabled(marker);
        }

        @Override
        public void log(Logger logger, Marker marker, Throwable error) {
            logger.debug(marker, error.getMessage(), error);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... args) {
            logger.debug(marker, format, args);
        }
    };

    /**
     * {@link Level#INFO} 对应的 {@link LogHelper} 实现。
     */
    public static final LogHelper INFO = new LogHelper() {
        @Override
        public boolean canLog(Logger logger, Marker marker) {
            return logger.isInfoEnabled(marker);
        }

        @Override
        public void log(Logger logger, Marker marker, Throwable error) {
            logger.info(marker, error.getMessage(), error);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... args) {
            logger.info(marker, format, args);
        }
    };

    /**
     * {@link Level#WARN} 对应的 {@link LogHelper} 实现。
     */
    public static final LogHelper WARN = new LogHelper() {
        @Override
        public boolean canLog(Logger logger, Marker marker) {
            return logger.isWarnEnabled(marker);
        }

        @Override
        public void log(Logger logger, Marker marker, Throwable error) {
            logger.warn(marker, error.getMessage(), error);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... args) {
            logger.warn(marker, format, args);
        }
    };

    /**
     * {@link Level#ERROR} 对应的 {@link LogHelper} 实现。
     */
    public static final LogHelper ERROR = new LogHelper() {
        @Override
        public boolean canLog(Logger logger, Marker marker) {
            return logger.isErrorEnabled(marker);
        }

        @Override
        public void log(Logger logger, Marker marker, Throwable error) {
            logger.error(marker, error.getMessage(), error);
        }

        @Override
        public void log(Logger logger, Marker marker, String format, Object... args) {
            logger.error(marker, format, args);
        }
    };
}
