package com.remoteLaboratory.utils.scheduler;

import com.remoteLaboratory.entities.Device;
import com.remoteLaboratory.entities.DeviceOrder;
import com.remoteLaboratory.entities.SysSetting;
import com.remoteLaboratory.repositories.DeviceOrderRepository;
import com.remoteLaboratory.repositories.DeviceRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.SysSettingRepository;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.SpringUtil;
import com.remoteLaboratory.utils.date.DateTimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

/**
 * 生成设备预约
 */
public class DeviceOrderJob implements Job {
    private static Logger log = LoggerFactory.getLogger(LogCleanUtil.class);

    private LogRecordRepository logRecordRepository;

    private SysSettingRepository sysSettingRepository;

    private DeviceRepository deviceRepository;

    private DeviceOrderRepository deviceOrderRepository;

    public DeviceOrderJob() {
        super();
        this.logRecordRepository = SpringUtil.getBean(LogRecordRepository.class);
        this.sysSettingRepository = SpringUtil.getBean(SysSettingRepository.class);
        this.deviceRepository = SpringUtil.getBean(DeviceRepository.class);
        this.deviceOrderRepository = SpringUtil.getBean(DeviceOrderRepository.class);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("start generate device order...");
        SysSetting deviceOrderTime = this.sysSettingRepository.findByKeyName(Constants.DEVICE_ORDER_TIME);
        Integer deviceOrderTimeValue = Integer.valueOf(deviceOrderTime.getValue());
        SysSetting deviceOpenTimeStart = this.sysSettingRepository.findByKeyName(Constants.DEVICE_OPEN_TIME_START);
        Integer deviceOpenTimeStartValue = Integer.valueOf(deviceOpenTimeStart.getValue());
        SysSetting deviceOpenTimeEnd = this.sysSettingRepository.findByKeyName(Constants.DEVICE_OPEN_TIME_END);
        Integer deviceOpenTimeEndValue = Integer.valueOf(deviceOpenTimeEnd.getValue());
        SysSetting deviceOpenWeekend = this.sysSettingRepository.findByKeyName(Constants.DEVICE_OPEN_WEEKEND);
        Integer deviceOpenWeekendValue = Integer.valueOf(deviceOpenWeekend.getValue());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        for(int i = 1; i < deviceOrderTimeValue; i++) {
            if(deviceOpenWeekendValue.equals(0) && DateTimeUtil.isWeekend(calendar.getTime())) {
                continue;
            }
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            DeviceOrder d = this.deviceOrderRepository.findFirstByYearAndMonthAndDay(year, month, day);
            if(d == null) {
                List<Device> deviceList = this.deviceRepository.findOnlineDevice();
                if(CollectionUtils.isNotEmpty(deviceList)) {
                    for(Device device : deviceList) {
                        calendar.set(Calendar.HOUR_OF_DAY, deviceOpenTimeStartValue);
                        for(int j = deviceOpenTimeStartValue; j < deviceOpenTimeEndValue; j++) {
                            DeviceOrder deviceOrder = new DeviceOrder();
                            deviceOrder.setYear(year);
                            deviceOrder.setMonth(month);
                            deviceOrder.setDay(day);
                            deviceOrder.setHour(j);
                            deviceOrder.setDeviceId(device.getId());
                            deviceOrder.setDeviceName(device.getName());
                            deviceOrder.setStartTime(calendar.getTime());
                            deviceOrder.setStatus(0);
                            calendar.set(Calendar.HOUR_OF_DAY, j + 1);
                            deviceOrder.setEndTime(calendar.getTime());
                            deviceOrder = this.deviceOrderRepository.save(deviceOrder);
                        }
                    }
                }
            }
        }
    }
}