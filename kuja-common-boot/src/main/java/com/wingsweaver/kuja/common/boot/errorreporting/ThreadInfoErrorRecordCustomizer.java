package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContextAccessor;
import com.wingsweaver.kuja.common.boot.include.IncludeChecker;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.exception.Extended;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于提取错误对应的线程信息的 {@link ErrorRecordCustomizer} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ThreadInfoErrorRecordCustomizer extends AbstractErrorRecordCustomizer {
    public static final String KEY = "threadInfo";

    public static final String KEY_THREAD = "thread";

    public static final String KEY_NAME = "name";

    public static final String KEY_ID = "id";

    public static final String KEY_PRIORITY = "priority";

    public static final String KEY_DAEMON = "daemon";

    public static final String KEY_GROUP = "group";

    public static final String KEY_GROUP_NAME = "groupName";

    @Override
    public ErrorRecord customize(ErrorHandlerContext context, ErrorRecord record) {
        Map<String, Object> threadInfo = this.resolveThreadInfo(context, context.getInputError());
        record.setTagValue(KEY, threadInfo);
        return record;
    }

    /**
     * 提取错误信息。
     *
     * @param context 错误处理上下文
     * @param error   错误
     * @return 错误信息
     */
    @SuppressWarnings("PMD.ReturnEmptyCollectionRatherThanNull")
    protected Map<String, Object> resolveThreadInfo(ErrorHandlerContext context, Throwable error) {
        // 推导对应的线程信息
        Thread thread = this.resolveThread(context, error);
        if (thread == null) {
            return null;
        }

        // 导出线程信息
        Map<String, Object> map = new HashMap<>(BufferSizes.TINY);
        this.exportThreadInfo(thread, map);

        // 返回
        return map;
    }

    /**
     * 导出线程信息。
     *
     * @param thread 线程
     * @param map    线程信息
     */
    protected void exportThreadInfo(Thread thread, Map<String, Object> map) {
        IncludeChecker includeChecker = this.createIncludeChecker(BusinessContextHolder.getCurrent(), null);

        // thread
        if (includeChecker.includes(KEY_THREAD)) {
            map.put(KEY_THREAD, thread.toString());
        }

        // thread name
        if (includeChecker.includes(KEY_NAME)) {
            map.put(KEY_NAME, thread.getName());
        }

        // thread id
        if (includeChecker.includes(KEY_ID)) {
            map.put(KEY_ID, thread.getId());
        }

        // thread priority
        if (includeChecker.includes(KEY_PRIORITY)) {
            map.put(KEY_PRIORITY, thread.getPriority());
        }

        // is daemon
        if (includeChecker.includes(KEY_DAEMON)) {
            map.put(KEY_DAEMON, thread.isDaemon());
        }

        // thread group 信息
        try {
            ThreadGroup group = thread.getThreadGroup();
            if (group != null) {
                // thread group
                if (includeChecker.includes(KEY_GROUP)) {
                    map.put(KEY_GROUP, group.toString());
                }

                // thread group name
                if (includeChecker.includes(KEY_GROUP_NAME)) {
                    map.put(KEY_GROUP_NAME, group.getName());
                }
            }
        } catch (Exception ignored) {
            // 忽略此错误
        }
    }

    /**
     * 推导错误对应的线程。
     *
     * @param context 错误处理上下文
     * @param error   错误
     * @return 线程
     */
    protected Thread resolveThread(ErrorHandlerContext context, Throwable error) {
        Thread thread = null;
        if (error instanceof Extended) {
            thread = ((Extended<?>) error).getExtendedAttribute(ClassUtil.resolveKey(Thread.class));
        }
        if (thread == null) {
            ErrorHandlerContextAccessor contextAccessor = new ErrorHandlerContextAccessor(context);
            thread = contextAccessor.getThread();
        }
        return thread != null ? thread : Thread.currentThread();
    }
}
