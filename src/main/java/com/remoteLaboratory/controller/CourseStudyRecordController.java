package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.CourseStudyRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.CourseStudyRecordService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.CourseStudyRecordPublicVo;
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
 * 课程学习记录接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/courseStudyRecord")
@Api(description = "课程学习记录")
@LoginRequired
public class CourseStudyRecordController {

    private static Logger log = LoggerFactory.getLogger(CourseStudyRecordController.class);

    @Autowired
    private CourseStudyRecordService courseStudyRecordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "课程学习记录列表", notes = "查询课程学习记录信息列表")
    @LoginRequired(adminRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(courseStudyRecordService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "课程学习记录", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping(path = "/listByCourseId/{courseId}")
    @ApiOperation(value = "课程学习记录列表", notes = "根据章Id查询课程学习记录信息列表")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse listByCourseId(@RequestBody ListInput listInput, @NotNull(message = "课程Id不能为空") @PathVariable Integer courseId, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(courseId);
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(courseStudyRecordService.listByCourseId(listInput, courseId));
        LogUtil.add(this.logRecordRepository, "列表查询", "课程学习记录", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @PostMapping(path = "/startStudy/{courseId}")
    @ApiOperation(value = "开始学习课程(生成学习记录)", notes = "开始学习课程接口(生成学习记录)")
    public CommonResponse startStudy(@PathVariable Integer courseId, @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        CourseStudyRecordPublicVo courseStudyRecordPublicVo = courseStudyRecordService.startStudy(courseId, loginUser);
        commonResponse.setResult(courseStudyRecordPublicVo);
        LogUtil.add(this.logRecordRepository, "开始学习", "课程学习记录", loginUser, courseStudyRecordPublicVo.getId(), courseStudyRecordPublicVo.getUserName() + ": " + courseStudyRecordPublicVo.getCourseName());
        return commonResponse;
    }

    @GetMapping(path = "/getDetail/{id}")
    @ApiOperation(value = "查询课程学习记录详情", notes = "根据ID查询课程学习记录详情接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse getDetail(@NotNull(message = "课程学习记录编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordService.get(id);
        Course course = this.courseService.get(courseStudyRecord.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CourseStudyRecordPublicVo courseStudyRecordPublicVo = courseStudyRecordService.getDetailById(id);
        CommonResponse commonResponse = CommonResponse.getInstance(courseStudyRecordPublicVo);
        LogUtil.add(this.logRecordRepository, "查询详情", "课程学习记录", loginUser, courseStudyRecordPublicVo.getId(), courseStudyRecordPublicVo.getUserName() + ": " + courseStudyRecordPublicVo.getCourseName());
        return commonResponse;
    }

    @GetMapping(path = "/getMyDetailByCourseId/{courseId}")
    @ApiOperation(value = "查询我的课程学习记录详情", notes = "根据课程ID查询我的课程学习记录详情接口()")
    public CommonResponse getDetailByCourseId(@NotNull(message = "课程学习记录编号不能为空") @PathVariable Integer courseId, @ApiIgnore User loginUser) throws BusinessException {
        CourseStudyRecordPublicVo courseStudyRecordPublicVo = courseStudyRecordService.getDetailByCourseId(courseId, loginUser.getId());
        CommonResponse commonResponse = CommonResponse.getInstance(courseStudyRecordPublicVo);
        LogUtil.add(this.logRecordRepository, "查询详情", "课程学习记录", loginUser, courseStudyRecordPublicVo.getId(), courseStudyRecordPublicVo.getUserName() + ": " + courseStudyRecordPublicVo.getCourseName());
        return commonResponse;
    }
}
