package com.remoteLaboratory.utils;

import com.remoteLaboratory.entities.SysSetting;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.SysSettingRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * 每天早上和晚上8点10分对所有在场内的车辆进行费用计算
 */
public class LogCleanJob implements Job {
    private static Logger log = LoggerFactory.getLogger(LogCleanUtil.class);


    private LogRecordRepository logRecordRepository;

    private SysSettingRepository sysSettingRepository;

    public LogCleanJob() {
        super();
        this.logRecordRepository = SpringUtil.getBean(LogRecordRepository.class);
        this.sysSettingRepository = SpringUtil.getBean(SysSettingRepository.class);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("start clean log...");
        SysSetting sysSetting = this.sysSettingRepository.findByKeyName(Constants.LOG_RETAIN_TIME);
        Integer logRetainTime = Integer.valueOf(sysSetting.getValue());
        logRetainTime = logRetainTime - logRetainTime * 2;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, logRetainTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        this.logRecordRepository.deleteByTime(calendar.getTime());
    }
}