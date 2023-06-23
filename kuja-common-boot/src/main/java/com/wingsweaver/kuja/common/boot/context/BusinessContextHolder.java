package com.wingsweaver.kuja.common.boot.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.NonThrowableCloseable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务上下文的持有者。
 *
 * @author wingsweaver
 */
public final class BusinessContextHolder {
    private BusinessContextHolder() {
        // 禁止实例化
    }

    /**
     * 本线程持有的业务上下文。
     */
    private static final ThreadLocal<BusinessContext> CURRENT = new ThreadLocal<>();

    /**
     * 继承父线程而来的业务上下文。
     */
    private static final ThreadLocal<BusinessContext> INHERITABLE = new TransmittableThreadLocal<>();

    /**
     * 获取当前的业务上下文。
     *
     * @return 当前的业务上下文
     */
    public static BusinessContext getCurrent() {
        return getCurrent(false);
    }

    /**
     * 获取当前的业务上下文。
     *
     * @param includeInheritable 是否获取继承父线程而来的业务上下文
     * @return 当前的业务上下文
     */
    public static BusinessContext getCurrent(boolean includeInheritable) {
        BusinessContext businessContext = CURRENT.get();
        if (businessContext == null && includeInheritable) {
            businessContext = INHERITABLE.get();
        }
        return businessContext;
    }

    /**
     * 设置当前的业务上下文。
     *
     * @param businessContext 当前的业务上下文
     */
    public static void setCurrent(BusinessContext businessContext) {
        setCurrent(businessContext, false);
    }

    /**
     * 设置当前的业务上下文。
     *
     * @param businessContext 当前的业务上下文
     * @param inheritable     是否设置继承父线程而来的业务上下文
     */
    public static void setCurrent(BusinessContext businessContext, boolean inheritable) {
        if (businessContext == null) {
            removeCurrent();
            return;
        }

        if (inheritable) {
            CURRENT.remove();
            INHERITABLE.set(businessContext);
        } else {
            CURRENT.set(businessContext);
            INHERITABLE.remove();
        }
    }

    /**
     * 移除当前的业务上下文。
     */
    public static void removeCurrent() {
        CURRENT.remove();
        INHERITABLE.remove();
    }

    /**
     * 生成一个临时持有业务上下文的工具类。
     *
     * @param businessContext 业务上下文
     * @return 临时持有业务上下文的工具类
     */
    public static TempHolder with(BusinessContext businessContext) {
        return new TempHolder(businessContext);
    }

    /**
     * 临时持有业务上下文的工具类。
     */
    public static class TempHolder implements NonThrowableCloseable {
        private static final Logger LOGGER = LoggerFactory.getLogger(TempHolder.class);

        /**
         * 旧的业务上下文。
         */
        private final BusinessContext oldContext;

        public TempHolder(BusinessContext context) {
            this.oldContext = getCurrent();
            setCurrent(context);
            LogUtil.trace(LOGGER, "BusinessContextHolder.TempHolder#init<>, holder = {}, set current business context to {}",
                    this, context);
        }

        @Override
        public void close() {
            setCurrent(this.oldContext);
            LogUtil.trace(LOGGER, "BusinessContextHolder.TempHolder#close(), holder = {}, restore current business context to {}",
                    this, this.oldContext);
        }
    }
}
