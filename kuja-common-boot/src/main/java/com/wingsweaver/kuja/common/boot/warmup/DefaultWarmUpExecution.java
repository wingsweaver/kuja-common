package com.wingsweaver.kuja.common.boot.warmup;

import com.wingsweaver.kuja.common.utils.support.tostring.ToStringIgnored;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 默认的预热执行记录 {@link WarmUpExecution} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultWarmUpExecution implements WarmUpExecution {
    private static final WarmUpTask[] EMPTY_TASKS = new WarmUpTask[0];

    /**
     * 预热执行记录的 ID。
     */
    private final String id;

    /**
     * 创建时间。
     */
    private final long creationTime = System.currentTimeMillis();

    /**
     * 预热任务的列表。
     */
    private final WarmUpTask[] tasks;

    /**
     * 构造函数。
     *
     * @param id    预热执行记录的 ID
     * @param tasks 预热任务的列表
     */
    public DefaultWarmUpExecution(String id, WarmUpTask... tasks) {
        this.id = id;
        this.tasks = tasks;
    }

    /**
     * 构造函数。
     *
     * @param id    预热执行记录的 ID
     * @param tasks 预热任务的列表
     */
    public DefaultWarmUpExecution(String id, Collection<WarmUpTask> tasks) {
        this.id = id;
        if (CollectionUtils.isEmpty(tasks)) {
            this.tasks = EMPTY_TASKS;
        } else {
            this.tasks = tasks.toArray(new WarmUpTask[0]);
        }
    }

    @Override
    @ToStringIgnored
    public WarmUpTask[] getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return this.getClass().getTypeName() + "#" + this.getId();
    }
}
