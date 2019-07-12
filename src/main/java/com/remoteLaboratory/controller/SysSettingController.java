package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.SysSetting;
import com.remoteLaboratory.repositories.SysSettingRepository;
import com.remoteLaboratory.service.SysSettingService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.SysSettingInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * 系统设置rest接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/sysSetting")
@Api(description = "系统设置")
public class SysSettingController {

    private static Logger log = LoggerFactory.getLogger(SysSettingController.class);

    private SysSettingService sysSettingService;

    private SysSettingRepository sysSettingRepository;

    @Autowired
    public SysSettingController(SysSettingService sysSettingService, SysSettingRepository sysSettingRepository) {
        this.sysSettingService = sysSettingService;
        this.sysSettingRepository = sysSettingRepository;
    }

    @PostConstruct
    private void initSysSetting() throws Exception {
        SysSetting logRetainTime = this.sysSettingRepository.findByKeyName(Constants.LOG_RETAIN_TIME);
        if (logRetainTime == null) {
            logRetainTime = new SysSetting();
            logRetainTime.setZhName("日志保留时间");
            logRetainTime.setKeyName(Constants.LOG_RETAIN_TIME);
            logRetainTime.setValue("90");
            logRetainTime.setDescription("值为正整数，单位: 天");
            this.sysSettingRepository.save(logRetainTime);
        }

        SysSetting deviceOrderTime = this.sysSettingRepository.findByKeyName(Constants.DEVICE_ORDER_TIME);
        if (deviceOrderTime == null) {
            deviceOrderTime = new SysSetting();
            deviceOrderTime.setZhName("设备预约可提前天数");
            deviceOrderTime.setKeyName(Constants.DEVICE_ORDER_TIME);
            deviceOrderTime.setValue("5");
            deviceOrderTime.setDescription("值为正整数，单位: 天");
            this.sysSettingRepository.save(deviceOrderTime);
        }

        SysSetting deviceOpenTimeStart = this.sysSettingRepository.findByKeyName(Constants.DEVICE_OPEN_TIME_START);
        if (deviceOpenTimeStart == null) {
            deviceOpenTimeStart = new SysSetting();
            deviceOpenTimeStart.setZhName("设备开放时间-开始时间");
            deviceOpenTimeStart.setKeyName(Constants.DEVICE_OPEN_TIME_START);
            deviceOpenTimeStart.setValue("9");
            deviceOpenTimeStart.setDescription("值为整点小时");
            this.sysSettingRepository.save(deviceOpenTimeStart);
        }

        SysSetting deviceOpenTimeEnd = this.sysSettingRepository.findByKeyName(Constants.DEVICE_OPEN_TIME_END);
        if (deviceOpenTimeEnd == null) {
            deviceOpenTimeEnd = new SysSetting();
            deviceOpenTimeEnd.setZhName("设备开放时间-结束时间");
            deviceOpenTimeEnd.setKeyName(Constants.DEVICE_OPEN_TIME_END);
            deviceOpenTimeEnd.setValue("18");
            deviceOpenTimeEnd.setDescription("值为整点小时");
            this.sysSettingRepository.save(deviceOpenTimeEnd);
        }

        SysSetting deviceOpenWeekend = this.sysSettingRepository.findByKeyName(Constants.DEVICE_OPEN_WEEKEND);
        if (deviceOpenWeekend == null) {
            deviceOpenWeekend = new SysSetting();
            deviceOpenWeekend.setZhName("设备周末是否开放");
            deviceOpenWeekend.setKeyName(Constants.DEVICE_OPEN_WEEKEND);
            deviceOpenWeekend.setValue("1");
            deviceOpenWeekend.setDescription("0-不开放 1-开放");
            this.sysSettingRepository.save(deviceOpenWeekend);
        }

        SysSetting homePageImages = this.sysSettingRepository.findByKeyName(Constants.HOME_PAGE_IMAGES);
        if (homePageImages == null) {
            homePageImages = new SysSetting();
            homePageImages.setZhName("首页轮播图片");
            homePageImages.setKeyName(Constants.HOME_PAGE_IMAGES);
            homePageImages.setValue("[]");
            homePageImages.setDescription("图片url的json数组字符串");
            this.sysSettingRepository.save(homePageImages);
        }

    }

