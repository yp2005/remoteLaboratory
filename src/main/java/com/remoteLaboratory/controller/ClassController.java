package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.CourseStudyRecordService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.GetClassByCourseIdInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 班级接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/class")
@Api(description = "班级")
@LoginRequired
public class ClassController {

    @Autowired
    private CourseStudyRecordService courseStudyRecordService;

    @Autowired
    private CourseService courseService;

    @PostMapping(path = "/getByCourseId")
    @ApiOperation(value = "根据courseId获取学习课程的班级", notes = "根据courseId获取学习课程的班级接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse getClassByCourseId(@RequestBody GetClassByCourseIdInput getClassByCourseIdInput, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(getClassByCourseIdInput.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)
                && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        return CommonResponse.getInstance(courseStudyRecordService.getClassByCourseId(getClassByCourseIdInput));
    }

    @PostMapping(path = "/getGradeByCourseId")
    @ApiOperation(value = "根据courseId获取学习课程的年级", notes = "根据courseId获取学习课程的年级接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse getGradeByCourseId(@RequestBody GetClassByCourseIdInput getClassByCourseIdInput, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(getClassByCourseIdInput.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)
                && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        return CommonResponse.getInstance(courseStudyRecordService.getGradeByCourseId(getClassByCourseIdInput));
    }
}
