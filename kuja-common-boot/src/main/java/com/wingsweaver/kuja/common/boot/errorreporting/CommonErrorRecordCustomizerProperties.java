package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.include.IncludeAttribute;
import com.wingsweaver.kuja.common.boot.include.IncludeSettings;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * {@link AbstractErrorRecordCustomizer} 对应的通用设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonErrorRecordCustomizerProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * {@link AppInfoErrorRecordCustomizer} 设置。
     */
    @NestedConfigurationProperty
    private IncludeSettings include = new IncludeSettings(IncludeAttribute.ALWAYS);
}
