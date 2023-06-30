package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.utils.model.AbstractTagsTemps;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 返回结果的定义。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnValue extends AbstractTagsTemps {
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
     * 从其他实例中加载数据。
     *
     * @param other     其他实例
     * @param overwrite 是否覆盖已有数据
     */
    public void load(ReturnValue other, boolean overwrite) {
        // 检查参数
        if (other == null || other == this) {
            return;
        }

        // 导入数据
        if (overwrite) {
            this.setCode(other.getCode());
            this.setMessage(other.getMessage());
            this.setUserTip(other.getUserTip());
            MapUtil.copy(other.getTags(), this.getTags(true), true);
            MapUtil.copy(other.getTemps(false), this.getTemps(true), true);
        } else {
            if (StringUtil.isEmpty(this.getCode())) {
                this.setCode(other.getCode());
            }
            if (StringUtil.isEmpty(this.getMessage())) {
                this.setMessage(other.getMessage());
            }
            if (StringUtil.isEmpty(this.getUserTip())) {
                this.setUserTip(other.getUserTip());
            }
            MapUtil.copy(other.getTags(), this.getTags(true), false);
            MapUtil.copy(other.getTemps(false), this.getTemps(true), false);
        }
    }
}
