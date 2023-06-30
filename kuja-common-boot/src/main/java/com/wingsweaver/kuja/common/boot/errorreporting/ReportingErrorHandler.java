package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandler;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.utils.exception.Extended;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 用于上报错误的 {@link ErrorHandler} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ReportingErrorHandler extends AbstractComponent implements ErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportingErrorHandler.class);

    public static final String KEY_ERROR_REPORTED = ClassUtil.resolveKey(ReportingErrorHandler.class, "error-reported");

    /**
     * ID 生成器。
     */
    private StringIdGenerator idGenerator;

    /**
     * 优先度。
     */
    private int order = KujaCommonBootOrders.REPORTING_ERROR_HANDLER;

    /**
     * ErrorRecordCustomizer 实例的列表。
     */
    private List<ErrorRecordCustomizer> customizers;

    /**
     * ErrorReporter 实例的列表。
     */
    private List<ErrorReporter> reporters;

    /**
     * 错误上报的 Executor 实例。<br>
     * 如果设置的话，使用该 Executor 实例来异步上报错误；
     * 如果没有设置的话，使用当前线程来同步上报错误。
     */
    private Executor reportExecutor;

    /**
     * 异步上报错误时，是否将每一个 ErrorReporter 调用提交为一个上报任务。<br>
     * 如果为 false 的话，那么所有的 ErrorReporter 都会在同一个上报任务中被调用。
     */
    private boolean reporterInParallel = false;

    @Override
    public boolean handle(ErrorHandlerContext context) {
        try {
            // 检查错误是否已经处理
            if (this.shouldReport(context)) {
                this.reportInternal(context);
                this.markReported(context);
            }
        } catch (Exception ignored) {
            // 忽略中间的任何错误
        }

        // 返回
        return false;
    }

    /**
     * 标记错误已经上报。
     *
     * @param context 错误处理上下文
     */
    protected void markReported(ErrorHandlerContext context) {
        Throwable error = context.getInputError();

        // 更新扩展类型的错误
        if (error instanceof Extended) {
            Extended<?> extended = (Extended<?>) error;
            extended.setExtendedAttribute(KEY_ERROR_REPORTED, true);
        }

        // 更新业务上下文
        BusinessContext businessContext = context.getBusinessContext();
        if (businessContext != null) {
            businessContext.setAttribute(KEY_ERROR_REPORTED, error);
        }
    }

    /**
     * 上报错误。
     *
     * @param context 错误处理上下文
     */
    protected void reportInternal(ErrorHandlerContext context) {
        ErrorRecord errorRecord = new ErrorRecord();
        errorRecord.setId(this.idGenerator.nextId());
        errorRecord.setError(context.getInputError());

        // 更新 errorRecord
        for (ErrorRecordCustomizer customizer : this.customizers) {
            try {
                errorRecord = customizer.customize(context, errorRecord);
            } catch (Exception ex) {
                // 如果发生错误，那么只输出日志，继续往下处理（尽可能地捕捉错误）
                LogUtil.warn(LOGGER, ex, "ErrorRecordCustomizer {} failed to customize error record.", customizer);
            }
        }

        // 上报 errorRecord
        this.doErrorReport(errorRecord);
    }

    /**
     * 上报错误。
     *
     * @param errorRecord 错误记录
     */
    protected void doErrorReport(ErrorRecord errorRecord) {
        if (this.reportExecutor != null) {
            // 使用 reportExecutor 异步上报错误
            if (this.reporterInParallel) {
                // 将每个 reporter 都提交为一个上报任务
                for (ErrorReporter reporter : this.reporters) {
                    this.reportExecutor.execute(() -> this.singleErrorReport(errorRecord, reporter));
                }
            } else {
                // 在 reportExecutor 中上报本错误
                this.reportExecutor.execute(() -> this.batchErrorReport(errorRecord, this.reporters));
            }
        } else {
            // 在本线程中同步上报错误
            this.batchErrorReport(errorRecord, this.reporters);
        }
    }

    /**
     * 使用指定的单个 ErrorReporter 上报指定错误。
     *
     * @param errorRecord 错误记录
     * @param reporter    ErrorReporter 实例
     */
    protected void singleErrorReport(ErrorRecord errorRecord, ErrorReporter reporter) {
        try {
            LogUtil.trace(LOGGER, "Reporting error record {} with reporter {} in parallel.", errorRecord, reporter);
            reporter.report(errorRecord);
        } catch (Exception ex) {
            // 如果发生错误，那么只输出日志，继续往下处理（尽可能地捕捉错误）
            LogUtil.warn(LOGGER, ex, "ErrorReporter {} failed to report error record {} in parallel.", reporter, errorRecord);
        }
    }

    /**
     * 使用指定的一组 ErrorReporter 实例列表上报指定错误。
     *
     * @param errorRecord 错误记录
     * @param reporters   ErrorReporter 实例的列表
     */
    protected void batchErrorReport(ErrorRecord errorRecord, Collection<ErrorReporter> reporters) {
        for (ErrorReporter reporter : reporters) {
            try {
                LogUtil.trace(LOGGER, "Reporting error record {} with reporter {} in batch.", errorRecord, reporter);
                reporter.report(errorRecord);
            } catch (Exception ex) {
                // 如果发生错误，那么只输出日志，继续往下处理（尽可能地捕捉错误）
                LogUtil.warn(LOGGER, ex, "ErrorReporter {} failed to report error record {} in batch.", reporter, errorRecord);
            }
        }
    }

    /**
     * 检查错误是否应该上报（尚未上报）。
     *
     * @param context 错误处理上下文
     * @return 如果应该上报则返回 true，否则返回 false
     */
    @SuppressWarnings("RedundantIfStatement")
    protected boolean shouldReport(ErrorHandlerContext context) {
        Throwable error = context.getOutputError();

        // 检查是否是扩展类型的错误
        if (error instanceof Extended) {
            Extended<?> extended = (Extended<?>) error;
            if (extended.getExtendedAttribute(KEY_ERROR_REPORTED) != null) {
                return false;
            }
        }

        // 检查业务上下文
        BusinessContext businessContext = context.getBusinessContext();
        if (businessContext != null && businessContext.getAttribute(KEY_ERROR_REPORTED) == error) {
            return false;
        }

        // 默认返回 true
        return true;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 idGenerator
        this.initIdGenerator();

        // 初始化 customizers
        this.initCustomizers();

        // 初始化 reporters
        this.initReporters();
    }

    /**
     * 初始化 idGenerator。
     */
    protected void initIdGenerator() {
        if (this.idGenerator == null) {
            this.idGenerator = this.getBean(StringIdGenerator.class, () -> StringIdGenerator.FALLBACK);
        }
    }

    /**
     * 初始化 customizers。
     */
    protected void initCustomizers() {
        if (this.customizers == null) {
            this.customizers = this.getBeansOrdered(ErrorRecordCustomizer.class);
        }
    }

    /**
     * 初始化 reporters。
     */
    protected void initReporters() {
        if (this.reporters == null) {
            this.reporters = this.getBeansOrdered(ErrorReporter.class);
        }
    }
}
