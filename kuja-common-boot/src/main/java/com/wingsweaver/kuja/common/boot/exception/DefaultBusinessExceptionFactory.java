package com.wingsweaver.kuja.common.boot.exception;

import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionAttributes;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinitionRepository;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * 默认的 {@link BusinessExceptionFactory} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultBusinessExceptionFactory extends AbstractComponent implements BusinessExceptionFactory {
    /**
     * 错误定义仓库。
     */
    private ErrorDefinitionRepository errorDefinitionRepository;

    /**
     * 错误定义辅助工具类。
     */
    private ErrorDefinitionAttributes errorDefinitionAttributes;

    /**
     * 异常定制化处理器。
     */
    private List<BusinessExceptionCustomizer> exceptionCustomizers;

    @Override
    public BusinessException create(ErrorDefinition errorDefinition, Object... args) {
        return this.create(null, errorDefinition, args);
    }

    @Override
    public BusinessException create(Throwable cause, ErrorDefinition errorDefinition, Object... args) {
        // 检查参数
        if (errorDefinition == null) {
            throw new IllegalArgumentException("[errorDefinition] is null");
        }

        // 创建异常实例
        String message = this.errorDefinitionAttributes.resolveMessage(cause, errorDefinition, args);
        String userTip = this.errorDefinitionAttributes.resolveUserTip(cause, errorDefinition, args);
        BusinessException exception = new BusinessException(message, cause, errorDefinition);
        exception.withExtendedAttribute("thread", Thread.currentThread());
        exception.withExtendedAttribute("args", args);
        exception.setUserTip(userTip);
        this.initializeException(exception);

        // 返回异常实例
        return exception;
    }

    /**
     * 初始化业务异常。
     *
     * @param exception 业务异常
     */
    private void initializeException(BusinessException exception) {
        // 调整 stackTraceElement，去除本项目中的调用
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        String thisClassName = this.getClass().getName();
        boolean foundThis = false;
        int index = 0;
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (stackTraceElement.getClassName().startsWith(thisClassName)) {
                foundThis = true;
            } else if (foundThis) {
                break;
            }
            index++;
        }
        exception.setStackTrace(Arrays.copyOfRange(stackTraceElements, index, stackTraceElements.length));

        // 定制化异常
        this.exceptionCustomizers.forEach(customizer -> customizer.customize(exception));
    }

    @Override
    public BusinessException create(String errorDefinitionCode, Object... args) {
        ErrorDefinition errorDefinition = this.resolveErrorDefinition(errorDefinitionCode);
        if (errorDefinition == null) {
            throw new IllegalArgumentException("errorDefinitionCode [" + errorDefinitionCode + "] is not found");
        }
        return this.create(null, errorDefinition, args);
    }

    @Override
    public BusinessException create(Throwable cause, String errorDefinitionCode, Object... args) {
        ErrorDefinition errorDefinition = this.resolveErrorDefinition(errorDefinitionCode);
        if (errorDefinition == null) {
            throw new IllegalArgumentException("errorDefinitionCode [" + errorDefinitionCode + "] is not found");
        }
        return this.create(cause, errorDefinition, args);
    }

    /**
     * 根据错误定义编码解析错误定义。
     *
     * @param errorDefinitionCode 错误定义编码
     * @return 错误定义
     */
    protected ErrorDefinition resolveErrorDefinition(String errorDefinitionCode) {
        return this.errorDefinitionRepository.getErrorDefinition(errorDefinitionCode);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 errorDefinitionRepository
        this.initializeErrorDefinitionRepository();

        // 初始化 errorDefinitionAttributes
        this.initializeErrorDefinitionAttributes();

        // 初始化 exceptionCustomizers
        this.initializeExceptionCustomizers();
    }

    /**
     * 初始化 errorDefinitionRepository。
     */
    protected void initializeErrorDefinitionRepository() {
        if (this.errorDefinitionRepository == null) {
            this.errorDefinitionRepository = this.getBean(ErrorDefinitionRepository.class);
        }
    }

    /**
     * 初始化 errorDefinitionAttributes。
     */
    protected void initializeErrorDefinitionAttributes() {
        if (this.errorDefinitionAttributes == null) {
            this.errorDefinitionAttributes = this.getBean(ErrorDefinitionAttributes.class);
        }
    }

    /**
     * 初始化 exceptionCustomizers。
     */
    protected void initializeExceptionCustomizers() {
        if (this.exceptionCustomizers == null) {
            this.exceptionCustomizers = this.getBeansOrdered(BusinessExceptionCustomizer.class);
        }
    }
}
