package com.wingsweaver.kuja.common.utils.model.context;

import com.wingsweaver.kuja.common.utils.model.attributes.AttributesAccessor;

/**
 * 上下文的访问器（辅助工具类）。
 *
 * @author wingsweaver
 */
public class ContextAccessor extends AttributesAccessor<String> {
    /**
     * 业务上下文。
     */
    private final Context context;

    /**
     * 构造函数。
     *
     * @param context 业务上下文
     */
    public ContextAccessor(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 获取上下文的实例。
     *
     * @param <T> 上下文的类型
     * @return 上下文的实例
     */
    @SuppressWarnings("unchecked")
    public <T extends Context> T getContext() {
        return (T) this.context;
    }
}
