package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.CourseComment;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseCommentService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
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
@LoginRequired
public class CourseCommentController {

    private static Logger log = LoggerFactory.getLogger(CourseCommentController.class);

    @Autowired
    private CourseCommentService courseCommentService;

    @Autowired
    private LogRecordRepository logRecordRepository;

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
    public CommonResponse add(@Validated({CourseComment.Validation.class}) @RequestBody CourseComment courseComment, @ApiIgnore User loginUser) throws BusinessException {
        courseComment.setUserId(loginUser.getId());
        courseComment.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
        courseComment = courseCommentService.add(courseComment);
        CommonResponse commonResponse = CommonResponse.getInstance(courseComment);
        LogUtil.add(this.logRecordRepository, "添加", "课程评论", loginUser, courseComment.getId(), courseComment.getTitle());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改课程评论", notes = "修改课程评论信息接口")
    public CommonResponse update(@Validated({CourseComment.Validation.class}) @RequestBody CourseComment courseComment, @ApiIgnore User loginUser) throws BusinessException {
        courseComment.setUserId(loginUser.getId());
        courseComment.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
        courseComment = courseCommentService.update(courseComment);
        CommonResponse commonResponse = CommonResponse.getInstance(courseComment);
        LogUtil.add(this.logRecordRepository, "修改", "课程评论", loginUser, courseComment.getId(), courseComment.getTitle());
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