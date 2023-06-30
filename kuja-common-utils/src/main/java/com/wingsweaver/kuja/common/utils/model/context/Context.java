package com.wingsweaver.kuja.common.utils.model.context;

import com.wingsweaver.kuja.common.utils.model.attributes.MutableAttributes;
import com.wingsweaver.kuja.common.utils.model.id.IdGetter;

/**
 * 上下文的接口定义。
 *
 * @author wingsweaver
 */
public interface Context extends MutableAttributes<String>, IdGetter<String> {
    /**
     * 获取创建时间（UTC）。
     *
     * @return 创建时间
     */
    long getCreationTime();
}
