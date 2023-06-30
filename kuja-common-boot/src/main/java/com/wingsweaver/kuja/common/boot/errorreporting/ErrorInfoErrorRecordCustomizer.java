package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.include.IncludeChecker;
import com.wingsweaver.kuja.common.utils.exception.ErrorInfoExportUtil;

import java.util.Map;

/**
 * 用于提取错误信息的 {@link ErrorRecordCustomizer} 实现类。
 *
 * @author wingsweaver
 */
public class ErrorInfoErrorRecordCustomizer extends AbstractErrorRecordCustomizer {
    public static final String KEY = "errorInfo";

    @Override
    public ErrorRecord customize(ErrorHandlerContext context, ErrorRecord record) {
        Map<String, Object> errorInfo = this.resolveErrorInfo(context, context.getInputError());
        record.setTagValue(KEY, errorInfo);
        return record;
    }

    /**
     * 提取错误信息。
     *
     * @param context 错误处理上下文
     * @param error   错误
     * @return 错误信息
     */
    protected Map<String, Object> resolveErrorInfo(ErrorHandlerContext context, Throwable error) {
        // 尝试直接导出错误信息
        IncludeChecker includeChecker = this.createIncludeChecker(context.getBusinessContext(), null);
        return ErrorInfoExportUtil.export(error, includeChecker::includes);
    }
}
