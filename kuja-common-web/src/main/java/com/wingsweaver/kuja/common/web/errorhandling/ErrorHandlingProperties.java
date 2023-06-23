package com.wingsweaver.kuja.common.web.errorhandling;

import com.wingsweaver.kuja.common.web.constants.KujaCommonWebKeys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 错误处理相关的属性。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonWebKeys.PREFIX_ERROR_HANDLING_PROPERTIES)
public class ErrorHandlingProperties {
    /**
     * 全局错误处理 {@link org.springframework.web.bind.annotation.ControllerAdvice} 相关的属性。
     */
    @NestedConfigurationProperty
    private GlobalErrorAdviceProperties globalErrorAdvice = new GlobalErrorAdviceProperties();

    /**
     * 全局错误处理 {@link org.springframework.boot.web.servlet.error.ErrorController} 相关的属性。
     */
    @NestedConfigurationProperty
    private GlobalErrorControllerProperties globalErrorController = new GlobalErrorControllerProperties();

    /**
     * 全局错误处理 {@link org.springframework.web.bind.annotation.ControllerAdvice} 相关的属性。
     */
    @Getter
    @Setter
    public static class GlobalErrorAdviceProperties {
        /**
         * 是否启用。
         */
        private Boolean enabled = true;
    }

    /**
     * 全局错误处理 {@link org.springframework.stereotype.Controller} 相关的属性。
     */
    @Getter
    @Setter
    public static class GlobalErrorControllerProperties {
        /**
         * 是否启用。
         */
        private Boolean enabled = true;
    }
}
