package com.wingsweaver.kuja.common.utils.support.idgen.snowflake;

/**
 * 用于本地开发的、使用 PID 的 {@link WorkerIdResolver} 实现类。
 *
 * @author wingsweaver
 */
public class LocalPidWorkerIdResolver extends AbstractWorkerIdResolver {
    public LocalPidWorkerIdResolver(int bits) {
        super(bits);
        this.setTrimToMaskValue(true);
    }

    @Override
    protected int resolveWorkerIdInternal() {
        // 获取当前进程的 PID
        String name = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        String pidText = name.split("@")[0];
        return Integer.parseInt(pidText);
    }
}
