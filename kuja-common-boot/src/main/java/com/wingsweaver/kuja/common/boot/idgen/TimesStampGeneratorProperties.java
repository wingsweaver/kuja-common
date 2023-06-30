package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import com.wingsweaver.kuja.common.utils.support.idgen.snowflake.TimeStampUnit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 时间戳的生成设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_TIMESTAMP)
public class TimesStampGeneratorProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * BIT 数。
     */
    private int bits = 41;

    /**
     * 起点时刻。
     */
    private String start;

    /**
     * 时间单位。
     */
    private TimeStampUnit unit = TimeStampUnit.SECONDS;
}
