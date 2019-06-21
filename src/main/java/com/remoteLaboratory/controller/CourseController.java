package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseService;
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
 * 课程接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/course")
@Api(description = "课程")
@LoginRequired
public class CourseController {

    private static Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "课程列表", notes = "查询课程信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(courseService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "课程", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加课程", notes = "添加课程信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse add(@Validated({Course.Validation.class}) @RequestBody Course course, @ApiIgnore User loginUser) throws BusinessException {
        course = courseService.add(course);
        CommonResponse commonResponse = CommonResponse.getInstance(course);
        LogUtil.add(this.logRecordRepository, "添加", "课程", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改课程", notes = "修改课程信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse update(@Validated({Course.Validation.class}) @RequestBody Course course, @ApiIgnore User loginUser) throws BusinessException {
        course = courseService.update(course);
        CommonResponse commonResponse = CommonResponse.getInstance(course);
        LogUtil.add(this.logRecordRepository, "修改", "课程", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除课程", notes = "删除课程信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse delete(@NotNull(message = "课程编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        courseService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询课程", notes = "根据ID查询课程")
    public CommonResponse get(@NotNull(message = "课程编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Course course = courseService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(course);
        LogUtil.add(this.logRecordRepository, "查询", "课程", loginUser, course.getId(), course.getName());
        return commonResponse;
    }
}
