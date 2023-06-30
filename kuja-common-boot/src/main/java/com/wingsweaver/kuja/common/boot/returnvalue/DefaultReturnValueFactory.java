package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.exception.BusinessException;
import com.wingsweaver.kuja.common.boot.i18n.MessageHelper;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 默认的 {@link ReturnValueFactory} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultReturnValueFactory extends AbstractComponent implements ReturnValueFactory {
    /**
     * 消息辅助工具类。
     */
    private MessageHelper messageHelper;

    /**
     * 成功的返回结果。
     */
    private ReturnValue success;

    /**
     * 失败的返回结果。
     */
    private ReturnValue fail;

    /**
     * 返回值定制器。
     */
    private List<ReturnValueCustomizer> customizers;

    @Override
    public void patchSuccess(ReturnValue returnValue) {
        this.patchCode(returnValue, () -> this.success.getCode());
        this.patchMessage(returnValue, () -> this.success.getMessage());
        this.patchUserTip(returnValue, () -> this.success.getUserTip());
        this.patchTags(returnValue, this.success.getTags());
        this.patchTemp(returnValue, this.success.getTemps(false));
        this.customize(returnValue);
    }

    private void customize(ReturnValue returnValue) {
        this.customizers.forEach(customizer -> customizer.customize(returnValue));
    }

    @SuppressWarnings("unchecked")
    private void patchTemp(ReturnValue returnValue, Map<String, ?> map) {
        if (!CollectionUtils.isEmpty(map)) {
            MapUtil.copy((Map<String, Object>) map, returnValue.getTemps(true), false);
        }
    }

    @SuppressWarnings("unchecked")
    private void patchTags(ReturnValue returnValue, Map<String, ?> map) {
        if (!CollectionUtils.isEmpty(map)) {
            MapUtil.copy((Map<String, Object>) map, returnValue.getTags(true), false);
        }
    }

    private void patchUserTip(ReturnValue returnValue, Supplier<String> supplier) {
        if (StringUtil.isEmpty(returnValue.getUserTip())) {
            returnValue.setUserTip(this.resolveText(supplier.get()));
        }
    }

    private void patchMessage(ReturnValue returnValue, Supplier<String> supplier) {
        if (StringUtil.isEmpty(returnValue.getMessage())) {
            returnValue.setMessage(this.resolveText(supplier.get()));
        }
    }

    private void patchCode(ReturnValue returnValue, Supplier<String> supplier) {
        if (StringUtil.isEmpty(returnValue.getCode())) {
            returnValue.setCode(this.resolveText(supplier.get()));
        }
    }

    private String resolveText(String patternOrCode) {
        if (StringUtil.isEmpty(patternOrCode)) {
            return null;
        }
        return this.messageHelper.format(patternOrCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid i18 message code: " + patternOrCode));
    }

    @Override
    public void patchFail(ReturnValue returnValue, Throwable error) {
        this.patchCode(returnValue, () -> this.resolveCode(error));
        this.patchMessage(returnValue, () -> this.resolveMessage(error));
        this.patchUserTip(returnValue, () -> this.resolveUserTip(error));
        this.patchTags(returnValue, this.resolveTags(error));
        this.patchTemp(returnValue, this.resolveTemps(error));
        this.customize(returnValue);
    }

    private Map<String, ?> resolveTemps(Throwable error) {
        if (error instanceof BusinessException) {
            ErrorDefinition errorDefinition = ((BusinessException) error).getErrorDefinition();
            if (errorDefinition != null) {
                return errorDefinition.getTemps();
            }
        }
        return this.fail.getTemps(false);
    }

    private Map<String, ?> resolveTags(Throwable error) {
        if (error instanceof BusinessException) {
            ErrorDefinition errorDefinition = ((BusinessException) error).getErrorDefinition();
            if (errorDefinition != null) {
                return errorDefinition.getTags();
            }
        }
        return this.fail.getTags();
    }

    private String resolveCode(Throwable error) {
        if (error instanceof BusinessException) {
            return ((BusinessException) error).getCode();
        }
        return this.fail.getCode();
    }

    private String resolveUserTip(Throwable error) {
        String userTip = null;

        if (error instanceof BusinessException) {
            userTip = ((BusinessException) error).getUserTip();
        }

        if (StringUtil.isEmpty(userTip) && StringUtil.isNotEmpty(this.fail.getUserTip())) {
            userTip = this.resolveText(this.fail.getUserTip());
        }

        if (StringUtil.isEmpty(userTip) && error != null) {
            userTip = error.getLocalizedMessage();
        }

        return userTip;
    }

    private String resolveMessage(Throwable error) {
        String message = null;

        if (error instanceof BusinessException) {
            message = error.getMessage();
        }

        if (StringUtil.isEmpty(message) && StringUtil.isNotEmpty(this.fail.getMessage())) {
            return this.resolveText(this.fail.getMessage());
        }

        if (StringUtil.isEmpty(message) && error != null) {
            message = error.getMessage();
        }

        return message;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        AssertState.Named.notNull("success", this.getSuccess());
        AssertState.Named.notNull("fail", this.getFail());

        // 初始化 messageHelper
        this.initMessageHelper();

        // 初始化 customizers
        this.initCustomizers();
    }

    /**
     * 初始化 customizers。
     */
    protected void initCustomizers() {
        if (this.customizers == null) {
            this.customizers = this.getBeansOrdered(ReturnValueCustomizer.class);
        }
    }

    /**
     * 初始化 messageHelper。
     */
    protected void initMessageHelper() {
        if (this.messageHelper == null) {
            this.messageHelper = this.getBean(MessageHelper.class);
        }
    }
}
