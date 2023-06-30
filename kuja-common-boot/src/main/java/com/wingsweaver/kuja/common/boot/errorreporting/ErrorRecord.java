package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.utils.model.AbstractTagsTemps;
import com.wingsweaver.kuja.common.utils.model.id.IdSetter;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringIgnored;
import lombok.Getter;
import lombok.Setter;

/**
 * 错误报告的定义。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ErrorRecord extends AbstractTagsTemps implements IdSetter<String> {
    /**
     * 记录 ID。
     */
    private String id;

    /**
     * 生成的时间戳 (UTC)。
     */
    private final long creationTime = System.currentTimeMillis();

    /**
     * 错误实例。
     */
    @ToStringIgnored
    private transient Throwable error;

    @Override
    public String toString() {
        return this.getClass().getTypeName() + "#" + this.id;
    }
}
