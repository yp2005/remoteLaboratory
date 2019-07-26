package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.SignalChannel;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.SignalChannelService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
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
 * 信号通道接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/signalChannel")
@Api(description = "信号通道")
@LoginRequired
public class SignalChannelController {

    private static Logger log = LoggerFactory.getLogger(SignalChannelController.class);

    @Autowired
    private SignalChannelService signalChannelService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "信号通道列表", notes = "查询信号通道信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(signalChannelService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "信号通道", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加信号通道", notes = "添加信号通道信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse add(@Validated({SignalChannel.Validation.class}) @RequestBody SignalChannel signalChannel, @ApiIgnore User loginUser) throws BusinessException {
        signalChannel = signalChannelService.add(signalChannel);
        CommonResponse commonResponse = CommonResponse.getInstance(signalChannel);
        LogUtil.add(this.logRecordRepository, "添加", "信号通道", loginUser, signalChannel.getId(), signalChannel.getName());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改信号通道", notes = "修改信号通道信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse update(@Validated({SignalChannel.Validation.class}) @RequestBody SignalChannel signalChannel, @ApiIgnore User loginUser) throws BusinessException {
        signalChannel = signalChannelService.update(signalChannel);
        CommonResponse commonResponse = CommonResponse.getInstance(signalChannel);
        LogUtil.add(this.logRecordRepository, "修改", "信号通道", loginUser, signalChannel.getId(), signalChannel.getName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除信号通道", notes = "删除信号通道信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse delete(@NotNull(message = "信号通道编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        signalChannelService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询信号通道", notes = "根据ID查询信号通道")
    public CommonResponse get(@NotNull(message = "信号通道编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        SignalChannel signalChannel = signalChannelService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(signalChannel);
        LogUtil.add(this.logRecordRepository, "查询", "信号通道", loginUser, signalChannel.getId(), signalChannel.getName());
        return commonResponse;
    }
}
