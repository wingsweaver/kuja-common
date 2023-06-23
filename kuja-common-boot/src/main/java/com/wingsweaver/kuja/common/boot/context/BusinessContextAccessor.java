package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.model.attributes.AttributesAccessor;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 业务上下文的访问器（辅助工具类）。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BusinessContextAccessor extends AttributesAccessor<String> {
    /**
     * 业务上下文。
     */
    private final BusinessContext context;

    public BusinessContextAccessor(BusinessContext context) {
        super(context);
        this.context = context;
    }

    /**
     * Key: 业务上下文的类型。<br>
     * 如：WebFlux, WebServlet (Jee) 等。
     */
    public static final String KEY_CONTEXT_TYPE = ClassUtil.resolveKey(BusinessContextAccessor.class, "contextType");

    public <T> T getContextType() {
        return this.getAttribute(KEY_CONTEXT_TYPE);
    }

    public void setContextType(Object contextType) {
        this.setAttribute(KEY_CONTEXT_TYPE, contextType);
    }

    public boolean isContextType(Class<?> clazz) {
        return clazz.isInstance(this.getContextType());
    }

    public boolean isContextType(Object contextType) {
        return Objects.equals(this.getContextType(), contextType);
    }

    /**
     * Key: 发生的错误。
     */
    public static final String KEY_ERROR = ClassUtil.resolveKey(Throwable.class);

    public Throwable getError() {
        return this.getAttribute(KEY_ERROR);
    }

    public void setError(Throwable error) {
        this.setAttribute(KEY_ERROR, error);
    }

    public void setErrorIfAbsent(Throwable error) {
        this.setAttributeIfAbsent(KEY_ERROR, error);
    }

    public static final String KEY_HANDLER = ClassUtil.resolveKey(BusinessContextAccessor.class, "handler");

    public <T> T getHandler() {
        return this.getAttribute(KEY_HANDLER);
    }

    public void setHandler(Object handler) {
        this.setAttribute(KEY_HANDLER, handler);
    }

    public void setHandlerIfAbsent(Object handler) {
        this.setAttributeIfAbsent(KEY_HANDLER, handler);
    }
}
