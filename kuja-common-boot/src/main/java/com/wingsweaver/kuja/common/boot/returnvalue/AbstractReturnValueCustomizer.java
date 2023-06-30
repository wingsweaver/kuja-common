package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link ReturnValueCustomizer} 的抽象实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractReturnValueCustomizer extends AbstractComponent implements ReturnValueCustomizer {
    /**
     * Key: 是否启用。
     */
    @Getter
    private final String keyEnabled = ClassUtil.resolveKey(this.getClass(), "enabled");

    /**
     * 是否启用。
     */
    private boolean enabled = true;

    @Override
    public void customize(ReturnValue returnValue) {
        BusinessContext businessContext = BusinessContextHolder.getCurrent();
        if (this.isEnabled(businessContext)) {
            this.customize(businessContext, returnValue);
        }
    }

    /**
     * 对返回值进行定制化。
     *
     * @param businessContext 业务上下文
     * @param returnValue     返回值
     */
    protected abstract void customize(BusinessContext businessContext, ReturnValue returnValue);

    /**
     * 判断是否启用。
     *
     * @param businessContext 业务上下文
     * @return 如果启用，则返回 true
     */
    protected boolean isEnabled(BusinessContext businessContext) {
        if (businessContext == null) {
            return this.isEnabled();
        } else {
            return businessContext.getAttribute(this.keyEnabled, this.isEnabled());
        }
    }
}
