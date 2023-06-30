package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;

/**
 * 只运行一次的 {@link ReturnValueCustomizer} 的抽象实现。
 *
 * @author wingsweaver
 */
public abstract class AbstractOneTimeReturnValueCustomizer extends AbstractReturnValueCustomizer {
    /**
     * Key: 是否已经被处理过。
     */
    @Getter
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

    /**
     * 判断返回值是否已经定制过。
     *
     * @param returnValue 返回值
     * @return 如果已经定制过，则返回 true
     */
    protected boolean isCustomized(ReturnValue returnValue) {
        return returnValue.getTempValue(keyCustomized, false);
    }

    /**
     * 设置返回值是否已经定制过。
     *
     * @param returnValue 返回值
     * @param customized  是否已经定制过
     */
    @SuppressWarnings("SameParameterValue")
    protected void setCustomized(ReturnValue returnValue, boolean customized) {
        returnValue.setTempValue(keyCustomized, customized);
    }
}
