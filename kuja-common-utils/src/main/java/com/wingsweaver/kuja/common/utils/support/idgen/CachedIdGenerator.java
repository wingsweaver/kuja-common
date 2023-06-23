package com.wingsweaver.kuja.common.utils.support.idgen;

import com.wingsweaver.kuja.common.utils.support.lang.ThreadUtil;
import lombok.Getter;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.BackOffExecution;

import java.util.Objects;

/**
 * 带缓存数据的 {@link IdGenerator} 实现类。<br>
 * 通过缓存一定数量的 ID，可以缓解时钟回拨等造成的 ID 重复问题。
 *
 * @param <T> 数据类型
 * @author wingsweaver
 */
@Getter
public class CachedIdGenerator<T> implements IdGenerator<T> {
    /**
     * 实际的 ID 生成器的实例。
     */
    private final IdGenerator<T> idGenerator;

    /**
     * 缓存大小。
     */

    private final int cacheSize;

    /**
     * 重试管理器。
     */
    private final BackOff backOff;

    /**
     * 缓存数组（环形使用）。
     */
    @Getter(value = lombok.AccessLevel.NONE)

    private final Object[] cachedIds;

    /**
     * 下一个可以更新缓存数据的下标。
     */
    @Getter(value = lombok.AccessLevel.NONE)
    private int nextIndex;

    public CachedIdGenerator(IdGenerator<T> idGenerator, int cacheSize, BackOff backOff) {
        // 检查参数
        Objects.requireNonNull(idGenerator, "idGenerator must not be null.");
        Objects.requireNonNull(backOff, "backOff must not be null.");
        if (cacheSize <= 0) {
            throw new IllegalArgumentException("Cache size must be greater than 0.");
        }

        // 初始化
        this.idGenerator = idGenerator;
        this.cacheSize = cacheSize;
        this.cachedIds = new Object[cacheSize];
        this.backOff = backOff;
    }

    @Override
    public T nextId() {
        BackOffExecution execution = this.backOff.start();
        while (true) {
            // 尝试计算一次 ID
            T id = this.getIdGenerator().nextId();
            if (this.checkAndCache(id)) {
                // 如果不在缓存中，那么直接返回
                return id;
            }

            // 等待下一次重试
            long backOffValue = execution.nextBackOff();
            if (backOffValue == BackOffExecution.STOP) {
                break;
            }
            if (!ThreadUtil.sleep(backOffValue)) {
                // 如果发生了线程中断等异常，那么结束循环
                break;
            }
        }

        // 返回 null
        return null;
    }

    /**
     * 检查并缓存指定的 ID。
     *
     * @param id 要检查并缓存的 ID
     * @return 是否缓存成功
     */
    protected synchronized boolean checkAndCache(T id) {
        // 检查在缓存中是否存在
        for (Object cachedId : this.cachedIds) {
            if (Objects.equals(id, cachedId)) {
                // 如果存在的话，直接返回 false
                return false;
            }
        }

        // 如果在缓存中不存在的话，那么添加到缓存中
        this.cachedIds[this.nextIndex] = id;

        // 更新下一个保存位置
        this.nextIndex++;
        if (this.nextIndex >= this.cacheSize) {
            this.nextIndex = 0;
        }

        // 返回
        return true;
    }
}
