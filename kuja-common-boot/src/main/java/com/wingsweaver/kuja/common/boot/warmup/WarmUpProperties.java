package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 预热处理的相关设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_WARM_UP_PROPERTIES)
public class WarmUpProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * 优先级。
     */
    private Integer order;

    /**
     * 执行模式。
     */
    private Mode mode = Mode.RUNNER;

    /**
     * {@link WarmUpTaskExecutor} 的 Bean 名称。<br>
     * 如果不指定的话，使用默认的 WarmUpTaskExecutor。
     */
    private String taskExecutorName;

    /**
     * 是否异步执行。
     */
    private boolean async = true;

    /**
     * 使用默认的异步执行器 ({@link AsyncWarmUpTaskExecutor}) 时，关联的 Executor 的 Bean 的名称。<br>
     * 如果不指定的话，使用 {@link java.util.concurrent.CompletableFuture} 内置的 Executor。
     */
    private String asyncExecutorName;

    /**
     * 执行模式。
     */
    public enum Mode {
        /**
         * 以 CommandLineRunner 的模式执行。
         */
        RUNNER,

        /**
         * 以 SmartLifeCycle 的模式执行。
         */
        LIFE_CYCLE
    }
}
