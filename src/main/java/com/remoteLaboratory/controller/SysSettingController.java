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
@LoginRequired(adminRequired = "1")
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
    }

    @PostMapping(path = "/list")
    @ApiOperation(value = "系统设置列表", notes = "查询系统设置信息列表")
    public CommonResponse list(@RequestBody ListInput listInput) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.list(listInput));
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询系统设置", notes = "根据ID查询系统设置接口")
    public CommonResponse get(@NotNull(message = "系统设置编号不能为空") @PathVariable String id) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.get(id));
        return commonResponse;
    }

    @GetMapping(path = "/getLogRetainTimeSetting")
    @ApiOperation(value = "查询日志保留时间设置", notes = "查询日志保留时间设置接口")
    public CommonResponse getLogRetainTimeSetting() throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sysSettingService.getByKeyName(Constants.LOG_RETAIN_TIME));
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "更新系统设置", notes = "更新系统设置接口")
    public CommonResponse update(@RequestBody SysSettingInput sysSettingInput) throws BusinessException {
        if(sysSettingInput.getKeyName().equals(Constants.LOG_RETAIN_TIME)) {
            try {
                Integer.valueOf(sysSettingInput.getValue());
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(Messages.CODE_40010, "值为正整数，单位: 天");
            }
        }
        SysSetting sysSetting = this.sysSettingService.getByKeyName(sysSettingInput.getKeyName());
        sysSetting.setValue(sysSettingInput.getValue());
        return CommonResponse.getInstance(this.sysSettingService.update(sysSetting));
    }
}
