package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.utils.model.context.ContextAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;

/**
 * {@link ErrorHandlerContext} 的访问器。
 *
 * @author wingsweaver
 */
public class ErrorHandlerContextAccessor extends ContextAccessor {
    public ErrorHandlerContextAccessor(ErrorHandlerContext errorHandlerContext) {
        super(errorHandlerContext);
    }

    public static final String KEY_THREAD = ClassUtil.resolveKey(Thread.class);

    public Thread getThread() {
        return getAttribute(KEY_THREAD);
    }

    public void setThread(Thread thread) {
        setAttribute(KEY_THREAD, thread);
    }
}
