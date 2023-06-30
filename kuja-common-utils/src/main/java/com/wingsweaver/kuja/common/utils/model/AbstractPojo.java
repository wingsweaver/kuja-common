package com.wingsweaver.kuja.common.utils.model;

import com.wingsweaver.kuja.common.utils.support.tostring.ToStringBuilder;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringConfig;

import java.io.Serializable;

/**
 * POJO 的基类。<br>
 * 提供了基于 {@link ToStringBuilder} 将内容转换为字符串的功能。
 *
 * @author wingsweaver
 */
public abstract class AbstractPojo implements Serializable {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getTypeName());
        sb.append('(');
        ToStringBuilder.toStringBuilder(this, this::customizeToStringConfig, sb);
        sb.append(')');
        return sb.toString();
    }

    /**
     * toString 转换设置的定制处理。<br>
     * 默认关闭输出类型名称。
     *
     * @param config 转换设置
     */
    protected void customizeToStringConfig(ToStringConfig config) {
        config.setObjectToString(this);
        config.setIncludeTypeName(false);
        config.setMaxReflectDepth(1);
    }
}
