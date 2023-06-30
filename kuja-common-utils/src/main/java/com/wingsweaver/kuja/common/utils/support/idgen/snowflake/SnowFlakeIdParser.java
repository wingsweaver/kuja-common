package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple3;

/**
 * 解析 SnowFlake ID 的接口。
 *
 * @author wingsweaver
 */
public interface SnowFlakeIdParser {
    /**
     * 解析 SnowFlake ID。
     *
     * @param id 生成的 ID
     * @return t1: 时间戳, t2: Worker ID, t3: 序列 ID
     */
    Tuple3<Long, Long, Long> parse(long id);
}
