package com.wingsweaver.kuja.common.utils.support.lang;

import java.util.concurrent.TimeUnit;

/**
 * 线程工具类。
 *
 * @author wingsweaver
 */
public final class ThreadUtil {
    private ThreadUtil() {
        // 禁止实例化
    }

    /**
     * 休眠指定的时间。
     *
     * @param milliseconds 休眠的时间（毫秒）
     * @return 是否休眠成功
     */
    public static boolean sleep(long milliseconds) {
        long endTime = System.currentTimeMillis() + milliseconds;
        while (milliseconds >= 0) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            } catch (Exception ex) {
                // 忽略其他异常
            }

            // 计算下次所需的休眠时间
            milliseconds = endTime - System.currentTimeMillis();
        }

        // 返回
        return true;
    }

    /**
     * 休眠指定的时间。
     *
     * @param timeUnit 时间单位
     * @param time     休眠的时间
     * @return 是否休眠成功
     */
    public static boolean sleep(TimeUnit timeUnit, long time) {
        return sleep(timeUnit.toMillis(time));
    }
}
