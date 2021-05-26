package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.TestSubsectionInstanceRepository;
import com.remoteLaboratory.service.*;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.ExerciseUtil;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.date.DateTimeUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 实验报告接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/testInstance")
@Api(description = "实验报告")
@LoginRequired(studentRequired = "1")
public class TestInstanceController {

    private static Logger log = LoggerFactory.getLogger(TestInstanceController.class);

    @Autowired
    private TestInstanceService testInstanceService;

    @Autowired
    private TestExerciseInstanceService testExerciseInstanceService;

    @Autowired
    private TestSubsectionInstanceRepository testSubsectionInstanceRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private TestTemplateService testTemplateService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @PostMapping(path = "/list")
    @ApiOperation(value = "实验报告列表", notes = "查询实验报告信息列表")
    @LoginRequired(adminRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput, @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "实验报告", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping(path = "/listByCourseId/{courseId}")
    @ApiOperation(value = "实验报告列表", notes = "根据课程Id查询实验报告信息列表")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse listByCourseId(@RequestBody ListInput listInput, @NotNull(message = "课程Id不能为空") @PathVariable Integer courseId, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(courseId);
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.listByCourseId(listInput, courseId));
        LogUtil.add(this.logRecordRepository, "列表查询", "实验报告", loginUser, course.getId(), course.getName());
        return commonResponse;
    }

