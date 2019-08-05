package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Device;
import com.remoteLaboratory.entities.DeviceOrder;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CameraService;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.DeviceOrderService;
import com.remoteLaboratory.service.DeviceService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.DeviceOutputVo;
import com.remoteLaboratory.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;

/**
 * 设备预约接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/deviceOrder")
@Api(description = "设备预约")
@LoginRequired
public class DeviceOrderController {

    private static Logger log = LoggerFactory.getLogger(DeviceOrderController.class);

    @Autowired
    private DeviceOrderService deviceOrderService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private CameraService cameraService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "设备预约列表", notes = "查询设备预约信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(deviceOrderService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "设备预约", loginUser, null, null);
        return commonResponse;
    }

    @GetMapping(path = "/getMy")
    @ApiOperation(value = "查询我的设备预约(未完成的)", notes = "查询我的设备预约接口(未完成的)")
    public CommonResponse get(@ApiIgnore User loginUser) throws BusinessException {
        DeviceOrder deviceOrder = deviceOrderService.getByUserId(loginUser.getId());
        CommonResponse commonResponse = CommonResponse.getInstance(deviceOrder);
        LogUtil.add(this.logRecordRepository, "查询", "设备预约", loginUser, deviceOrder.getId(), deviceOrder.getUserName() + ": " + deviceOrder.getDeviceName());
        return commonResponse;
    }

    @GetMapping(path = "/getOrderDeviceDetail/{id}")
    @ApiOperation(value = "获取预约设备详情", notes = "获取预约设备详情接口")
    public CommonResponse getOrderDeviceDetail(@NotNull(message = "预约ID不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        DeviceOrder deviceOrder = deviceOrderService.get(id);
        if(!deviceOrder.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        if(deviceOrder.getStartTime().getTime() < System.currentTimeMillis() && deviceOrder.getEndTime().getTime() > System.currentTimeMillis()) {
            Device device = this.deviceService.get(deviceOrder.getDeviceId());
            DeviceOutputVo deviceOutputVo = new DeviceOutputVo(device);
            if(device.getCameraId() != null) {
                deviceOutputVo.setCamera(this.cameraService.get(device.getCameraId()));
            }
            commonResponse.setResult(deviceOutputVo);
        }
        else {
            throw new BusinessException(Messages.CODE_40010, "您的预约还未到时间或已过期");
        }
        LogUtil.add(this.logRecordRepository, "查询", "设备预约", loginUser, deviceOrder.getId(), deviceOrder.getUserName() + ": " + deviceOrder.getDeviceName());
        return commonResponse;
    }

    @PostMapping(path = "/order/{id}")
    @ApiOperation(value = "预约设备", notes = "预约设备接口")
    public CommonResponse order(@NotNull(message = "预约ID不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        DeviceOrder deviceOrder = deviceOrderService.order(id, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance(deviceOrder);
        LogUtil.add(this.logRecordRepository, "预约设备", "设备预约", loginUser, deviceOrder.getId(), deviceOrder.getUserName() + ": " + deviceOrder.getDeviceName());
        return commonResponse;
    }
}
