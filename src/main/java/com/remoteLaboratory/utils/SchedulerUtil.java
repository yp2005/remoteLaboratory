package com.remoteLaboratory.utils;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerUtil {
    private static Scheduler scheduler = null;

    public static Scheduler getScheduler() throws SchedulerException {
        if (SchedulerUtil.scheduler == null) {
            // 通过SchedulerFactory获取一个调度器实例
            StdSchedulerFactory sf = new StdSchedulerFactory();
            // 代表一个Quartz的独立运行容器
            SchedulerUtil.scheduler = sf.getScheduler();
            SchedulerUtil.scheduler.start();
        }
        return SchedulerUtil.scheduler;
    }
}
