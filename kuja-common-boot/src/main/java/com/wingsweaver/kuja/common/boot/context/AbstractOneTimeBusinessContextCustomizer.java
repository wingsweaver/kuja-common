package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;

/**
 * 只运行一次的 {@link BusinessContextCustomizer} 的抽象实现。
 *
 * @author wingsweaver
 */
public abstract class AbstractOneTimeBusinessContextCustomizer implements BusinessContextCustomizer {
    @Getter
    private final String keyCustomized = ClassUtil.resolveKey(this.getClass(), "customized");

    @Override
    public void customize(BusinessContext businessContext) {
        if (!this.isCustomized(businessContext)) {
            this.customizeInternal(businessContext);
            this.setCustomized(businessContext, true);
        }
    }

    /**
     * 定制业务上下文的实际处理。
     *
     * @param businessContext 业务上下文
     */
    protected abstract void customizeInternal(BusinessContext businessContext);

    protected boolean isCustomized(BusinessContext businessContext) {
        return businessContext.getAttribute(keyCustomized, false);
    }

    @SuppressWarnings("SameParameterValue")
    protected void setCustomized(BusinessContext businessContext, boolean customized) {
        businessContext.setAttribute(keyCustomized, customized);
    }
}
