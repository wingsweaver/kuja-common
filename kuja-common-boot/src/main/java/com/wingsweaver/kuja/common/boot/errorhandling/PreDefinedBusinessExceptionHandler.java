package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.exception.BusinessException;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * 用于处理 {@link BusinessException} 的 {@link ErrorHandler} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class PreDefinedBusinessExceptionHandler implements ErrorHandler, InitializingBean, DefaultOrdered {
    private ReturnValueFactory returnValueFactory;

    private List<ErrorDefinition2ReturnValuePatcher> patchers;

    @Override
    public boolean handle(ErrorHandlerContext context) {
        Throwable error = context.getInputError();
        if (error instanceof BusinessException && !context.isHandled() && context.getReturnValue() == null) {
            BusinessException exception = (BusinessException) error;
            ErrorDefinition errorDefinition = exception.getErrorDefinition();

            // 设置返回值
            ReturnValue returnValue = new ReturnValue();
            returnValue.setCode(StringUtil.notEmptyOr(exception.getCode(), errorDefinition.getCode()));
            returnValue.setMessage(exception.getMessage());
            returnValue.setUserTip(exception.getUserTip());
            MapUtil.copy(errorDefinition.getTags(), returnValue.getTags(true), true);
            MapUtil.copy(errorDefinition.getTemps(), returnValue.getTemps(true), true);
            this.patchers.forEach(patcher -> patcher.patch(errorDefinition, returnValue));
            this.returnValueFactory.patchFail(returnValue, error);

            // 更新处理上下文
            context.setHandled(true);
            context.setReturnValue(returnValue);
            return true;
        }

        return false;
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("returnValueFactory", this.getReturnValueFactory());
        AssertState.Named.notNull("patchers", this.getPatchers());
    }
}
