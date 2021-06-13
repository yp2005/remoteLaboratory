package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.CourseStudyRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.CourseRepository;
import com.remoteLaboratory.repositories.CourseStudyRecordRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.SetScoreInput;
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
 * 课程接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/course")
@Api(description = "课程")
public class CourseController {

    private static Logger  log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseStudyRecordRepository courseStudyRecordRepository;

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

    @PostMapping(path = "/startExperiment/{courseId}")
    @ApiOperation(value = "课程开始实验", notes = "课程开始实验接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse startExperiment(@PathVariable Integer courseId,  @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(courseId);
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(!course.getStatus().equals(1)) {
            throw new BusinessException(Messages.CODE_40010, "课程尚未开始，不能开始实验！");
        }
        course.setExperimentStarted(true);
        course = courseService.update(course);
        CommonResponse commonResponse = CommonResponse.getInstance(course);
        LogUtil.add(this.logRecordRepository, "开始实验", "课程", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @PostMapping(path = "/setScore")
    @ApiOperation(value = "设置分数分布", notes = "设置分数分布接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse setScore(@RequestBody SetScoreInput setScoreInput, @ApiIgnore User loginUser) throws BusinessException {
        Course course = courseService.setScore(setScoreInput, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance(course);
        LogUtil.add(this.logRecordRepository, "开始实验", "课程", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加课程", notes = "添加课程信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse add(@Validated({Course.Validation.class}) @RequestBody Course course, @ApiIgnore User loginUser) throws BusinessException {
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)) {
            course.setTeacherId(loginUser.getId());
            course.setTeacherName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
        }
        course.setStatus(0);
        course.setExperimentStarted(false);
        course.setStudentNumber(0);
        course.setSubjectNumber(0);
        course.setCommentNumber(0);
        course.setIsDeleted(false);
        course = courseService.add(course);
        CommonResponse commonResponse = CommonResponse.getInstance(course);
        LogUtil.add(this.logRecordRepository, "添加", "课程", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改课程", notes = "修改课程信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse update(@Validated({Course.Validation.class}) @RequestBody Course course, @ApiIgnore User loginUser) throws BusinessException {
        Course courseOld = this.courseService.get(course.getId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !courseOld.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)) {
            course.setTeacherId(loginUser.getId());
            course.setTeacherName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
        }
        course.setStatus(0);
        course = courseService.update(course);
        CommonResponse commonResponse = CommonResponse.getInstance(course);
        LogUtil.add(this.logRecordRepository, "修改", "课程", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @PutMapping(path = "/updateStatus/{courseId}/{status}")
    @ApiOperation(value = "修改课程状态(0-草稿(即将开始) 1-发布(进行中) 2-结束)", notes = "修改课程状态接口(0-草稿(即将开始) 1-发布(进行中) 2-结束)")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse updateStatus(@NotNull(message = "课程编号不能为空") @PathVariable("courseId") Integer courseId,
                                       @NotNull(message = "课程状态不能为空") @PathVariable("status") Integer status,
                                       @ApiIgnore User loginUser) throws BusinessException {
        Course course = courseService.updateStatus(courseId, status, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance(course);
        LogUtil.add(this.logRecordRepository, "修改状态", "课程", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除课程", notes = "删除课程信息接口")
    @LoginRequired(teacherRequired = "1")
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

    @GetMapping(path = "/getDetail/{id}")
    @ApiOperation(value = "查询课程", notes = "根据ID查询课程")
    public CommonResponse getDetail(@NotNull(message = "课程编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Course course = courseService.getDetail(id);
        CommonResponse commonResponse = CommonResponse.getInstance(course);
        LogUtil.add(this.logRecordRepository, "查询", "课程", loginUser, course.getId(), course.getName());
        return commonResponse;
    }
}
