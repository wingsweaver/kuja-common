package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.model.tags.convert.TagConversionService;
import com.wingsweaver.kuja.common.utils.support.idgen.LongIdGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.CachedSnowFlakeIdGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.DefaultSequenceIdGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.DefaultTimeStampGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.SequenceIdGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.SnowFlakeIdGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.TimeStampGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.WorkerIdResolver;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Date;

/**
 * {@link LongIdGenerator} 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({IdGeneratorProperties.class, TimesStampGeneratorProperties.class,
        WorkerIdResolverProperties.class, SequenceIdGeneratorProperties.class})
@ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR, name = "enabled", havingValue = "true", matchIfMissing = true)
public class LongIdGeneratorConfiguration extends AbstractConfiguration {
    /**
     * 创建 LongIdGenerator 对象。
     *
     * @param properties 属性
     * @return LongIdGenerator 对象
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_TIMESTAMP, name = "enabled", havingValue = "true", matchIfMissing = true)
    public TimeStampGenerator kujaTimeStampGenerator(TimesStampGeneratorProperties properties) {
        DefaultTimeStampGenerator timeStampGenerator = new DefaultTimeStampGenerator(properties.getBits());
        timeStampGenerator.setUnit(properties.getUnit());
        String start = StringUtil.trimToEmpty(properties.getStart());
        if (!start.isEmpty()) {
            try {
                Date startTimeStamp = TagConversionService.toValue(start, Date.class);
                timeStampGenerator.setStartEpoch(startTimeStamp.getTime());
            } catch (Exception ex) {
                throw new ExtendedRuntimeException("Failed to parse start time stamp: " + start, ex)
                        .withExtendedAttribute("start", start);
            }
        }
        return timeStampGenerator;
    }

    /**
     * 创建 WorkerIdResolver 对象。
     *
     * @param properties 属性
     * @return WorkerIdResolver 对象
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_SEQUENCE_ID, name = "enabled", havingValue = "true", matchIfMissing = true)
    public SequenceIdGenerator kujaSequenceIdGenerator(SequenceIdGeneratorProperties properties) {
        return new DefaultSequenceIdGenerator(properties.getBits());
    }

    /**
     * 创建 WorkerIdResolver 对象。
     *
     * @param idGeneratorProperties 属性
     * @param timeStampGenerator    TimeStampGenerator 对象
     * @param workerIdResolver      WorkerIdResolver 对象
     * @param sequenceIdGenerator   SequenceIdGenerator 对象
     * @return LongIdGenerator 对象
     */
    @Bean
    @ConditionalOnMissingBean
    public LongIdGenerator kujaLongIdGenerator(IdGeneratorProperties idGeneratorProperties,
                                               TimeStampGenerator timeStampGenerator,
                                               WorkerIdResolver workerIdResolver,
                                               SequenceIdGenerator sequenceIdGenerator) {
        SnowFlakeIdGenerator snowFlakeIdGenerator = new SnowFlakeIdGenerator();

        FixedBackOff backOff = new FixedBackOff(idGeneratorProperties.getRetryInterval(),
                idGeneratorProperties.getMaxRetries());
        snowFlakeIdGenerator.setBackOff(backOff);
        snowFlakeIdGenerator.setTimeStampGenerator(timeStampGenerator);
        snowFlakeIdGenerator.setWorkerIdResolver(workerIdResolver);
        snowFlakeIdGenerator.setSequenceIdGenerator(sequenceIdGenerator);

        // 按需生成带缓存的 IdGenerator
        LongIdGenerator idGenerator = snowFlakeIdGenerator;
        if (idGeneratorProperties.getCacheSize() > 0) {
            idGenerator = new CachedSnowFlakeIdGenerator(snowFlakeIdGenerator, idGeneratorProperties.getCacheSize(), backOff);
        }

        // 返回
        return idGenerator;
    }
}
