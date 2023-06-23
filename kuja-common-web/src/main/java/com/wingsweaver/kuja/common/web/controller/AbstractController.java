package com.wingsweaver.kuja.common.web.controller;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlingDelegate;
import com.wingsweaver.kuja.common.boot.model.AbstractBusinessComponent;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.web.context.WebContextAccessor;
import com.wingsweaver.kuja.common.web.errorhandling.AbstractResponseEntityReturnValueErrorHandlingComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller 的基类。<br>
 * Controller 是指提供业务逻辑的类，它们通常是无状态的，也就是说，它们不会持有任何状态，也不会修改任何状态。<br>
 * 其实现类一般被 {@link org.springframework.stereotype.Controller} 注解标记。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractController extends AbstractBusinessComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    @Autowired
    private ErrorHandlingDelegate errorHandlingDelegate;

    /**
     * 异常处理组件。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private InnerErrorHandlingComponent errorHandlingComponent;

    /**
     * 处理异常。<br>
     * 允许各 Controller 定制自己的异常处理，而无需使用统一的异常处理器。
     *
     * @param businessContext 业务上下文
     * @param error           要处理的异常
     * @return 处理结果
     * @throws Throwable 异常无法处理，继续交给后续异常处理器处理
     */
    @ExceptionHandler(Throwable.class)
    public Object onError(BusinessContext businessContext, Throwable error) throws Throwable {
        LogUtil.trace(LOGGER, () -> "AbstractController.onError, businessContext = " + businessContext
                + ", request = " + new WebContextAccessor(businessContext).getRequestFullPath());
        return this.errorHandlingComponent.handleError(businessContext, error);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        AssertState.Named.notNull("errorHandlingDelegate", this.getErrorHandlingDelegate());

        // 初始化 errorHandlingComponent
        this.errorHandlingComponent = new InnerErrorHandlingComponent();
        this.errorHandlingComponent.setReturnValueFactory(this.getReturnValueFactory());
        this.errorHandlingComponent.setErrorHandlingDelegate(this.getErrorHandlingDelegate());
    }

    /**
     * 内置的异常处理组件。
     */
    private static class InnerErrorHandlingComponent extends AbstractResponseEntityReturnValueErrorHandlingComponent {
    }
}
