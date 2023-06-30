package com.wingsweaver.kuja.common.utils.model.context;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.attributes.MapMutableAttributes;
import com.wingsweaver.kuja.common.utils.model.id.IdSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于 {@link MapMutableAttributes} 的 {@link Context} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class MapContext extends MapMutableAttributes<String> implements Context, IdSetter<String> {
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
     * @param map Map 对象
     */
    public MapContext(Map<String, ?> map) {
        super(map);
    }

    /**
     * 构造函数。
     *
     * @param initCapacity 初始容量
     */
    public MapContext(int initCapacity) {
        this(new HashMap<>(initCapacity));
    }

    /**
     * 构造函数。
     */
    public MapContext() {
        this(BufferSizes.SMALL);
    }

    /**
     * 构造函数。
     *
     * @param context Context 实例
     */
    public MapContext(Context context) {
        super(context);
    }

    @Override
    public String toString() {
        return this.getClass().getTypeName() + "#" + this.getId();
    }
}
