package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.utils.model.AbstractTagsTemps;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringIgnored;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 默认的 {@link ErrorDefinition} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultErrorDefinition extends AbstractTagsTemps implements ErrorDefinition {
    /**
     * 错误定义的优先度。
     */
    private int order;

    /**
     * 错误编码。
     */
    private String code;

    /**
     * 错误消息（面向开发者）。
     */
    private String message;

    /**
     * 用户提示（面向终端用户）。
     */
    private String userTip;

    /**
     * 从另一个 {@link ErrorDefinition} 实例中加载数据。
     *
     * @param errorDefinition ErrorDefinition 实例
     * @param overwrite       是否覆盖已有数据
     */
    public void load(ErrorDefinition errorDefinition, boolean overwrite) {
        // 检查参数
        if (errorDefinition == null || errorDefinition == this) {
            return;
        }

        // 导入数据
        if (overwrite) {
            this.order = errorDefinition.getOrder();
            this.code = errorDefinition.getCode();
            this.message = errorDefinition.getMessage();
            this.userTip = errorDefinition.getUserTip();
            MapUtil.copy(errorDefinition.getTags(), this.getTags(true), true);
            MapUtil.copy(errorDefinition.getTemps(), this.getTemps(true), true);
        } else {
            if (this.order == 0) {
                this.order = errorDefinition.getOrder();
            }
            if (StringUtil.isEmpty(this.code)) {
                this.code = errorDefinition.getCode();
            }
            if (StringUtil.isEmpty(this.message)) {
                this.message = errorDefinition.getMessage();
            }
            if (StringUtil.isEmpty(this.userTip)) {
                this.userTip = errorDefinition.getUserTip();
            }
            MapUtil.copy(errorDefinition.getTags(), this.getTags(true), false);
            MapUtil.copy(errorDefinition.getTemps(), this.getTemps(true), false);
        }
    }

    /**
     * 从另一个 {@link ReturnValue} 实例中加载数据。
     *
     * @param returnValue ReturnValue 实例
     * @param overwrite   是否覆盖已有数据
     */
    public void load(ReturnValue returnValue, boolean overwrite) {
        // 检查参数
        if (returnValue == null) {
            return;
        }

        // 导入数据
        if (overwrite) {
            this.code = returnValue.getCode();
            this.message = returnValue.getMessage();
            this.userTip = returnValue.getUserTip();
            MapUtil.copy(returnValue.getTags(), this.getTags(true), true);
            MapUtil.copy(returnValue.getTemps(false), this.getTemps(true), true);
        } else {
            if (StringUtil.isEmpty(this.code)) {
                this.code = returnValue.getCode();
            }
            if (StringUtil.isEmpty(this.message)) {
                this.message = returnValue.getMessage();
            }
            if (StringUtil.isEmpty(this.userTip)) {
                this.userTip = returnValue.getUserTip();
            }
            MapUtil.copy(returnValue.getTags(), this.getTags(true), false);
            MapUtil.copy(returnValue.getTemps(false), this.getTemps(true), false);
        }
    }

    @Override
    @ToStringIgnored
    public Map<String, Object> getTemps() {
        return this.getTemps(false);
    }

    /**
     * 生成 Builder 实例。
     *
     * @return Builder 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@link ErrorDefinition} 的生成工具类。
     */
    public static final class Builder extends InnerBuilder<Builder> {
        // 什么也不做
    }

    /**
     * {@link ErrorDefinition} 的内部生成工具类。
     */
    protected static class InnerBuilder<B> extends AbstractTagsTemps.InnerBuilder<B> {
        /**
         * 错误定义的优先度。
         */
        protected int order;

        /**
         * 错误编码。
         */
        protected String code;

        /**
         * 错误消息（面向开发者）。
         */
        protected String message;

        /**
         * 用户提示（面向终端用户）。
         */
        protected String userTip;

        @SuppressWarnings("unchecked")
        public B order(int order) {
            this.order = order;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B code(String code) {
            this.code = code;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B message(String message) {
            this.message = message;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B userTip(String userTip) {
            this.userTip = userTip;
            return (B) this;
        }

        /**
         * 构建 {@link DefaultErrorDefinition} 实例。
         *
         * @return DefaultErrorDefinition 实例
         */
        public DefaultErrorDefinition build() {
            DefaultErrorDefinition errorDefinition = new DefaultErrorDefinition(this.order, this.code, this.message, this.userTip);
            errorDefinition.setTags(tags);
            errorDefinition.setTemps(temps);
            return errorDefinition;
        }
    }
}
