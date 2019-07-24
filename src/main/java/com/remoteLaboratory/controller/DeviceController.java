package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Device;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.DeviceService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.BindCameraInput;
import com.remoteLaboratory.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 设备接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/device")
@Api(description = "设备")
@LoginRequired
public class DeviceController {

    private static Logger log = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "设备列表", notes = "查询设备信息列表")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(deviceService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "设备", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加设备", notes = "添加设备信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse add(@Validated({Device.Validation.class}) @RequestBody Device device, @ApiIgnore User loginUser) throws BusinessException {
        device = deviceService.add(device);
        CommonResponse commonResponse = CommonResponse.getInstance(device);
        LogUtil.add(this.logRecordRepository, "添加", "设备", loginUser, device.getId(), device.getName());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改设备", notes = "修改设备信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse update(@Validated({Device.Validation.class}) @RequestBody Device device, @ApiIgnore User loginUser) throws BusinessException {
        device = deviceService.update(device);
        CommonResponse commonResponse = CommonResponse.getInstance(device);
        LogUtil.add(this.logRecordRepository, "修改", "设备", loginUser, device.getId(), device.getName());
        return commonResponse;
    }

    @PutMapping(path = "/bindCamera")
    @ApiOperation(value = "绑定摄像头", notes = "绑定摄像头接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse bindCamera(@Validated({Device.Validation.class}) @RequestBody BindCameraInput bindCameraInput, @ApiIgnore User loginUser) throws BusinessException {
        Device device = deviceService.bindCamera(bindCameraInput);
        CommonResponse commonResponse = CommonResponse.getInstance(device);
        LogUtil.add(this.logRecordRepository, "绑定摄像头", "设备", loginUser, device.getId(), device.getName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除设备", notes = "删除设备信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse delete(@NotNull(message = "设备编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        deviceService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询设备", notes = "根据ID查询设备")
    public CommonResponse get(@NotNull(message = "设备编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Device device = deviceService.get(id);
        // 学生不能直接获取在线实验设备的详情
        if(device.getType().equals(1) && loginUser.getUserType().equals(Constants.USER_TYPE_STUDENT)) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CommonResponse commonResponse = CommonResponse.getInstance(device);
        LogUtil.add(this.logRecordRepository, "查询", "设备", loginUser, device.getId(), device.getName());
        return commonResponse;
    }
}
