package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.Section;
import com.remoteLaboratory.entities.SectionStudyRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.SectionService;
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
 * 课程节接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/section")
@Api(description = "课程节")
@LoginRequired
public class SectionController {

    private static Logger log = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    private SectionService sectionService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private CourseService courseService;

    @PostMapping(path = "/list")
    @ApiOperation(value = "课程节列表", notes = "查询课程节信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(sectionService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "课程节", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加课程节", notes = "添加课程节信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse add(@Validated({Section.Validation.class}) @RequestBody Section section, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(section.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        section = sectionService.add(section);
        CommonResponse commonResponse = CommonResponse.getInstance(section);
        LogUtil.add(this.logRecordRepository, "添加", "课程节", loginUser, section.getId(), section.getName() + "、" + section.getTitle());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改课程节", notes = "修改课程节信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse update(@Validated({Section.Validation.class}) @RequestBody Section section, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(section.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        section = sectionService.update(section);
        CommonResponse commonResponse = CommonResponse.getInstance(section);
        LogUtil.add(this.logRecordRepository, "修改", "课程节", loginUser, section.getId(), section.getName() + "、" + section.getTitle());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除课程节", notes = "删除课程节信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse delete(@NotNull(message = "课程节编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        sectionService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询课程节", notes = "根据ID查询课程节")
    public CommonResponse get(@NotNull(message = "课程节编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Section section = sectionService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(section);
        LogUtil.add(this.logRecordRepository, "查询", "课程节", loginUser, section.getId(), section.getName() + "、" + section.getTitle());
        return commonResponse;
    }

    @GetMapping(path = "/startStudy/{id}")
    @ApiOperation(value = "开始学习课程小节", notes = "开始学习课程小节接口")
    public CommonResponse startStudy(@NotNull(message = "课程节编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Section section = sectionService.startStudy(id, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance(section);
        LogUtil.add(this.logRecordRepository, "开始学习", "课程节", loginUser, section.getId(), section.getName() + "、" + section.getTitle());
        return commonResponse;
    }

    @GetMapping(path = "/finish/{id}")
    @ApiOperation(value = "完成课程小节学习", notes = "完成课程小节学习接口")
    public CommonResponse finish(@NotNull(message = "课程节编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        SectionStudyRecord sectionStudyRecord = sectionService.finish(id, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance(sectionStudyRecord);
        LogUtil.add(this.logRecordRepository, "完成学习", "课程节", loginUser, sectionStudyRecord.getSectionId(), sectionStudyRecord.getSectionName() + "、" + sectionStudyRecord.getSectionTitle());
        return commonResponse;
    }
}
