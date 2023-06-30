package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.boot.i18n.MessageHelper;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * 默认的 {@link ErrorDefinitionAttributes} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultErrorDefinitionAttributes extends AbstractComponent implements ErrorDefinitionAttributes {
    /**
     * 消息帮助类。
     */
    private MessageHelper messageHelper;

    /**
     * 错误定义相关设置。
     */
    private ErrorDefinitionProperties properties;

    @Override
    public String resolveMessage(Throwable cause, ErrorDefinition errorDefinition, Object[] args) {
        String messageOrCode = errorDefinition.getMessage();
        if (StringUtil.isEmpty(messageOrCode)) {
            return null;
        }

        Optional<String> optional = this.messageHelper.format(messageOrCode, args);
        if (this.properties.isFailFast()) {
            return optional.orElseThrow(() -> new IllegalArgumentException(
                    "cannot resolve message for errorDefinition [" + errorDefinition.getCode() + "]"));
        } else {
            return optional.orElse(null);
        }
    }

    @Override
    public String resolveUserTip(Throwable cause, ErrorDefinition errorDefinition, Object[] args) {
        String userTipOrCode = errorDefinition.getUserTip();
        if (StringUtil.isEmpty(userTipOrCode)) {
            if (this.properties.isFallbackToCode()) {
                return this.messageHelper.getMessage(errorDefinition.getCode(), args).orElse(null);
            } else {
                return null;
            }
        }

        Optional<String> optional = this.messageHelper.format(userTipOrCode, args);
        if (this.properties.isFailFast()) {
            return optional.orElseThrow(() -> new IllegalArgumentException(
                    "cannot resolve userTip for errorDefinition [" + errorDefinition.getCode() + "]"));
        } else {
            return optional.orElse(null);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 properties
        this.initProperties();

        // 初始化 messageHelper
        this.initMessageHelper();
    }

    /**
     * 初始化 messageHelper。
     */
    protected void initMessageHelper() {
        if (this.messageHelper == null) {
            this.messageHelper = this.getBean(MessageHelper.class);
        }
    }

    /**
     * 初始化 properties。
     */
    protected void initProperties() {
        if (this.properties == null) {
            this.properties = this.getBean(ErrorDefinitionProperties.class);
        }
    }
}
