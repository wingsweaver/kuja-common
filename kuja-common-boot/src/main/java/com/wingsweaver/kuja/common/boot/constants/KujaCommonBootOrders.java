package com.wingsweaver.kuja.common.boot.constants;

import com.wingsweaver.kuja.common.utils.constants.Orders;

/**
 * kuja-common-boot 模块的 Bean Order 定义。
 *
 * @author wingsweaver
 */
public interface KujaCommonBootOrders {
    /**
     * {@link com.wingsweaver.kuja.common.boot.warmup.WarmUpTriggerBean}。
     */
    int WARM_UP_TRIGGER = Orders.LOWEST_PRECEDENCE - Orders.STEP_LARGE;

    /**
     * {@link com.wingsweaver.kuja.common.boot.appinfo.AppInfoCustomizationBean}。
     */
    int APP_INFO_CUSTOMIZATION_BEAN = Orders.HIGHEST_PRECEDENCE + Orders.STEP_HUGE;

    /**
     * {@link com.wingsweaver.kuja.common.boot.env.postprocessor.PresetEnvironmentPostProcessor}。<br>
     * 需要在 {@link org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor} 之前执行。
     */
    int PRESET_ENVIRONMENT_POST_PROCESSOR = Orders.HIGHEST_PRECEDENCE + 8;

    /**
     * {@link com.wingsweaver.kuja.common.boot.env.postprocessor.FallbackEnvironmentPostProcessor}。
     */
    int FALLBACK_ENVIRONMENT_POST_PROCESSOR = Orders.LOWEST_PRECEDENCE - Orders.STEP_HUGE;

    /**
     * {@link com.wingsweaver.kuja.common.boot.env.postprocessor.InheritedEnvironmentPostProcessor}。
     */
    int INHERITED_ENVIRONMENT_POST_PROCESSOR = PRESET_ENVIRONMENT_POST_PROCESSOR + 2;

    /**
     * {@link com.wingsweaver.kuja.common.boot.env.postprocessor.RemoveInheritedEnvironmentPostProcessor}。
     */
    int REMOVE_INHERITED_ENVIRONMENT_POST_PROCESSOR = Orders.LOWEST_PRECEDENCE - Orders.STEP_TINY;
}
