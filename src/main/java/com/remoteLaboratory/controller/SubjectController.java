package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Subject;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.UserRepository;
import com.remoteLaboratory.service.SubjectService;
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
 * 话题接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/subject")
@Api(description = "话题")
@LoginRequired
public class SubjectController {

    private static Logger log = LoggerFactory.getLogger(SubjectController.class);

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "话题列表", notes = "查询话题信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(subjectService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "话题", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加话题", notes = "添加话题信息接口")
    public CommonResponse add(@Validated({Subject.Validation.class}) @RequestBody Subject subject, @ApiIgnore User loginUser) throws BusinessException {
        loginUser = this.userRepository.findOne(loginUser.getId());
        if(loginUser.getForumForbidden().equals(1)) {
            throw new BusinessException(Messages.CODE_40010, "你被禁言了！");
        }
        subject.setUserId(loginUser.getId());
        subject.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
        subject.setUserImage(loginUser.getUserImage());
        subject.setUserType(loginUser.getUserType());
        subject.setReplyNumber(0);
        subject.setOrderNumber(99);
        subject = subjectService.add(subject);
        CommonResponse commonResponse = CommonResponse.getInstance(subject);
        LogUtil.add(this.logRecordRepository, "添加", "话题", loginUser, subject.getId(), subject.getTitle());
        return commonResponse;
    }

    @PostMapping(path = "/top/{subjectId}/{status}")
    @ApiOperation(value = "置顶话题(0-取消置顶 1-置顶)", notes = "置顶话题接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse top(@PathVariable("subjectId") Integer subjectId, @PathVariable("status") Integer status, @ApiIgnore User loginUser) throws BusinessException {
        Subject subject = this.subjectService.get(subjectId);
        Course course = this.courseService.get(subject.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        subject.setOrderNumber(status.equals(1) ? 0 : 99);
        subject = subjectService.update(subject);
        CommonResponse commonResponse = CommonResponse.getInstance(subject);
        LogUtil.add(this.logRecordRepository, "置顶话题", "话题", loginUser, subject.getId(), subject.getTitle());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除话题", notes = "删除话题信息接口")
    public CommonResponse delete(@NotNull(message = "话题编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        subjectService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询话题", notes = "根据ID查询话题")
    public CommonResponse get(@NotNull(message = "话题编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Subject subject = subjectService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(subject);
        LogUtil.add(this.logRecordRepository, "查询", "话题", loginUser, subject.getId(), subject.getTitle());
        return commonResponse;
    }
}
