package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.CourseComment;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseCommentService;
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
 * 课程评论接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/courseComment")
@Api(description = "课程评论")
public class CourseCommentController {

    private static Logger log = LoggerFactory.getLogger(CourseCommentController.class);

    @Autowired
    private CourseCommentService courseCommentService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private CourseService courseService;

    @PostMapping(path = "/list")
    @ApiOperation(value = "课程评论列表", notes = "查询课程评论信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(courseCommentService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "课程评论", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加课程评论", notes = "添加课程评论信息接口")
    @LoginRequired
    public CommonResponse add(@Validated({CourseComment.Validation.class}) @RequestBody CourseComment courseComment, @ApiIgnore User loginUser) throws BusinessException {
        courseComment.setUserId(loginUser.getId());
        courseComment.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
        courseComment.setMainPageDisplay(false);
        courseComment.setUserImage(loginUser.getUserImage());
        courseComment.setUserKey(loginUser.getUserKey());
        StringBuilder class1 = new StringBuilder();
        class1.append(loginUser.getCollege())
                .append("->")
                .append(loginUser.getMajor())
                .append("->")
                .append(loginUser.getGrade())
                .append("->")
                .append(loginUser.getClass1());
        courseComment.setClass1(class1.toString());
        courseComment = courseCommentService.add(courseComment);
        CommonResponse commonResponse = CommonResponse.getInstance(courseComment);
        LogUtil.add(this.logRecordRepository, "添加", "课程评论", loginUser, courseComment.getId(), courseComment.getTitle());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改课程评论", notes = "修改课程评论信息接口")
    @LoginRequired
    public CommonResponse update(@Validated({CourseComment.Validation.class}) @RequestBody CourseComment courseComment, @ApiIgnore User loginUser) throws BusinessException {
        CourseComment courseCommentOld = this.courseCommentService.get(courseComment.getId());
        if(!loginUser.getId().equals(courseCommentOld.getUserId())) { // 只有自己可以修改评论
            throw new BusinessException(Messages.CODE_50200);
        }
        courseComment.setUserId(loginUser.getId());
        courseComment.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
        courseComment.setMainPageDisplay(courseCommentOld.getMainPageDisplay());
        courseComment.setUserImage(loginUser.getUserImage());
        courseComment = courseCommentService.update(courseComment);
        CommonResponse commonResponse = CommonResponse.getInstance(courseComment);
        LogUtil.add(this.logRecordRepository, "修改", "课程评论", loginUser, courseComment.getId(), courseComment.getTitle());
        return commonResponse;
    }

    @PostMapping(path = "setMainPageDisplay/{id}")
    @ApiOperation(value = "设置评论首页显示", notes = "设置评论首页显示接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse setMainPageDisplay(@NotNull(message = "课程评论编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        CourseComment courseComment = this.courseCommentService.get(id);
        Course course = this.courseService.get(courseComment.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        courseComment = courseCommentService.setMainPageDisplay(courseComment);
        CommonResponse commonResponse = CommonResponse.getInstance(courseComment);
        LogUtil.add(this.logRecordRepository, "设置评论首页显示", "课程评论", loginUser, courseComment.getId(), courseComment.getTitle());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除课程评论", notes = "删除课程评论信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse delete(@NotNull(message = "课程评论编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        courseCommentService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询课程评论", notes = "根据ID查询课程评论")
    public CommonResponse get(@NotNull(message = "课程评论编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        CourseComment courseComment = courseCommentService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(courseComment);
        LogUtil.add(this.logRecordRepository, "查询", "课程评论", loginUser, courseComment.getId(), courseComment.getTitle());
        return commonResponse;
    }
}
