package com.wingsweaver.kuja.common.boot.errorhandling;

import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回 {@link com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue} 的 {@link AbstractErrorHandlingComponent} 的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractReturnValueErrorHandlingComponent extends AbstractErrorHandlingComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReturnValueErrorHandlingComponent.class);

    /**
     * 是否已经处理过的 Key。
     */
    protected static final String KEY_FALLBACK_HANDLED = ClassUtil.resolveKey(AbstractReturnValueErrorHandlingComponent.class, "fallbackHandled");

    /**
     * ReturnValueFactory 实例。
     */
    private ReturnValueFactory returnValueFactory;

    @Override
    protected void postProcessContext(ErrorHandlerContext context) {
        // 检查是否异常是否被处理
        if (context.isHandled()) {
            return;
        }

        // 如果没有被处理的话，执行默认处理
        LogUtil.trace(LOGGER, "error is not handled, fallback to default handler");
        ReturnValue returnValue = this.returnValueFactory.fail(this.resolveFinalError(context));
        context.setReturnValue(returnValue);
        context.setHandled(true);
        context.setAttribute(KEY_FALLBACK_HANDLED, true);
    }

    @Override
    protected Object normalizeReturnValue(ErrorHandlerContext context, Object returnValue) {
        if (returnValue instanceof ReturnValue && !context.getAttribute(KEY_FALLBACK_HANDLED, false)) {
            // 按需刷新返回值
            LogUtil.trace(LOGGER, "patch return value");
            this.returnValueFactory.patchFail((ReturnValue) returnValue, this.resolveFinalError(context));
        }

        // 返回结果
        return returnValue;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 returnValueFactory
        this.initReturnValueFactory();
    }

    /**
     * 初始化 returnValueFactory。
     */
    protected void initReturnValueFactory() {
        if (this.returnValueFactory == null) {
            this.returnValueFactory = this.getBean(ReturnValueFactory.class);
        }
    }
}
