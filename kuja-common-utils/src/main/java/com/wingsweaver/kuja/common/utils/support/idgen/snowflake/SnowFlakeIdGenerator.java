package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple3;
import com.wingsweaver.kuja.common.utils.support.idgen.LongIdGenerator;
import com.wingsweaver.kuja.common.utils.support.lang.ThreadUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.BackOffExecution;

/**
 * 基于 SnowFlake 思路的 {@link LongIdGenerator} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class SnowFlakeIdGenerator extends AbstractComponent implements LongIdGenerator, SnowFlakeIdParser {
    /**
     * 时间戳生成器。
     */
    private TimeStampGenerator timeStampGenerator;

    /**
     * 工作节点 ID 生成器。
     */
    private WorkerIdResolver workerIdResolver;

    /**
     * 序列 ID 生成器。
     */
    private SequenceIdGenerator sequenceIdGenerator;

    /**
     * 重试生成 ID 时的 BackOff。
     */
    private BackOff backOff;

    /**
     * 解析生成的 ID，得到生成时的时间戳、Worker ID 和序列 ID。
     *
     * @param id 生成的 ID
     * @return t1: 时间戳; t2: Worker ID; t3: 序列 ID
     */
    @Override
    public Tuple3<Long, Long, Long> parse(long id) {
        long sequenceId = (id & this.sequenceIdGenerator.maxValue());
        id = (id >> this.sequenceIdGenerator.bits());
        long workerId = (id & this.workerIdResolver.maxValue());
        id = (id >> this.workerIdResolver.bits());
        long timestamp = (id & this.timeStampGenerator.maxValue());
        long epochUtc = this.timeStampGenerator.toEpoch(timestamp);
        return Tuple3.of(epochUtc, workerId, sequenceId);
    }

    @Override
    public Long nextId() {
        BackOffExecution execution = this.backOff.start();

        while (true) {
            // 生成一个 TimesStamp
            long timestamp = this.timeStampGenerator.toTimeStamp(System.currentTimeMillis());

            // 生成一个序列 ID
            Long sequenceId = this.sequenceIdGenerator.nextSequenceId(timestamp);

            // 如果生成的序列 ID 有效，那么生成 ID 并且返回
            if (sequenceId != null) {
                return this.createId(timestamp, sequenceId);
            }

            // 否则的话，等待一段时间后重试
            long nextBackOff = execution.nextBackOff();
            if (nextBackOff < 0) {
                return null;
            }
            ThreadUtil.sleep(nextBackOff);
        }
    }

    /**
     * 基于时间戳、Worker ID 和序列 ID 生成 ID。
     *
     * @param timestamp  时间戳
     * @param sequenceId 序列 ID
     * @return ID
     */
    private long createId(long timestamp, long sequenceId) {
        long workerId = this.workerIdResolver.resolveWorkerId();
        return sequenceId
                + (workerId << this.sequenceIdGenerator.bits())
                + (timestamp << (this.sequenceIdGenerator.bits() + this.workerIdResolver.bits()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 TimeStampGenerator
        this.initTimeStampGenerator();

        // 初始化 WorkerIdResolver
        this.initWorkerIdResolver();

        // 初始化 SequenceIdGenerator
        this.initSequenceIdGenerator();

        // 检查各部分的 BIT 值是否合法
        int totalBits = this.timeStampGenerator.bits() + this.workerIdResolver.bits() + this.sequenceIdGenerator.bits();
        AssertState.isTrue(totalBits <= MAX_ID_BITS, () -> "The total bits of ID must be less than or equal to " + MAX_ID_BITS
                + ", actual value is " + totalBits);
    }

    /**
     * 初始化 {@link SequenceIdGenerator}。
     */
    protected void initSequenceIdGenerator() {
        if (this.sequenceIdGenerator == null) {
            this.sequenceIdGenerator = this.getBean(SequenceIdGenerator.class);
        }
    }

    /**
     * 初始化 {@link WorkerIdResolver}。
     */
    protected void initWorkerIdResolver() {
        if (this.workerIdResolver == null) {
            this.workerIdResolver = this.getBean(WorkerIdResolver.class);
        }
    }

    /**
     * 初始化 {@link TimeStampGenerator}。
     */
    protected void initTimeStampGenerator() {
        if (this.timeStampGenerator == null) {
            this.timeStampGenerator = this.getBean(TimeStampGenerator.class);
        }
    }
}
