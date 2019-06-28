package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.*;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.ExerciseUtil;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.AnswerInput;
import com.remoteLaboratory.vo.GradeInput;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.TestInstancePublicVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;

/**
 * 测验实例接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/testInstance")
@Api(description = "测验实例")
@LoginRequired
public class TestInstanceController {

    private static Logger log = LoggerFactory.getLogger(TestInstanceController.class);

    @Autowired
    private TestInstanceService testInstanceService;

    @Autowired
    private TestExerciseInstanceService testExerciseInstanceService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "测验实例列表", notes = "查询测验实例信息列表")
    @LoginRequired(adminRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput, @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "测验实例", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping(path = "/listBySectionId/{sectionId}")
    @ApiOperation(value = "测验实例列表", notes = "根据小节Id查询测验实例信息列表")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse listBySectionId(@RequestBody ListInput listInput, @NotNull(message = "小节Id不能为空") @PathVariable Integer sectionId, @ApiIgnore User loginUser) throws BusinessException {
        Section section = this.sectionService.get(sectionId);
        Course course = this.courseService.get(section.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.listBySectionId(listInput, sectionId));
        LogUtil.add(this.logRecordRepository, "列表查询", "测验实例", loginUser, section.getId(), section.getName());
        return commonResponse;
    }

    @PostMapping(path = "/listByChapterId/{chapterId}")
    @ApiOperation(value = "测验实例列表", notes = "根据章Id查询测验实例信息列表")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse listByChapterId(@RequestBody ListInput listInput, @NotNull(message = "章Id不能为空") @PathVariable Integer chapterId, @ApiIgnore User loginUser) throws BusinessException {
        Chapter chapter = this.chapterService.get(chapterId);
        Course course = this.courseService.get(chapter.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.listByChapterId(listInput, chapterId));
        LogUtil.add(this.logRecordRepository, "列表查询", "测验实例", loginUser, chapter.getId(), chapter.getName());
        return commonResponse;
    }

    @PostMapping(path = "/listByCourseId/{courseId}")
    @ApiOperation(value = "测验实例列表", notes = "根据课程Id查询测验实例信息列表")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse listByCourseId(@RequestBody ListInput listInput, @NotNull(message = "课程Id不能为空") @PathVariable Integer courseId, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(courseId);
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.listByCourseId(listInput, courseId));
        LogUtil.add(this.logRecordRepository, "列表查询", "测验实例", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @PostMapping(path = "/startTest/{testTemplateId}")
    @ApiOperation(value = "开始测验(生成测验实例)", notes = "开始测验接口(生成测验实例)")
    public CommonResponse startTest(@NotNull(message = "测验模板编号不能为空") @PathVariable Integer testTemplateId, @ApiIgnore User loginUser) throws BusinessException {
        TestInstancePublicVo testInstancePublicVo = testInstanceService.startTest(testTemplateId, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance(testInstancePublicVo);
        LogUtil.add(this.logRecordRepository, "开始测验", "测验实例", loginUser, testInstancePublicVo.getId(), testInstancePublicVo.getName());
        return commonResponse;
    }

    @GetMapping(path = "/getMyBySectionId/{sectionId}")
    @ApiOperation(value = "查询测验实例详情", notes = "根据课程小节ID查询测验实例详情接口")
    public CommonResponse getMyBySectionId(@NotNull(message = "测验实例编号不能为空") @PathVariable Integer sectionId, @ApiIgnore User loginUser) throws BusinessException {
        TestInstancePublicVo testInstancePublicVo = testInstanceService.getMyBySectionId(sectionId, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance(testInstancePublicVo);
        LogUtil.add(this.logRecordRepository, "查询详情", "测验实例", loginUser, testInstancePublicVo.getId(), testInstancePublicVo.getName());
        return commonResponse;
    }

    @GetMapping(path = "/getDetail/{id}")
    @ApiOperation(value = "查询测验实例详情", notes = "根据ID查询测验实例详情接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse getDetail(@NotNull(message = "测验实例编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        TestInstancePublicVo testInstancePublicVo = testInstanceService.getDetail(id);
        Course course = this.courseService.get(testInstancePublicVo.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CommonResponse commonResponse = CommonResponse.getInstance(testInstancePublicVo);
        LogUtil.add(this.logRecordRepository, "查询详情", "测验实例", loginUser, testInstancePublicVo.getId(), testInstancePublicVo.getName());
        return commonResponse;
    }

    @PostMapping(path = "/answer")
    @ApiOperation(value = "答题", notes = "答题接口")
    public CommonResponse answer(@RequestBody AnswerInput answerInput, @ApiIgnore User loginUser) throws BusinessException {
        TestExerciseInstance testExerciseInstance = this.testExerciseInstanceService.get(answerInput.getTestExerciseInstanceId());
        TestInstance testInstance = this.testInstanceService.get(testExerciseInstance.getTestInstanceId());
        if (!testInstance.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(!testInstance.getStatus().equals(0)) {
            throw new BusinessException(Messages.CODE_40010, "已交卷不可再继续答题");
        }
        testExerciseInstance.setAnswer(answerInput.getAnswer());
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testExerciseInstanceService.answer(testExerciseInstance));
        LogUtil.add(this.logRecordRepository, "答题", "测验小题实例", loginUser, testExerciseInstance.getId(), ExerciseUtil.getTypeName(testExerciseInstance.getExercisesType()));
        return commonResponse;
    }

    @PostMapping(path = "/grade")
    @ApiOperation(value = "打分", notes = "打分接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse grade(@RequestBody GradeInput gradeInput, @ApiIgnore User loginUser) throws BusinessException {
        TestExerciseInstance testExerciseInstance = this.testExerciseInstanceService.get(gradeInput.getTestExerciseInstanceId());
        TestInstance testInstance = this.testInstanceService.get(testExerciseInstance.getTestInstanceId());
        Course course = this.courseService.get(testInstance.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(testInstance.getStatus().equals(0)) {
            throw new BusinessException(Messages.CODE_40010, "学生尚未交卷");
        }
        testExerciseInstance.setScored(gradeInput.getScored());
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testExerciseInstanceService.update(testExerciseInstance));
        LogUtil.add(this.logRecordRepository, "打分", "测验小题实例", loginUser, testExerciseInstance.getId(), ExerciseUtil.getTypeName(testExerciseInstance.getExercisesType()));
        return commonResponse;
    }

    @PostMapping(path = "/submit/{id}")
    @ApiOperation(value = "交卷", notes = "交卷接口")
    public CommonResponse submit(@NotNull(message = "测验实例编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        TestInstance testInstance = this.testInstanceService.get(id);
        if (!testInstance.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(!testInstance.getStatus().equals(0)) {
            throw new BusinessException(Messages.CODE_40010, "已交卷不可重复提交");
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.submit(id, 1));
        LogUtil.add(this.logRecordRepository, "交卷", "测验实例", loginUser, testInstance.getId(), testInstance.getName());
        return commonResponse;
    }

    @PostMapping(path = "/finish/{id}")
    @ApiOperation(value = "阅卷完成", notes = "阅卷完成接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse finish(@NotNull(message = "测验实例编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        TestInstance testInstance = this.testInstanceService.get(id);
        Course course = this.courseService.get(testInstance.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(testInstance.getStatus().equals(0)) {
            throw new BusinessException(Messages.CODE_40010, "学生尚未交卷");
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.submit(id, 2));
        LogUtil.add(this.logRecordRepository, "阅卷完成", "测验实例", loginUser, testInstance.getId(), testInstance.getName());
        return commonResponse;
    }
}
