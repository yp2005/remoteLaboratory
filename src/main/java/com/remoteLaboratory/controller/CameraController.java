package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Camera;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CameraService;
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
 * 摄像头接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/camera")
@Api(description = "摄像头")
@LoginRequired(adminRequired = "1")
public class CameraController {

    private static Logger log = LoggerFactory.getLogger(CameraController.class);

    @Autowired
    private CameraService cameraService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "摄像头列表", notes = "查询摄像头信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(cameraService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "摄像头", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加摄像头", notes = "添加摄像头信息接口")
    public CommonResponse add(@Validated({Camera.Validation.class}) @RequestBody Camera camera, @ApiIgnore User loginUser) throws BusinessException {
        camera = cameraService.add(camera);
        CommonResponse commonResponse = CommonResponse.getInstance(camera);
        LogUtil.add(this.logRecordRepository, "添加", "摄像头", loginUser, camera.getId(), camera.getName());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改摄像头", notes = "修改摄像头信息接口")
    public CommonResponse update(@Validated({Camera.Validation.class}) @RequestBody Camera camera, @ApiIgnore User loginUser) throws BusinessException {
        camera = cameraService.update(camera);
        CommonResponse commonResponse = CommonResponse.getInstance(camera);
        LogUtil.add(this.logRecordRepository, "修改", "摄像头", loginUser, camera.getId(), camera.getName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除摄像头", notes = "删除摄像头信息接口")
    public CommonResponse delete(@NotNull(message = "摄像头编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        cameraService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询摄像头", notes = "根据ID查询摄像头")
    public CommonResponse get(@NotNull(message = "摄像头编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Camera camera = cameraService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(camera);
        LogUtil.add(this.logRecordRepository, "查询", "摄像头", loginUser, camera.getId(), camera.getName());
        return commonResponse;
    }
}
