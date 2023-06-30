package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple3;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class SnowFlakeIdGeneratorTest {
    @Test
    void test() throws Exception {
        DefaultTimeStampGenerator timeStampGenerator = new DefaultTimeStampGenerator(41);
        Date start = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.parse("2020-01-01T00:00:00+08:00");
        timeStampGenerator.setStartEpoch(start.getTime());
        timeStampGenerator.setUnit(TimeStampUnit.MILLISECONDS);

        FixedWorkerIdResolver workerIdResolver = new FixedWorkerIdResolver(10);
        workerIdResolver.setWorkerId(987);

        DefaultSequenceIdGenerator sequenceIdGenerator = new DefaultSequenceIdGenerator(12);

        BackOff backOff = new FixedBackOff(100L, 10L);

        SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator();
        idGenerator.setTimeStampGenerator(timeStampGenerator);
        idGenerator.setWorkerIdResolver(workerIdResolver);
        idGenerator.setSequenceIdGenerator(sequenceIdGenerator);
        idGenerator.setBackOff(backOff);

        idGenerator.afterPropertiesSet();
        assertSame(timeStampGenerator, idGenerator.getTimeStampGenerator());
        assertSame(workerIdResolver, idGenerator.getWorkerIdResolver());
        assertSame(sequenceIdGenerator, idGenerator.getSequenceIdGenerator());
        assertSame(backOff, idGenerator.getBackOff());

        long id = idGenerator.nextId();
        System.out.println("id: " + id);

        Tuple3<Long, Long, Long> tuple3 = idGenerator.parse(id);
        System.out.println("timestamp: " + tuple3.getT1());
        assertNotNull(tuple3.getT1());
        System.out.println("workerId: " + tuple3.getT2());
        assertEquals(987, tuple3.getT2());
        System.out.println("sequenceId: " + tuple3.getT3());
        assertNotNull(tuple3.getT3());
    }

    @Test
    void test2() throws Exception {
        StaticApplicationContext applicationContext = new StaticApplicationContext();

        CustomTimeStampGenerator timeStampGenerator = new CustomTimeStampGenerator(41);
        long timestamp = System.currentTimeMillis() / 1000;
        {
            timeStampGenerator.setLastTimeStamp(timestamp);
            RootBeanDefinition beanDefinition = new RootBeanDefinition(TimeStampGenerator.class, () -> timeStampGenerator);
            applicationContext.registerBeanDefinition("timeStampGenerator", beanDefinition);
        }
        FixedWorkerIdResolver workerIdResolver = new FixedWorkerIdResolver(10);
        {
            workerIdResolver.setWorkerId(987);
            RootBeanDefinition beanDefinition = new RootBeanDefinition(WorkerIdResolver.class, () -> workerIdResolver);
            applicationContext.registerBeanDefinition("workerIdResolver", beanDefinition);
        }
        FixedSequenceIdGenerator sequenceIdGenerator = new FixedSequenceIdGenerator(12);
        {
            sequenceIdGenerator.setSequenceId(3456);
            RootBeanDefinition beanDefinition = new RootBeanDefinition(SequenceIdGenerator.class, () -> sequenceIdGenerator);
            applicationContext.registerBeanDefinition("sequenceIdGenerator", beanDefinition);
        }

        BackOff backOff = new FixedBackOff(10, 2L);
        SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator();
        idGenerator.setApplicationContext(applicationContext);
        idGenerator.setBackOff(backOff);
        idGenerator.afterPropertiesSet();

        assertSame(timeStampGenerator, idGenerator.getTimeStampGenerator());
        assertSame(workerIdResolver, idGenerator.getWorkerIdResolver());
        assertSame(sequenceIdGenerator, idGenerator.getSequenceIdGenerator());

        // 第 1 次可以生成
        long id = idGenerator.nextId();
        Tuple3<Long, Long, Long> tuple3 = idGenerator.parse(id);
        assertEquals(timestamp, tuple3.getT1());
        assertEquals(987, tuple3.getT2());
        assertEquals(3456, tuple3.getT3());

        // 第 2 次无法生成（TimeStamp 尚未更新）
        assertNull(idGenerator.nextId());

        // 第 3 次可以生成（TimeStamp 更新）
        id = idGenerator.nextId();
        tuple3 = idGenerator.parse(id);
        assertEquals(timestamp + 1, tuple3.getT1());
        assertEquals(987, tuple3.getT2());
        assertEquals(3456, tuple3.getT3());
    }

    @Getter
    @Setter
    static class CustomTimeStampGenerator extends AbstractPartGenerator implements TimeStampGenerator {
        private long lastTimeStamp;

        private int lastIndex = 0;

        private int updateTimeStampCount = 5;


        public CustomTimeStampGenerator(int bits) {
            super(bits);
        }

        @Override
        public long toTimeStamp(long epoch) {
            if (this.lastIndex >= this.updateTimeStampCount) {
                this.lastIndex = 0;
                this.lastTimeStamp += 1;
            } else {
                this.lastIndex++;
            }
            return this.lastTimeStamp;
        }

        @Override
        public long toEpoch(long timestamp) {
            return timestamp;
        }
    }

    @Getter
    @Setter
    static class FixedSequenceIdGenerator extends AbstractPartGenerator implements SequenceIdGenerator {
        private long lastTimestamp;

        private long sequenceId;

        public FixedSequenceIdGenerator(int bits) {
            super(bits);
        }

        @Override
        public Long nextSequenceId(long timestamp) {
            if (this.lastTimestamp != timestamp) {
                this.lastTimestamp = timestamp;
                return this.sequenceId;
            } else {
                return NO_MORE_SEQUENCE_ID;
            }
        }
    }
}