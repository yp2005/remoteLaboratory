package com.remoteLaboratory.utils.scheduler;

import com.remoteLaboratory.entities.SysSetting;
import com.remoteLaboratory.repositories.SysSettingRepository;
import com.remoteLaboratory.service.LogRecordService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.SpringUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * 日志清理
 */
public class LogCleanJob implements Job {
    private static Logger log = LoggerFactory.getLogger(LogCleanJob.class);

    private LogRecordService logRecordService;

    private SysSettingRepository sysSettingRepository;

    public LogCleanJob() {
        super();
        this.logRecordService = SpringUtil.getBean(LogRecordService.class);
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
        try {
            this.logRecordService.deleteByTime(calendar.getTime());
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }
}