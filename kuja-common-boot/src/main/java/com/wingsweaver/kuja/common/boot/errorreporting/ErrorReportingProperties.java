package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 错误上报功能的相关设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.ErrorReporting.PREFIX)
public class ErrorReportingProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * 错误上报的 Executor 实例的名称。<br>
     * 如果设置的话，使用该 Executor 实例来异步上报错误；
     * 如果没有设置的话，使用当前线程来同步上报错误。
     */
    private String reportExecutor;

    /**
     * 异步上报错误时，是否将每一个 ErrorReporter 调用提交为一个上报任务。<br>
     * 如果为 false 的话，那么所有的 ErrorReporter 都会在同一个上报任务中被调用。
     */
    private boolean reporterInParallel = false;

    /**
     * {@link ErrorRecordCustomizer} 相关设置。
     */
    @NestedConfigurationProperty
    private ErrorReportingCustomizerProperties customizer = new ErrorReportingCustomizerProperties();

    /**
     * {@link ErrorReporter} 相关设置。
     */
    @NestedConfigurationProperty
    private ErrorReportingReporterProperties reporter = new ErrorReportingReporterProperties();

}
