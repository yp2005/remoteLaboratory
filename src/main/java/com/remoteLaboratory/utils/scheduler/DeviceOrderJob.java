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

    private SysSettingRepository sysSettingRepository;

    private DeviceRepository deviceRepository;

    private DeviceOrderRepository deviceOrderRepository;

    public DeviceOrderJob() {
        super();
        this.sysSettingRepository = SpringUtil.getBean(SysSettingRepository.class);
        this.deviceRepository = SpringUtil.getBean(DeviceRepository.class);
        this.deviceOrderRepository = SpringUtil.getBean(DeviceOrderRepository.class);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("start generate device order...");
        List<Device> deviceList = this.deviceRepository.findOnlineDevice();
        if(CollectionUtils.isEmpty(deviceList)) {
            return;
        }
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
        calendar.set(Calendar.MILLISECOND, 0);
        for(int i = 0; i < deviceOrderTimeValue; i++) {
            if(deviceOpenWeekendValue.equals(0) && DateTimeUtil.isWeekend(calendar.getTime())) {
                continue;
            }
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            DeviceOrder d = this.deviceOrderRepository.findFirstByYearAndMonthAndDay(year, month, day);
            if(d == null) {
                if(CollectionUtils.isNotEmpty(deviceList)) {
                    for(Device device : deviceList) {
                        calendar.set(Calendar.HOUR_OF_DAY, deviceOpenTimeStartValue);
                        int j = deviceOpenTimeStartValue;
                        while (true) {
                            DeviceOrder deviceOrder = new DeviceOrder();
                            deviceOrder.setStartHour(j);
                            j += device.getDuration();
                            if(j > deviceOpenTimeEndValue) break;
                            deviceOrder.setEndHour(j);
                            deviceOrder.setYear(year);
                            deviceOrder.setMonth(month);
                            deviceOrder.setDay(day);
                            deviceOrder.setDeviceId(device.getId());
                            deviceOrder.setDeviceName(device.getName());
                            deviceOrder.setStartTime(calendar.getTime());
                            deviceOrder.setStatus(0);
                            calendar.set(Calendar.HOUR_OF_DAY, j);
                            deviceOrder.setEndTime(calendar.getTime());
                            deviceOrder = this.deviceOrderRepository.save(deviceOrder);
                        }
                    }
                }
            }
            calendar.add(Calendar.DATE, 1);
        }
    }

    public void execute() {
        log.info("start generate device order...");
        List<Device> deviceList = this.deviceRepository.findOnlineDevice();
        if(CollectionUtils.isEmpty(deviceList)) {
            return;
        }
        log.info("device number:" + deviceList.size());
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
        calendar.set(Calendar.MILLISECOND, 0);
        log.info("deviceOrderTime:" + deviceOrderTime);
        log.info("deviceOpenWeekend:" + deviceOpenWeekend);
        for(int i = 0; i < deviceOrderTimeValue; i++) {
            if(deviceOpenWeekendValue.equals(0) && DateTimeUtil.isWeekend(calendar.getTime())) {
                continue;
            }
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            log.info("day:" + year + "-" + month + "-" + day);
            DeviceOrder d = this.deviceOrderRepository.findFirstByYearAndMonthAndDay(year, month, day);
            if(d == null) {
                if(CollectionUtils.isNotEmpty(deviceList)) {
                    for(Device device : deviceList) {
                        calendar.set(Calendar.HOUR_OF_DAY, deviceOpenTimeStartValue);
                        int j = deviceOpenTimeStartValue;
                        while (true) {
                            DeviceOrder deviceOrder = new DeviceOrder();
                            deviceOrder.setStartHour(j);
                            j += device.getDuration();
                            if(j > deviceOpenTimeEndValue) break;
                            deviceOrder.setEndHour(j);
                            deviceOrder.setYear(year);
                            deviceOrder.setMonth(month);
                            deviceOrder.setDay(day);
                            deviceOrder.setDeviceId(device.getId());
                            deviceOrder.setDeviceName(device.getName());
                            deviceOrder.setStartTime(calendar.getTime());
                            deviceOrder.setStatus(0);
                            calendar.set(Calendar.HOUR_OF_DAY, j);
                            deviceOrder.setEndTime(calendar.getTime());
                            deviceOrder = this.deviceOrderRepository.save(deviceOrder);
                        }
                    }
                }
            }
            calendar.add(Calendar.DATE, 1);
        }
    }

    public void execute(Integer deviceId) {
        log.info("start generate device order...");
        Device device = this.deviceRepository.findOne(deviceId);
        if(device == null) {
            return;
        }
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
        calendar.set(Calendar.MILLISECOND, 0);
        for(int i = 0; i < deviceOrderTimeValue; i++) {
            if(deviceOpenWeekendValue.equals(0) && DateTimeUtil.isWeekend(calendar.getTime())) {
                continue;
            }
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            DeviceOrder d = this.deviceOrderRepository.findFirstByDeviceIdAndYearAndMonthAndDay(deviceId, year, month, day);
            if(d == null) {
                calendar.set(Calendar.HOUR_OF_DAY, deviceOpenTimeStartValue);
                int j = deviceOpenTimeStartValue;
                while (true) {
                    DeviceOrder deviceOrder = new DeviceOrder();
                    deviceOrder.setStartHour(j);
                    j += device.getDuration();
                    if(j > deviceOpenTimeEndValue) break;
                    deviceOrder.setEndHour(j);
                    deviceOrder.setYear(year);
                    deviceOrder.setMonth(month);
                    deviceOrder.setDay(day);
                    deviceOrder.setDeviceId(device.getId());
                    deviceOrder.setDeviceName(device.getName());
                    deviceOrder.setStartTime(calendar.getTime());
                    deviceOrder.setStatus(0);
                    calendar.set(Calendar.HOUR_OF_DAY, j);
                    deviceOrder.setEndTime(calendar.getTime());
                    deviceOrder = this.deviceOrderRepository.save(deviceOrder);
                }
            }
            calendar.add(Calendar.DATE, 1);
        }
    }
}