    @PostMapping(path = "/exportByCourseId/{courseId}")
    @ApiOperation(value = "导出实验报告", notes = "导出实验报告")
    public void exportByCourseId(@RequestBody ListInput listInput, @NotNull(message = "课程Id不能为空") @PathVariable Integer courseId, @ApiIgnore User loginUser, HttpServletResponse response) throws BusinessException {
        Course course = this.courseService.get(courseId);
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        ListOutput listOutput = testInstanceService.listByCourseId(listInput, courseId);
        if(CollectionUtils.isNotEmpty(listOutput.getList())) {
            //创建工作簿
            HSSFWorkbook wb = new HSSFWorkbook();

            //创建字体样式
            HSSFFont cellFont = wb.createFont();
            cellFont.setFontName("宋体");//使用宋体
            cellFont.setFontHeightInPoints((short) 12);//字体大小
            //创建单元格样式style
            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setFont(cellFont);//将字体注入
            cellStyle.setWrapText(true);//自动换行
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            cellStyle.setBorderTop((short) 1);//设置边框大小
            cellStyle.setBorderBottom((short) 1);
            cellStyle.setBorderLeft((short) 1);
            cellStyle.setBorderRight((short) 1);

            //创建表头字体样式
            HSSFFont headFont = wb.createFont();
            headFont.setFontName("宋体");//使用宋体
            headFont.setFontHeightInPoints((short) 14);//字体大小
            headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
            //创建表头样式style
            HSSFCellStyle headStyle = wb.createCellStyle();
            headStyle.setFont(headFont);//将字体注入
            headStyle.setWrapText(true);//自动换行
            headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
            headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            headStyle.setBorderTop((short) 1);//设置边框大小
            headStyle.setBorderBottom((short) 1);
            headStyle.setBorderLeft((short) 1);
            headStyle.setBorderRight((short) 1);

            HSSFSheet wbSheet = wb.createSheet("学生成绩");
            // 设置默认列宽
            wbSheet.setDefaultColumnWidth(20);
            HSSFCell cell;
            HSSFRow row;

            row = wbSheet.createRow(0);

            cell = row.createCell(0);
            cell.setCellStyle(headStyle);
            cell.setCellValue("课程");

            cell = row.createCell(1);
            cell.setCellStyle(headStyle);
            cell.setCellValue("实验报告");

            cell = row.createCell(2);
            cell.setCellStyle(headStyle);
            cell.setCellValue("班级");

            cell = row.createCell(3);
            cell.setCellStyle(headStyle);
            cell.setCellValue("姓名");

            cell = row.createCell(4);
            cell.setCellStyle(headStyle);
            cell.setCellValue("学号");

            cell = row.createCell(5);
            cell.setCellStyle(headStyle);
            cell.setCellValue("交卷时间");

            cell = row.createCell(6);
            cell.setCellStyle(headStyle);
            cell.setCellValue("成绩");

            for (int i = 0; i < listOutput.getList().size(); i++) {
                TestInstance testInstance = (TestInstance) listOutput.getList().get(i);
                row = wbSheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(course.getName());

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(testInstance.getName());

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(testInstance.getClass1());

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(testInstance.getUserName());

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(testInstance.getUserKey());

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(DateTimeUtil.formatDate(testInstance.getSubmitTime()));

                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(testInstance.getScored());
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-Disposition", "attachment;Filename=" + System.currentTimeMillis() + ".xls");
            try {
                OutputStream outputStream = response.getOutputStream();
                wb.write(outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new BusinessException(Messages.CODE_50000);
            }
        }
        else {
            throw new BusinessException(Messages.CODE_20001);
        }
        LogUtil.add(this.logRecordRepository, "导出实验报告", "实验报告", loginUser, course.getId(), course.getName());
    }

    @PostMapping(path = "/startTest/{testTemplateId}")
    @ApiOperation(value = "开始答题(生成实验报告)", notes = "开始答题接口(生成实验报告)")
    public CommonResponse startTest(@NotNull(message = "实验报告模板编号不能为空") @PathVariable Integer testTemplateId, @ApiIgnore User loginUser) throws BusinessException {
        TestInstancePublicVo testInstancePublicVo = testInstanceService.startTest(testTemplateId, loginUser);
        if(testInstancePublicVo.getStatus().equals(0) && testInstancePublicVo.getTestType().equals(1)) { // 实验报告中的实验报告
            for (TestSubsectionInstancePublicVo testSubsectionInstancePublicVo : testInstancePublicVo.getTestSubsectionInstancePublicVoList()) {
                if(CollectionUtils.isNotEmpty(testSubsectionInstancePublicVo.getTestPartInstancePublicVoList())) {
                    for(TestPartInstancePublicVo testPartInstancePublicVo : testSubsectionInstancePublicVo.getTestPartInstancePublicVoList()) {
                        if(CollectionUtils.isNotEmpty(testPartInstancePublicVo.getTestExerciseInstanceList())) {
                            List<TestExerciseInstanceOutput> testExerciseInstanceOutputList = new ArrayList<>();
                            for(TestExerciseInstance testExerciseInstance : (List<TestExerciseInstance>)testPartInstancePublicVo.getTestExerciseInstanceList()) {
                                testExerciseInstanceOutputList.add(new TestExerciseInstanceOutput(testExerciseInstance));
                            }
                            testPartInstancePublicVo.setTestExerciseInstanceList(testExerciseInstanceOutputList);
                        }
                    }
                }
            }
        }
        CommonResponse commonResponse = CommonResponse.getInstance(testInstancePublicVo);
        LogUtil.add(this.logRecordRepository, "开始答题", "实验报告", loginUser, testInstancePublicVo.getId(), testInstancePublicVo.getName());
        return commonResponse;
    }

    @GetMapping(path = "/getMy")
    @ApiOperation(value = "查询我的实验报告", notes = "查询我的实验报告接口")
    public CommonResponse getMy(@ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.getByUserId(loginUser.getId()));
        LogUtil.add(this.logRecordRepository, "查询我的实验报告", "实验报告", loginUser, null, null);
        return commonResponse;
    }

    @GetMapping(path = "/getMyByCourseId/{courseId}")
    @ApiOperation(value = "根据课程ID查询我的实验报告", notes = "根据课程ID查询我的实验报告接口")
    public CommonResponse getMyByCourseId(@PathVariable Integer courseId, @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.getByUserIdAndCourseId(loginUser.getId(), courseId));
        LogUtil.add(this.logRecordRepository, "根据课程ID查询我的实验报告", "实验报告", loginUser, null, null);
        return commonResponse;
    }

