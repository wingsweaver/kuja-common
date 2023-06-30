package com.wingsweaver.kuja.common.utils.model.context;

import com.wingsweaver.kuja.common.utils.model.attributes.LayeredMutableAttributes;
import com.wingsweaver.kuja.common.utils.model.id.IdSetter;
import lombok.Getter;
import lombok.Setter;

/**
 * 基于 {@link LayeredMutableAttributes} 的业务上下文实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class LayeredContext extends LayeredMutableAttributes<String> implements Context, IdSetter<String> {
    /**
     * 上下文 ID。
     */
    private String id;

    /**
     * 创建时间戳（UTC）。
     */
    private long creationTime = System.currentTimeMillis();

    /**
     * 构造函数。
     *
     * @param parent 父级上下文
     */
    public LayeredContext(Context parent) {
        super(parent);
    }

    @Override
    public Context getParent() {
        return (Context) super.getParent();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getTypeName()).append("#").append(this.getId());
        Context parent = this.getParent();
        if (parent != null) {
            sb.append("{parent: ").append(parent).append("}");
        }
        return sb.toString();
    }
}
