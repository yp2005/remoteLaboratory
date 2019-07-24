package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Simulation;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.SimulationService;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 仿真实验接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/simulation")
@Api(description = "仿真实验")
@LoginRequired
public class SimulationController {

    private static Logger log = LoggerFactory.getLogger(SimulationController.class);

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "仿真实验列表", notes = "查询仿真实验信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(simulationService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "仿真实验", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加仿真实验", notes = "添加仿真实验信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse add(@Validated({Simulation.Validation.class}) @RequestBody Simulation simulation, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(simulation.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        simulation = simulationService.add(simulation);
        CommonResponse commonResponse = CommonResponse.getInstance(simulation);
        LogUtil.add(this.logRecordRepository, "添加", "仿真实验", loginUser, simulation.getId(), simulation.getName());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改仿真实验", notes = "修改仿真实验信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse update(@Validated({Simulation.Validation.class}) @RequestBody Simulation simulation, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(simulation.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        simulation = simulationService.update(simulation);
        CommonResponse commonResponse = CommonResponse.getInstance(simulation);
        LogUtil.add(this.logRecordRepository, "修改", "仿真实验", loginUser, simulation.getId(), simulation.getName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除仿真实验", notes = "删除仿真实验信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse delete(@NotNull(message = "仿真实验编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        simulationService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询仿真实验", notes = "根据ID查询仿真实验")
    public CommonResponse get(@NotNull(message = "仿真实验编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Simulation simulation = simulationService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(simulation);
        LogUtil.add(this.logRecordRepository, "查询", "仿真实验", loginUser, simulation.getId(), simulation.getName());
        return commonResponse;
    }
}