    @GetMapping(path = "/getDetail/{id}")
    @ApiOperation(value = "查询实验报告详情", notes = "根据ID查询实验报告详情接口")
    public CommonResponse getDetail(@NotNull(message = "实验报告编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        TestInstancePublicVo testInstancePublicVo = testInstanceService.getDetail(id);
        Course course = this.courseService.get(testInstancePublicVo.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)
                && !course.getTeacherId().equals(loginUser.getId())
                && !testInstancePublicVo.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        CommonResponse commonResponse = CommonResponse.getInstance(testInstancePublicVo);
        LogUtil.add(this.logRecordRepository, "查询详情", "实验报告", loginUser, testInstancePublicVo.getId(), testInstancePublicVo.getName());
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
        commonResponse.setResult(new TestExerciseInstanceOutput(testExerciseInstanceService.answer(testExerciseInstance, loginUser)));
        LogUtil.add(this.logRecordRepository, "答题", "实验报告小题实例", loginUser, testExerciseInstance.getId(), ExerciseUtil.getTypeName(testExerciseInstance.getExercisesType()));
        return commonResponse;
    }

    @PostMapping(path = "/grade")
    @ApiOperation(value = "打分", notes = "打分接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse grade(@RequestBody GradeInput gradeInput, @ApiIgnore User loginUser) throws BusinessException {
        TestExerciseInstance testExerciseInstance = null;
        TestInstance testInstance;
        TestSubsectionInstance testSubsectionInstance = null;
        if(gradeInput.getType().equals(1)) { // 小题
            testExerciseInstance = this.testExerciseInstanceService.get(gradeInput.getTestExerciseInstanceId());
            testInstance = this.testInstanceService.get(testExerciseInstance.getTestInstanceId());
            testExerciseInstance.setScored(gradeInput.getScored());
        }
        else { // 分项
            testSubsectionInstance = this.testSubsectionInstanceRepository.findOne(gradeInput.getTestSubsectionInstanceId());
            testInstance = this.testInstanceService.get(testSubsectionInstance.getTestInstanceId());
            testSubsectionInstance.setScored(gradeInput.getScored());
        }
        Course course = this.courseService.get(testInstance.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(testInstance.getStatus().equals(0)) {
            throw new BusinessException(Messages.CODE_40010, "学生尚未交卷");
        }
        if(testInstance.getTestType().equals(2)) {
            throw new BusinessException(Messages.CODE_40010, "问卷调查无需阅卷");
        }

        CommonResponse commonResponse = CommonResponse.getInstance();
        if(gradeInput.getType().equals(1)) { // 小题
            commonResponse.setResult(testExerciseInstanceService.update(testExerciseInstance));
            LogUtil.add(this.logRecordRepository, "打分", "实验报告小题实例", loginUser, testExerciseInstance.getId(), ExerciseUtil.getTypeName(testExerciseInstance.getExercisesType()));
        }
        else {

            if(testInstance.getStatus().equals(2)) { // 已阅卷重新结算分数
                testSubsectionInstance = this.testInstanceService.grade(testSubsectionInstance);
            }
            else {
                testSubsectionInstance = testSubsectionInstanceRepository.save(testSubsectionInstance);
            }
            commonResponse.setResult(testSubsectionInstance);
            LogUtil.add(this.logRecordRepository, "打分", "实验报告分项实例", loginUser, testSubsectionInstance.getId(), testSubsectionInstance.getName());
        }
        return commonResponse;
    }

    @PostMapping(path = "/submit/{id}")
    @ApiOperation(value = "交卷", notes = "交卷接口")
    public CommonResponse submit(@NotNull(message = "实验报告编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        TestInstance testInstance = this.testInstanceService.get(id);
        if (!testInstance.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(!testInstance.getStatus().equals(0)) {
            throw new BusinessException(Messages.CODE_40010, "已交卷不可重复提交");
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.submit(id, 1, loginUser));
        LogUtil.add(this.logRecordRepository, "交卷", "实验报告", loginUser, testInstance.getId(), testInstance.getName());
        return commonResponse;
    }

    @PostMapping(path = "/finish/{id}")
    @ApiOperation(value = "阅卷完成", notes = "阅卷完成接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse finish(@NotNull(message = "实验报告编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        TestInstance testInstance = this.testInstanceService.get(id);
        Course course = this.courseService.get(testInstance.getCourseId());
        if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(testInstance.getStatus().equals(0)) {
            throw new BusinessException(Messages.CODE_40010, "学生尚未交卷");
        }
        if(testInstance.getTestType().equals(2)) {
            throw new BusinessException(Messages.CODE_40010, "问卷调查无需阅卷");
        }
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(testInstanceService.submit(id, 2, null));
        LogUtil.add(this.logRecordRepository, "阅卷完成", "实验报告", loginUser, testInstance.getId(), testInstance.getName());
        return commonResponse;
    }
}