    @PostMapping(path = "/list")
    @ApiOperation(value = "系统设置列表", notes = "查询系统设置信息列表")
    @LoginRequired(adminRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.list(listInput));
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询系统设置", notes = "根据ID查询系统设置接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse get(@NotNull(message = "系统设置编号不能为空") @PathVariable String id) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.get(id));
        return commonResponse;
    }

    @GetMapping(path = "/getLogRetainTimeSetting")
    @ApiOperation(value = "查询日志保留时间设置", notes = "查询日志保留时间设置接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse getLogRetainTimeSetting() throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.getByKeyName(Constants.LOG_RETAIN_TIME));
        return commonResponse;
    }

    @GetMapping(path = "/getDeviceOrderTime")
    @ApiOperation(value = "查询设备预约可提前天数设置", notes = "查询设备预约可提前天数设置接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse getDeviceOrderTime() throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.getByKeyName(Constants.DEVICE_ORDER_TIME));
        return commonResponse;
    }

    @GetMapping(path = "/getDeviceOpenTimeStart")
    @ApiOperation(value = "查询设备开放时间-开始时间设置", notes = "查询设备开放时间-开始时间设置接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse getDeviceOpenTimeStart() throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.getByKeyName(Constants.DEVICE_OPEN_TIME_START));
        return commonResponse;
    }

    @GetMapping(path = "/getDeviceOpenTimeEnd")
    @ApiOperation(value = "查询设备开放时间-结束时间设置", notes = "查询设备开放时间-结束时间设置接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse getDeviceOpenTimeEnd() throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.getByKeyName(Constants.DEVICE_OPEN_TIME_END));
        return commonResponse;
    }

    @GetMapping(path = "/getDeviceOpenWeekend")
    @ApiOperation(value = "查询设备周末是否开放设置", notes = "查询设备周末是否开放设置接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse getDeviceOpenWeekend() throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.getByKeyName(Constants.DEVICE_OPEN_WEEKEND));
        return commonResponse;
    }

    @GetMapping(path = "/getHomePageImages")
    @ApiOperation(value = "查询首页轮播图片", notes = "查询首页轮播图片接口")
    public CommonResponse getHomePageImages() throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.getByKeyName(Constants.HOME_PAGE_IMAGES));
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "更新系统设置", notes = "更新系统设置接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse update(@RequestBody SysSettingInput sysSettingInput) throws BusinessException {
        SysSetting sysSetting = this.sysSettingService.getByKeyName(sysSettingInput.getKeyName());
        if(sysSettingInput.getKeyName().equals(Constants.LOG_RETAIN_TIME)
                || sysSettingInput.getKeyName().equals(Constants.DEVICE_OPEN_TIME_START)
                || sysSettingInput.getKeyName().equals(Constants.DEVICE_OPEN_TIME_END)
                || sysSettingInput.getKeyName().equals(Constants.DEVICE_ORDER_TIME)) {
            try {
                Integer.valueOf(sysSettingInput.getValue());
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(Messages.CODE_40010, sysSetting.getDescription());
            }
        }
        else if(sysSettingInput.getKeyName().equals(Constants.DEVICE_OPEN_WEEKEND)) {
            if(!sysSettingInput.getValue().equals("0") && !sysSettingInput.getValue().equals("1")) {
                throw new BusinessException(Messages.CODE_40010, sysSetting.getDescription());
            }
        }
        sysSetting.setValue(sysSettingInput.getValue());
        return CommonResponse.getInstance(this.sysSettingService.update(sysSetting));
    }
}
