package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的 {@link ErrorDefinition} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefaultErrorDefinition implements ErrorDefinition {
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
     * 附加数据。
     */
    private Map<String, Object> tags;

    /**
     * 临时数据。
     */
    private Map<String, Object> temps;

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

    @SuppressWarnings("SameParameterValue")
    public Map<String, Object> getTags(boolean createIfAbsent) {
        if (this.tags == null && createIfAbsent) {
            this.tags = new HashMap<>(BufferSizes.SMALL);
        }
        return this.tags;
    }

    @SuppressWarnings("SameParameterValue")
    public Map<String, Object> getTemps(boolean createIfAbsent) {
        if (this.temps == null && createIfAbsent) {
            this.temps = new HashMap<>(BufferSizes.SMALL);
        }
        return this.temps;
    }
}
