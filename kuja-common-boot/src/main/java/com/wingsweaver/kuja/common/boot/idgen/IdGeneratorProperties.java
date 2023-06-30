package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * ID 生成器的相关设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR)
public class IdGeneratorProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * 缓存大小。<br>
     * 小于或者等于 0 时，不使用缓存 ({@link com.wingsweaver.kuja.common.utils.support.idgen.CachedIdGenerator})。
     */
    private int cacheSize = 1024;

    /**
     * 重试的等待时间（毫秒）。
     */
    private long retryInterval = 100;

    /**
     * 最大重试次数。
     */
    private int maxRetries = 10;

    /**
     * 时间戳的生成设置。
     */
    @NestedConfigurationProperty
    private TimesStampGeneratorProperties timeStamp = new TimesStampGeneratorProperties();

    /**
     * WorkerId 的生成设置。
     */
    @NestedConfigurationProperty
    private WorkerIdResolverProperties workerId = new WorkerIdResolverProperties();

    /**
     * 序列 ID 的生成设置。
     */
    @NestedConfigurationProperty
    private SequenceIdGeneratorProperties sequenceId = new SequenceIdGeneratorProperties();

}
