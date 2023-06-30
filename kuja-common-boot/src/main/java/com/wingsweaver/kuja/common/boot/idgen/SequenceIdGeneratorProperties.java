package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 序列 ID 的生成设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_SEQUENCE_ID)
public class SequenceIdGeneratorProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * BIT 数。
     */
    private int bits = 12;
}
