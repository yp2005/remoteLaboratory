package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.TestTemplate;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.TestTemplateService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.TestTemplatePublicVo;
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
 * 测验模板接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/testTemplate")
@Api(description = "测验模板")
@LoginRequired(teacherRequired = "1")
public class TestTemplateController {

    private static Logger log = LoggerFactory.getLogger(TestTemplateController.class);

    @Autowired
    private TestTemplateService testTemplateService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "测验模板列表", notes = "查询测验模板信息列表")
    @LoginRequired(adminRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testTemplateService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "测验模板", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加测验模板", notes = "添加测验模板信息接口")
    public CommonResponse add(@Validated({TestTemplatePublicVo.Validation.class}) @RequestBody TestTemplatePublicVo testTemplatePublicVo, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(testTemplatePublicVo.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        testTemplatePublicVo = testTemplateService.add(testTemplatePublicVo);
        CommonResponse commonResponse = CommonResponse.getInstance(testTemplatePublicVo);
        LogUtil.add(this.logRecordRepository, "添加", "测验模板", loginUser, testTemplatePublicVo.getId(), testTemplatePublicVo.getName());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改测验模板", notes = "修改测验模板信息接口")
    public CommonResponse update(@Validated({TestTemplatePublicVo.Validation.class}) @RequestBody TestTemplatePublicVo testTemplatePublicVo, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(testTemplatePublicVo.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        testTemplatePublicVo = testTemplateService.update(testTemplatePublicVo);
        CommonResponse commonResponse = CommonResponse.getInstance(testTemplatePublicVo);
        LogUtil.add(this.logRecordRepository, "修改", "测验模板", loginUser, testTemplatePublicVo.getId(), testTemplatePublicVo.getName());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除测验模板", notes = "删除测验模板信息接口")
    public CommonResponse delete(@NotNull(message = "测验模板编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        testTemplateService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/getBySectionId/{sectionId}")
    @ApiOperation(value = "查询测验模板", notes = "根据课程小节ID查询测验模板接口")
    @LoginRequired
    public CommonResponse get(@NotNull(message = "课程小节编号不能为空") @PathVariable Integer sectionId, @ApiIgnore User loginUser) throws BusinessException {
        TestTemplate testTemplate = testTemplateService.getBySectionId(sectionId);
        CommonResponse commonResponse = CommonResponse.getInstance(testTemplate);
        LogUtil.add(this.logRecordRepository, "查询", "测验模板", loginUser, testTemplate.getId(), testTemplate.getName());
        return commonResponse;
    }

    @GetMapping(path = "/getDetailBySectionId/{sectionId}")
    @ApiOperation(value = "查询测验模板详情", notes = "根据课程小节ID查询测验模板详情接口")
    public CommonResponse getDetailBySectionId(@NotNull(message = "课程小节编号不能为空") @PathVariable Integer sectionId, @ApiIgnore User loginUser) throws BusinessException {
        TestTemplatePublicVo testTemplatePublicVo = testTemplateService.getDetailBySectionId(sectionId);
        CommonResponse commonResponse = CommonResponse.getInstance(testTemplatePublicVo);
        LogUtil.add(this.logRecordRepository, "查询详情", "测验模板", loginUser, testTemplatePublicVo.getId(), testTemplatePublicVo.getName());
        return commonResponse;
    }
}
