package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.model.context.ContextAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务上下文的访问器（辅助工具类）。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BusinessContextAccessor extends ContextAccessor {
    /**
     * 业务上下文。
     */
    private final BusinessContext context;

    /**
     * 构造函数。
     *
     * @param context 业务上下文
     */
    public BusinessContextAccessor(BusinessContext context) {
        super(context);
        this.context = context;
    }

    /**
     * Key: 发生的错误。
     */
    public static final String KEY_ERROR = ClassUtil.resolveKey(Throwable.class);

    /**
     * 获取发生的错误。
     *
     * @return 发生的错误
     */
    public Throwable getError() {
        return this.getAttribute(KEY_ERROR);
    }

    /**
     * 设置发生的错误。
     *
     * @param error 发生的错误
     */
    public void setError(Throwable error) {
        this.setAttribute(KEY_ERROR, error);
    }

    /**
     * 如果没有发生错误，那么设置发生的错误。
     *
     * @param error 发生的错误
     */
    public void setErrorIfAbsent(Throwable error) {
        this.setAttributeIfAbsent(KEY_ERROR, error);
    }

    /**
     * Key: 业务上下文的处理器。
     */
    public static final String KEY_HANDLER = ClassUtil.resolveKey(BusinessContextAccessor.class, "handler");

    /**
     * 获取业务上下文的处理器。
     *
     * @param <T> 业务上下文的处理器
     * @return 业务上下文的处理器
     */
    public <T> T getHandler() {
        return this.getAttribute(KEY_HANDLER);
    }

    /**
     * 设置业务上下文的处理器。
     *
     * @param handler 业务上下文的处理器
     */
    public void setHandler(Object handler) {
        this.setAttribute(KEY_HANDLER, handler);
    }

    /**
     * 如果没有业务上下文的处理器，那么设置业务上下文的处理器。
     *
     * @param handler 业务上下文的处理器
     */
    public void setHandlerIfAbsent(Object handler) {
        this.setAttributeIfAbsent(KEY_HANDLER, handler);
    }
}
