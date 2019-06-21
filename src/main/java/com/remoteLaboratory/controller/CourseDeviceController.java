package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.CourseDevice;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseDeviceService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
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
 * 课程设备接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/courseDevice")
@Api(description = "课程设备")
@LoginRequired
public class CourseDeviceController {

    private static Logger log = LoggerFactory.getLogger(CourseDeviceController.class);

    @Autowired
    private CourseDeviceService courseDeviceService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "课程设备列表", notes = "查询课程设备信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(courseDeviceService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "课程设备", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加课程设备", notes = "添加课程设备信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse add(@Validated({CourseDevice.Validation.class}) @RequestBody CourseDevice courseDevice, @ApiIgnore User loginUser) throws BusinessException {
        courseDevice = courseDeviceService.add(courseDevice);
        CommonResponse commonResponse = CommonResponse.getInstance(courseDevice);
        LogUtil.add(this.logRecordRepository, "添加", "课程设备", loginUser, courseDevice.getId(), courseDevice.getCourseName() + "->" + courseDevice.getDeviceName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除课程设备", notes = "删除课程设备信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse delete(@NotNull(message = "课程设备编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        courseDeviceService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询课程设备", notes = "根据ID查询课程设备")
    public CommonResponse get(@NotNull(message = "课程设备编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        CourseDevice courseDevice = courseDeviceService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(courseDevice);
        LogUtil.add(this.logRecordRepository, "查询", "课程设备", loginUser, courseDevice.getId(), courseDevice.getCourseName() + "->" + courseDevice.getDeviceName());
        return commonResponse;
    }
}
