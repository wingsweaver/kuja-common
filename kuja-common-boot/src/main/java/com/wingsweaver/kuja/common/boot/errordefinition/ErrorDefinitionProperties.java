package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * 错误定义的相关设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_ERROR_DEFINITION_PROPERTIES)
public class ErrorDefinitionProperties extends AbstractPojo {
    /**
     * 错误定义的资源。
     */
    private List<String> locations = Collections.singletonList("classpath:/error-definition.properties");

    /**
     * 错误定义 Key 的分隔符。
     */
    private String delimiter = ".";

    /**
     * 无法读取错误定义时是否快速失败。
     */
    private boolean failFast = true;

    /**
     * 没有定义 Message 和 UserTip 时，是否允许回退到 Code。
     */
    private boolean fallbackToCode = false;
}
