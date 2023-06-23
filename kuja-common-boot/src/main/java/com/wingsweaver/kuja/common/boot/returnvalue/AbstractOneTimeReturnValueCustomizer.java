package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * 只运行一次的 {@link ReturnValueCustomizer} 的抽象实现。
 *
 * @author wingsweaver
 */
public abstract class AbstractOneTimeReturnValueCustomizer extends AbstractReturnValueCustomizer implements InitializingBean {
    private final String keyCustomized = ClassUtil.resolveKey(this.getClass(), "customized");

    @Override
    public void customize(BusinessContext businessContext, ReturnValue returnValue) {
        if (!this.isCustomized(returnValue)) {
            this.customizeInternal(businessContext, returnValue);
            this.setCustomized(returnValue, true);
        }
    }

    /**
     * 对返回值进行定制化的实际处理。
     *
     * @param businessContext 业务上下文
     * @param returnValue     返回值
     */
    protected abstract void customizeInternal(BusinessContext businessContext, ReturnValue returnValue);

    protected boolean isCustomized(ReturnValue returnValue) {
        return returnValue.getTempValue(keyCustomized, false);
    }

    @SuppressWarnings("SameParameterValue")
    protected void setCustomized(ReturnValue returnValue, boolean customized) {
        returnValue.setTempValue(keyCustomized, customized);
    }

    @Override
    public void afterPropertiesSet() {
        // 什么也不做
    }
}
