package com.remoteLaboratory.utils.scheduler;

import com.remoteLaboratory.utils.date.DateTimeUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

public class LogCleanUtil {
    private static Logger log = LoggerFactory.getLogger(LogCleanUtil.class);

    public LogCleanUtil() {
    }

    public void execute() {
        // 创建一个JobDetail实例 并指定Job在Scheduler中所属组及名称
        JobDetail jobDetail = JobBuilder.newJob(LogCleanJob.class).withIdentity("jobLogClean", "logClean").build();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // 每天晚上3点执行日志清除
        SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("triggerLogClean", "logClean").
                startAt(calendar.getTime())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24).repeatForever()).build();
        // 添加JobDetail到Scheduler容器中，并且和Trigger进行关联，返回执行时间
        try {
            if (SchedulerUtil.getScheduler().checkExists(jobDetail.getKey())) { // 如果已存在，删除
                SchedulerUtil.getScheduler().deleteJob(jobDetail.getKey());
            }
            Date time = SchedulerUtil.getScheduler().scheduleJob(jobDetail, simpleTrigger);
            log.info("start execut at: " + DateTimeUtil.formatDate(time));
        } catch (SchedulerException e1) {
            e1.printStackTrace();
        }
    }
}
