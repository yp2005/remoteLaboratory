package com.remoteLaboratory.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.remoteLaboratory.bo.SearchPara;
import com.remoteLaboratory.bo.SearchParas;
import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.*;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.CourseStudyRecordService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程学习记录服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class CourseStudyRecordServiceImpl implements CourseStudyRecordService {
    private static Logger log = LoggerFactory.getLogger(CourseStudyRecordServiceImpl.class);

    @Autowired
    private CourseStudyRecordRepository courseStudyRecordRepository;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ChapterStudyRecordRepository chapterStudyRecordRepository;

    @Autowired
    private SectionStudyRecordRepository sectionStudyRecordRepository;

    @Autowired
    private TestInstanceRepository testInstanceRepository;

    @Autowired
    private TestSubsectionInstanceRepository testSubsectionInstanceRepository;

    @Autowired
    private TestTemplateRepository testTemplateRepository;

    @Autowired
    private TestRecordRepository testRecordRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public CourseStudyRecord add(CourseStudyRecord courseStudyRecord) throws BusinessException {
        courseStudyRecord = courseStudyRecordRepository.save(courseStudyRecord);
        return courseStudyRecord;
    }

    @Override
    public CourseStudyRecordPublicVo startStudy(Integer courseId, User user) throws BusinessException {
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findByCourseIdAndUserId(courseId, user.getId());
        if (courseStudyRecord == null) {
            Course course = this.courseService.get(courseId);
            if (!course.getStatus().equals(1)) {
                throw new BusinessException(Messages.CODE_40010, "课程已结束或尚未开始");
            }
            courseStudyRecord = new CourseStudyRecord();
            courseStudyRecord.setStudied(0.0);
            courseStudyRecord.setScore(0.0);
            courseStudyRecord.setPreStudyScore(0.0);
            courseStudyRecord.setOperationScore(0.0);
            courseStudyRecord.setDataScore(0.0);
            courseStudyRecord.setDataAnalysisScore(0.0);
            courseStudyRecord.setReportScore(0.0);
            courseStudyRecord.setGraded(false);
            courseStudyRecord.setStatus(0);
            courseStudyRecord.setIsOld(false);
            courseStudyRecord.setUserId(user.getId());
            courseStudyRecord.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
            courseStudyRecord.setCourseId(course.getId());
            courseStudyRecord.setCourseName(course.getName());
            courseStudyRecord.setCourseMainImg(course.getMainImg());
            courseStudyRecord.setCourseIntroduction(course.getIntroduction());
            courseStudyRecord.setCourseVideoDesc(course.getVideoDesc());
            courseStudyRecord.setIsQuestionnaireFinish(false);
            StringBuilder class1 = new StringBuilder();
            class1.append(user.getCollege())
                    .append("->")
                    .append(user.getMajor())
                    .append("->")
                    .append(user.getGrade())
                    .append("->")
                    .append(user.getClass1());
            courseStudyRecord.setClass1(class1.toString());
            courseStudyRecord.setGrade(user.getGrade());
            // 查询课程的问卷调查
            TestTemplate questionnaire = this.testTemplateRepository.findQuestionnaireByCourseId(courseId);
            // 查询课程的实验报告
            List<TestTemplate> testTemplateList = this.testTemplateRepository.findByCourseIdAndTestType(courseId, 1);
            if (questionnaire != null) {
                courseStudyRecord.setQuestionnaireTemplateId(questionnaire.getId());
            }
            courseStudyRecord.setTestTemplateNumber(testTemplateList.size());
            courseStudyRecord.setTestTemplateFinishedNumber(0);
            courseStudyRecord.setTestTemplateSubmitedNumber(0);
            courseStudyRecord = this.courseStudyRecordRepository.save(courseStudyRecord);
            CourseStudyRecordPublicVo courseStudyRecordPublicVo = new CourseStudyRecordPublicVo(courseStudyRecord);
            List<ChapterStudyRecordPublicVo> chapterStudyRecordPublicVoList = new ArrayList<>();
            List<Chapter> chapterList = this.chapterRepository.findByCourseId(courseId);
            if (CollectionUtils.isNotEmpty(chapterList)) {
                for (Chapter chapter : chapterList) {
                    ChapterStudyRecord chapterStudyRecord = new ChapterStudyRecord();
                    chapterStudyRecord.setChapterId(chapter.getId());
                    chapterStudyRecord.setChapterName(chapter.getName());
                    chapterStudyRecord.setChapterTitle(chapter.getTitle());
                    chapterStudyRecord.setCourseStudyRecordId(courseStudyRecord.getId());
                    chapterStudyRecord.setStudied(0.0);
                    chapterStudyRecord.setUserId(user.getId());
                    chapterStudyRecord.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
                    chapterStudyRecord = this.chapterStudyRecordRepository.save(chapterStudyRecord);
                    ChapterStudyRecordPublicVo chapterStudyRecordPublicVo = new ChapterStudyRecordPublicVo(chapterStudyRecord);
                    List<SectionStudyRecord> sectionStudyRecordList = new ArrayList<>();
                    List<Section> sectionList = this.sectionRepository.findByChapterId(chapter.getId());
                    if (CollectionUtils.isNotEmpty(sectionList)) {
                        for (Section section : sectionList) {
                            SectionStudyRecord sectionStudyRecord = new SectionStudyRecord();
                            sectionStudyRecord.setChapterStudyRecordId(chapterStudyRecord.getId());
                            sectionStudyRecord.setCourseStudyRecordId(courseStudyRecord.getId());
                            sectionStudyRecord.setChapterId(chapter.getId());
                            sectionStudyRecord.setChapterName(chapter.getName());
                            sectionStudyRecord.setChapterTitle(chapter.getTitle());
                            sectionStudyRecord.setSectionId(section.getId());
                            sectionStudyRecord.setSectionName(section.getName());
                            sectionStudyRecord.setSectionTitle(section.getTitle());
                            sectionStudyRecord.setStudyStatus(0);
                            sectionStudyRecord.setUserId(user.getId());
                            sectionStudyRecord.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
                            sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
                            sectionStudyRecordList.add(sectionStudyRecord);
                        }
                    }
                    chapterStudyRecordPublicVo.setSectionStudyRecordList(sectionStudyRecordList);
                    chapterStudyRecordPublicVoList.add(chapterStudyRecordPublicVo);
                }
            }
            courseStudyRecordPublicVo.setChapterStudyRecordPublicVoList(chapterStudyRecordPublicVoList);
            List<TestRecord> testRecordList = new ArrayList<>();
            for (TestTemplate testTemplate : testTemplateList) {
                TestRecord testRecord = new TestRecord();
                testRecord.setUserId(courseStudyRecord.getUserId());
                testRecord.setUserName(courseStudyRecord.getUserName());
                testRecord.setCourseStudyRecordId(courseStudyRecord.getId());
                testRecord.setTestTemplateId(testTemplate.getId());
                testRecord.setTestTemplateName(testTemplate.getName());
                testRecord.setStatus(0);
                testRecord = this.testRecordRepository.save(testRecord);
                testRecordList.add(testRecord);
            }
            courseStudyRecordPublicVo.setTestRecordList(testRecordList);
            course.setStudentNumber(course.getStudentNumber() + 1);
            course = this.courseRepository.save(course);
            return courseStudyRecordPublicVo;
        } else {
            return this.getDetailByCourseId(courseId, user.getId());
        }
    }

    @Override
    public void updatePercent(CourseStudyRecord courseStudyRecord) throws BusinessException {
        if (courseStudyRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        // 学习状态 0-学习中 1-已完成
        if (courseStudyRecord.getStatus().equals(0)) { // 未完成学习计算学习进度
            List<ChapterStudyRecord> chapterStudyRecordList = this.chapterStudyRecordRepository.findByCourseStudyRecordId(courseStudyRecord.getId());
            Double courseStudied = courseStudyRecord.getTestTemplateSubmitedNumber() / (courseStudyRecord.getTestTemplateNumber() + chapterStudyRecordList.size()) * 1.0;
            if (CollectionUtils.isNotEmpty(chapterStudyRecordList)) {
                Double chapterPercent = 1.0 / (courseStudyRecord.getTestTemplateNumber() + chapterStudyRecordList.size());
                for (ChapterStudyRecord chapterStudyRecord : chapterStudyRecordList) {
                    List<SectionStudyRecord> sectionStudyRecordList = this.sectionStudyRecordRepository.findByChapterStudyRecordId(chapterStudyRecord.getId());
                    Double chapterStudied = 0.0;
                    if (CollectionUtils.isNotEmpty(sectionStudyRecordList)) {
                        Double sectionPercent = 1.0 / sectionStudyRecordList.size();
                        for (SectionStudyRecord sectionStudyRecord : sectionStudyRecordList) {
                            if (sectionStudyRecord.getStudyStatus().equals(1)) {
                                chapterStudied += sectionPercent;
                            }
                        }
                    } else {
                        chapterStudied = 1.0;
                    }
                    chapterStudied = Math.round(chapterStudied * 100) / 100.0;
                    chapterStudyRecord.setStudied(chapterStudied);
                    chapterStudyRecord = this.chapterStudyRecordRepository.save(chapterStudyRecord);
                    courseStudied += chapterPercent * chapterStudied;
                }
            } else if (courseStudyRecord.getTestTemplateNumber().equals(0)) {
                courseStudied = 1.0;
            }
            courseStudied = Math.round(courseStudied * 100) / 100.0;
            courseStudyRecord.setStudied(courseStudied);
            courseStudyRecord.setStatus(courseStudyRecord.getStudied().equals(1.0) ? 1 : 0);
            courseStudyRecord = this.courseStudyRecordRepository.save(courseStudyRecord);
        }
        // 实验报告完成后计算分数
        if (courseStudyRecord.getTestTemplateFinishedNumber().equals(courseStudyRecord.getTestTemplateNumber())) {
            courseStudyRecord = this.calculateScore(courseStudyRecord);
            courseStudyRecord = this.courseStudyRecordRepository.save(courseStudyRecord);
        }
    }

    @Override
    public CourseStudyRecord calculateScore(CourseStudyRecord courseStudyRecord) throws BusinessException {
        List<TestInstance> testInstances = this.testInstanceRepository.findByUserIdAndCourseId(courseStudyRecord.getUserId(), courseStudyRecord.getCourseId());
        Double totalScore = 0.0;
        Double operationScore = 0.0;
        Double dataScore = 0.0;
        Double dataAnalysisScore = 0.0;
        Double reportScore = 0.0;
        if (CollectionUtils.isNotEmpty(testInstances)) {
            for (TestInstance testInstance : testInstances) {
                totalScore += testInstance.getScored();
                List<TestSubsectionInstance> testSubsectionInstanceList = this.testSubsectionInstanceRepository.findByTestInstanceId(testInstance.getId());
                if (CollectionUtils.isNotEmpty(testSubsectionInstanceList)) {
                    for (TestSubsectionInstance testSubsectionInstance : testSubsectionInstanceList) {
                        // 1-实验操作 2-实验数据 3-数据分析 4-实验报告
                        switch (testSubsectionInstance.getType()) {
                            case 1:
                                operationScore += testSubsectionInstance.getScored();
                                break;
                            case 2:
                                dataScore += testSubsectionInstance.getScored();
                                break;
                            case 3:
                                dataAnalysisScore += testSubsectionInstance.getScored();
                                break;
                            case 4:
                                reportScore += testSubsectionInstance.getScored();
                                break;
                        }
                    }
                }
            }
        }
        Course course = this.courseRepository.findOne(courseStudyRecord.getCourseId());
        Double testScore = totalScore / courseStudyRecord.getTestTemplateNumber();
        operationScore = operationScore / courseStudyRecord.getTestTemplateNumber();
        dataScore = dataScore / courseStudyRecord.getTestTemplateNumber();
        dataAnalysisScore = dataAnalysisScore / courseStudyRecord.getTestTemplateNumber();
        reportScore = reportScore / courseStudyRecord.getTestTemplateNumber();

        Double reportSocrePercent = course.getReportScore() / 100;
        testScore = Math.round(testScore * reportSocrePercent * 10) / 10.0;
        operationScore = Math.round(operationScore * reportSocrePercent * 10) / 10.0;
        dataScore = Math.round(dataScore * reportSocrePercent * 10) / 10.0;
        dataAnalysisScore = Math.round(dataAnalysisScore * reportSocrePercent * 10) / 10.0;
        reportScore = Math.round(reportScore * reportSocrePercent * 10) / 10.0;

        Double preStudyScore = Math.round(course.getPreStudyScore() * courseStudyRecord.getStudied()  * 10) / 10.0;
        courseStudyRecord.setScore(preStudyScore + testScore);
        courseStudyRecord.setPreStudyScore(preStudyScore);
        courseStudyRecord.setOperationScore(operationScore);
        courseStudyRecord.setDataScore(dataScore);
        courseStudyRecord.setDataAnalysisScore(dataAnalysisScore);
        courseStudyRecord.setReportScore(reportScore);
        courseStudyRecord.setGraded(true);
        return courseStudyRecord;
    }

    @Override
    public CourseStudyRecord update(CourseStudyRecord courseStudyRecord) throws BusinessException {
        courseStudyRecord = courseStudyRecordRepository.save(courseStudyRecord);
        return courseStudyRecord;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findOne(id);
            if (courseStudyRecord != null) {
                Course course = this.courseService.get(courseStudyRecord.getCourseId());
                if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                courseStudyRecordRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("课程学习记录");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(courseStudyRecord.getId());
                logRecord.setObjectName(courseStudyRecord.getUserName() + ": " + courseStudyRecord.getCourseName());
                logRecords.add(logRecord);
            }
        }
        for (LogRecord logRecord : logRecords) {
            logRecordRepository.save(logRecord);
        }
    }

    @Override
    public ListOutput list(ListInput listInput) throws BusinessException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if (StringUtils.isNotBlank(listInput.getSortDirection())
                && StringUtils.isNotBlank(listInput.getSortProperties())) {
            sort = new Sort(Sort.Direction.fromString(listInput.getSortDirection()), listInput.getSortProperties());
        }
        Pageable pageable = null;
        if (listInput.getPage() != null && listInput.getPageSize() != null) {
            pageable = new PageRequest(listInput.getPage(), listInput.getPageSize(), sort);
        }
        ListOutput listOutput = new ListOutput();
        if (pageable != null) {
            Page<CourseStudyRecord> list = courseStudyRecordRepository.findAll(new MySpecification<CourseStudyRecord>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<CourseStudyRecord> list = courseStudyRecordRepository.findAll(new MySpecification<CourseStudyRecord>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public ListOutput listByCourseId(ListInput listInput, Integer courseId) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("courseId");
        searchPara.setOp("eq");
        searchPara.setValue(courseId);
        if (listInput.getSearchParas() == null) {
            listInput.setSearchParas(new SearchParas());
        }
        if (listInput.getSearchParas().getConditions() == null) {
            listInput.getSearchParas().setConditions(new ArrayList<>());
        }
        listInput.getSearchParas().getConditions().add(searchPara);
        return this.list(listInput);
    }

    @Override
    public ListOutput listByUserId(ListInput listInput, Integer userId) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("userId");
        searchPara.setOp("eq");
        searchPara.setValue(userId);
        if (listInput.getSearchParas() == null) {
            listInput.setSearchParas(new SearchParas());
        }
        if (listInput.getSearchParas().getConditions() == null) {
            listInput.getSearchParas().setConditions(new ArrayList<>());
        }
        listInput.getSearchParas().getConditions().add(searchPara);
        return this.list(listInput);
    }

    @Override
    public CourseStudyRecord get(Integer id) throws BusinessException {
        CourseStudyRecord courseStudyRecord = courseStudyRecordRepository.findOne(id);
        if (courseStudyRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return courseStudyRecord;
    }

    @Override
    public CourseStudyRecordPublicVo getDetailByCourseId(Integer courseId, Integer userId) throws BusinessException {
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findByCourseIdAndUserId(courseId, userId);
        if (courseStudyRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        CourseStudyRecordPublicVo courseStudyRecordPublicVo = new CourseStudyRecordPublicVo(courseStudyRecord);
        List<ChapterStudyRecordPublicVo> chapterStudyRecordPublicVoList = new ArrayList<>();
        List<ChapterStudyRecord> chapterStudyRecordList = this.chapterStudyRecordRepository.findByCourseStudyRecordId(courseStudyRecord.getId());
        if (CollectionUtils.isNotEmpty(chapterStudyRecordList)) {
            for (ChapterStudyRecord chapterStudyRecord : chapterStudyRecordList) {
                ChapterStudyRecordPublicVo chapterStudyRecordPublicVo = new ChapterStudyRecordPublicVo(chapterStudyRecord);
                List<SectionStudyRecord> sectionStudyRecordList = this.sectionStudyRecordRepository.findByChapterStudyRecordId(chapterStudyRecord.getId());
                chapterStudyRecordPublicVo.setSectionStudyRecordList(sectionStudyRecordList);
                chapterStudyRecordPublicVoList.add(chapterStudyRecordPublicVo);
            }
        }
        courseStudyRecordPublicVo.setChapterStudyRecordPublicVoList(chapterStudyRecordPublicVoList);
        courseStudyRecordPublicVo.setTestRecordList(this.testRecordRepository.findByCourseStudyRecordId(courseStudyRecord.getId()));
        return courseStudyRecordPublicVo;
    }

    @Override
    public Map<String, Long> getScoreStatistics(ScoreStatisticsInput scoreStatisticsInput) throws BusinessException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CASE WHEN score >= 90 THEN '优' WHEN score >= 80 THEN '良' WHEN score >= 70 THEN '中' WHEN score >= 60 THEN '及格' ELSE '不及格' END AS level , count(score) AS count FROM rl_course_study_record WHERE course_id = ? ");
        if(StringUtils.isNotEmpty(scoreStatisticsInput.getClass1())) {
            sql.append("AND class1 = ? ");
        }
        if(scoreStatisticsInput.getType().equals(1)) {
            sql.append("AND is_old = false ");
        }
        else if(scoreStatisticsInput.getType().equals(2)) {
            sql.append("AND is_old = true ");
        }
        sql.append("GROUP BY CASE WHEN score >= 90 THEN '优' WHEN score >= 80 THEN '良' WHEN score >= 70 THEN '中' WHEN score >= 60 THEN '及格' ELSE '不及格' END");

        List<Map<String, Object>> result;
        if(StringUtils.isNotEmpty(scoreStatisticsInput.getClass1())) {
            result = jdbcTemplate.queryForList(sql.toString(), scoreStatisticsInput.getCourseId(), scoreStatisticsInput.getClass1());
        }
        else {
            result = jdbcTemplate.queryForList(sql.toString(), scoreStatisticsInput.getCourseId());
        }
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("优", 0l);
        resultMap.put("良", 0l);
        resultMap.put("中", 0l);
        resultMap.put("及格", 0l);
        resultMap.put("不及格", 0l);
        if (CollectionUtils.isNotEmpty(result)) {
            for (Map<String, Object> stringObjectMap : result) {
                Long count = (Long) stringObjectMap.get("count");
                switch ((String) stringObjectMap.get("level")) {
                    case ("优"):
                        resultMap.put("优", count);
                        break;
                    case ("良"):
                        resultMap.put("良", count);
                        break;
                    case ("中"):
                        resultMap.put("中", count);
                        break;
                    case ("及格"):
                        resultMap.put("及格", count);
                        break;
                    case ("不及格"):
                        resultMap.put("不及格", count);
                        break;
                    default:
                        break;
                }
            }
        }
        return resultMap;
    }

    @Override
    public CourseStudyRecordPublicVo getDetailById(Integer id) throws BusinessException {
        CourseStudyRecord courseStudyRecord = courseStudyRecordRepository.findOne(id);
        if (courseStudyRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        CourseStudyRecordPublicVo courseStudyRecordPublicVo = new CourseStudyRecordPublicVo(courseStudyRecord);
        List<ChapterStudyRecordPublicVo> chapterStudyRecordPublicVoList = new ArrayList<>();
        List<ChapterStudyRecord> chapterStudyRecordList = this.chapterStudyRecordRepository.findByCourseStudyRecordId(courseStudyRecord.getId());
        if (CollectionUtils.isNotEmpty(chapterStudyRecordList)) {
            for (ChapterStudyRecord chapterStudyRecord : chapterStudyRecordList) {
                ChapterStudyRecordPublicVo chapterStudyRecordPublicVo = new ChapterStudyRecordPublicVo(chapterStudyRecord);
                List<SectionStudyRecord> sectionStudyRecordList = this.sectionStudyRecordRepository.findByChapterStudyRecordId(chapterStudyRecord.getId());
                chapterStudyRecordPublicVo.setSectionStudyRecordList(sectionStudyRecordList);
                chapterStudyRecordPublicVoList.add(chapterStudyRecordPublicVo);
            }
        }
        courseStudyRecordPublicVo.setChapterStudyRecordPublicVoList(chapterStudyRecordPublicVoList);
        courseStudyRecordPublicVo.setTestRecordList(this.testRecordRepository.findByCourseStudyRecordId(courseStudyRecord.getId()));
        return courseStudyRecordPublicVo;
    }

    @Override
    public List<String> getClassByCourseId(GetClassByCourseIdInput getClassByCourseIdInput) throws BusinessException {
        List<String> result = null;
        switch (getClassByCourseIdInput.getType()) {
            case 0:
                result = this.courseStudyRecordRepository.findClassByCourseId(getClassByCourseIdInput.getCourseId());
                break;
            case 1:
                result = this.courseStudyRecordRepository.findClassByCourseIdAndIsOld(getClassByCourseIdInput.getCourseId(), false);
                break;
            case 2:
                result = this.courseStudyRecordRepository.findClassByCourseIdAndIsOld(getClassByCourseIdInput.getCourseId(), true);
                break;
        }
        return result;
    }

    @Override
    public List<String> getGradeByCourseId(GetClassByCourseIdInput getClassByCourseIdInput) throws BusinessException {
        List<String> result = null;
        switch (getClassByCourseIdInput.getType()) {
            case 0:
                result = this.courseStudyRecordRepository.findGradeByCourseId(getClassByCourseIdInput.getCourseId());
                break;
            case 1:
                result = this.courseStudyRecordRepository.findGradeByCourseIdAndIsOld(getClassByCourseIdInput.getCourseId(), false);
                break;
            case 2:
                result = this.courseStudyRecordRepository.findGradeByCourseIdAndIsOld(getClassByCourseIdInput.getCourseId(), true);
                break;
        }
        return result;
    }

    public static void main(String[] args){
      String ss = "{\n" +
              "  \"id\": 25,\n" +
              "  \"courseId\": 13,\n" +
              "  \"courseName\": \"外骨骼上肢康复机器控制虚拟仿真实验\",\n" +
              "  \"courseMainImg\": \"/remoteLaboratory/upload/admin/20215/c03c05a716b34e909afd8ffde4c04ddc.jpg\",\n" +
              "  \"courseVideoDesc\": \"/remoteLaboratory/upload/admin/20215/90fa5f79e60049afa0e0f7cd2de7ccfe.mp4\",\n" +
              "  \"courseIntroduction\": \"外骨骼上肢康复机器人控制虚拟仿真实验项目依托东北大学优势学科，结合自动化专业核心课程《自动控制原理》的关键知识点，以科研反哺本科教学的新模式，采用实际工程数据和虚拟仿真技术，开发了包含康复医学理论认知、上肢康复机器人训练系统组成、上肢康复机器人运动学和动力学分析、上肢康复机器人被动训练控制器的设计和自主性被动训练控制器的设计等内容，突出康复机器人的控制核心-智能的人机交互和精准的控制算法。\",\n" +
              "  \"class1\": \"4->4->4->4\",\n" +
              "  \"userId\": 15,\n" +
              "  \"userName\": \"4\",\n" +
              "  \"studied\": 0.33,\n" +
              "  \"score\": 82.6,\n" +
              "  \"preStudyScore\": 6.6,\n" +
              "  \"operationScore\": 16,\n" +
              "  \"dataScore\": 16,\n" +
              "  \"dataAnalysisScore\": 16,\n" +
              "  \"reportScore\": 28,\n" +
              "  \"graded\": true,\n" +
              "  \"chapterId\": 5,\n" +
              "  \"chapterName\": \"一\",\n" +
              "  \"chapterTitle\": \"外骨骼上肢康复机器人控制虚拟仿真实验\",\n" +
              "  \"sectionId\": 8,\n" +
              "  \"sectionName\": \"1.4\",\n" +
              "  \"sectionTitle\": \"学习视频四\",\n" +
              "  \"status\": 1,\n" +
              "  \"isOld\": true,\n" +
              "  \"isQuestionnaireFinish\": true,\n" +
              "  \"questionnaireTemplateId\": 7,\n" +
              "  \"testTemplateNumber\": 2,\n" +
              "  \"testTemplateFinishedNumber\": 2,\n" +
              "  \"testTemplateSubmitedNumber\": 2,\n" +
              "  \"createTime\": \"2021-06-29 20:34:32\",\n" +
              "  \"updateTime\": \"2021-06-30 09:10:58\",\n" +
              "  \"chapterStudyRecordPublicVoList\": [\n" +
              "    {\n" +
              "      \"id\": 24,\n" +
              "      \"chapterId\": 5,\n" +
              "      \"chapterName\": \"一\",\n" +
              "      \"chapterTitle\": \"外骨骼上肢康复机器人控制虚拟仿真实验\",\n" +
              "      \"courseStudyRecordId\": 25,\n" +
              "      \"studied\": 1,\n" +
              "      \"userId\": 15,\n" +
              "      \"userName\": \"4\",\n" +
              "      \"createTime\": \"2021-06-29 20:34:32\",\n" +
              "      \"updateTime\": \"2021-06-29 21:04:01\",\n" +
              "      \"sectionStudyRecordList\": [\n" +
              "        {\n" +
              "          \"id\": 53,\n" +
              "          \"chapterId\": 5,\n" +
              "          \"chapterName\": \"一\",\n" +
              "          \"chapterTitle\": \"外骨骼上肢康复机器人控制虚拟仿真实验\",\n" +
              "          \"sectionId\": 5,\n" +
              "          \"sectionName\": \"1.1\",\n" +
              "          \"sectionTitle\": \"学习视频一\",\n" +
              "          \"courseStudyRecordId\": 25,\n" +
              "          \"chapterStudyRecordId\": 24,\n" +
              "          \"studyStatus\": 1,\n" +
              "          \"userId\": 15,\n" +
              "          \"userName\": \"4\",\n" +
              "          \"createTime\": \"2021-06-29 20:34:32\",\n" +
              "          \"updateTime\": \"2021-06-29 21:03:55\"\n" +
              "        },\n" +
              "        {\n" +
              "          \"id\": 54,\n" +
              "          \"chapterId\": 5,\n" +
              "          \"chapterName\": \"一\",\n" +
              "          \"chapterTitle\": \"外骨骼上肢康复机器人控制虚拟仿真实验\",\n" +
              "          \"sectionId\": 6,\n" +
              "          \"sectionName\": \"1.2\",\n" +
              "          \"sectionTitle\": \"学习视频二\",\n" +
              "          \"courseStudyRecordId\": 25,\n" +
              "          \"chapterStudyRecordId\": 24,\n" +
              "          \"studyStatus\": 1,\n" +
              "          \"userId\": 15,\n" +
              "          \"userName\": \"4\",\n" +
              "          \"createTime\": \"2021-06-29 20:34:32\",\n" +
              "          \"updateTime\": \"2021-06-29 21:03:57\"\n" +
              "        },\n" +
              "        {\n" +
              "          \"id\": 55,\n" +
              "          \"chapterId\": 5,\n" +
              "          \"chapterName\": \"一\",\n" +
              "          \"chapterTitle\": \"外骨骼上肢康复机器人控制虚拟仿真实验\",\n" +
              "          \"sectionId\": 7,\n" +
              "          \"sectionName\": \"1.3\",\n" +
              "          \"sectionTitle\": \"学习视频三\",\n" +
              "          \"courseStudyRecordId\": 25,\n" +
              "          \"chapterStudyRecordId\": 24,\n" +
              "          \"studyStatus\": 1,\n" +
              "          \"userId\": 15,\n" +
              "          \"userName\": \"4\",\n" +
              "          \"createTime\": \"2021-06-29 20:34:32\",\n" +
              "          \"updateTime\": \"2021-06-29 21:03:59\"\n" +
              "        },\n" +
              "        {\n" +
              "          \"id\": 56,\n" +
              "          \"chapterId\": 5,\n" +
              "          \"chapterName\": \"一\",\n" +
              "          \"chapterTitle\": \"外骨骼上肢康复机器人控制虚拟仿真实验\",\n" +
              "          \"sectionId\": 8,\n" +
              "          \"sectionName\": \"1.4\",\n" +
              "          \"sectionTitle\": \"学习视频四\",\n" +
              "          \"courseStudyRecordId\": 25,\n" +
              "          \"chapterStudyRecordId\": 24,\n" +
              "          \"studyStatus\": 1,\n" +
              "          \"userId\": 15,\n" +
              "          \"userName\": \"4\",\n" +
              "          \"createTime\": \"2021-06-29 20:34:32\",\n" +
              "          \"updateTime\": \"2021-06-29 21:04:01\"\n" +
              "        }\n" +
              "      ]\n" +
              "    }\n" +
              "  ],\n" +
              "  \"testRecordList\": [\n" +
              "    {\n" +
              "      \"id\": 34,\n" +
              "      \"courseStudyRecordId\": 25,\n" +
              "      \"testTemplateId\": 9,\n" +
              "      \"testTemplateName\": \"实验报告一\",\n" +
              "      \"status\": 2,\n" +
              "      \"testInstanceId\": 33,\n" +
              "      \"userId\": 15,\n" +
              "      \"userName\": \"4\",\n" +
              "      \"createTime\": \"2021-06-29 20:34:32\",\n" +
              "      \"updateTime\": \"2021-06-29 21:02:59\"\n" +
              "    },\n" +
              "    {\n" +
              "      \"id\": 35,\n" +
              "      \"courseStudyRecordId\": 25,\n" +
              "      \"testTemplateId\": 11,\n" +
              "      \"testTemplateName\": \"实验报告二\",\n" +
              "      \"status\": 2,\n" +
              "      \"testInstanceId\": 34,\n" +
              "      \"userId\": 15,\n" +
              "      \"userName\": \"4\",\n" +
              "      \"createTime\": \"2021-06-29 20:34:32\",\n" +
              "      \"updateTime\": \"2021-06-29 21:03:33\"\n" +
              "    }\n" +
              "  ]\n" +
              "}";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        CourseStudyRecordPublicVo courseStudyRecord = gson.fromJson(ss, CourseStudyRecordPublicVo.class);
        courseStudyRecord.setStatus(0);
        // 学习状态 0-学习中 1-已完成
        if (courseStudyRecord.getStatus().equals(0)) { // 未完成学习计算学习进度
            List<ChapterStudyRecordPublicVo> chapterStudyRecordList = courseStudyRecord.getChapterStudyRecordPublicVoList();
            Double courseStudied = 1.0 * courseStudyRecord.getTestTemplateSubmitedNumber() / (courseStudyRecord.getTestTemplateNumber() + chapterStudyRecordList.size());
            if (CollectionUtils.isNotEmpty(chapterStudyRecordList)) {
                Double chapterPercent = 1.0 / (courseStudyRecord.getTestTemplateNumber() + chapterStudyRecordList.size());
                for (ChapterStudyRecordPublicVo chapterStudyRecord : chapterStudyRecordList) {
                    List<SectionStudyRecord> sectionStudyRecordList = chapterStudyRecord.getSectionStudyRecordList();
                    Double chapterStudied = 0.0;
                    if (CollectionUtils.isNotEmpty(sectionStudyRecordList)) {
                        Double sectionPercent = 1.0 / sectionStudyRecordList.size();
                        for (SectionStudyRecord sectionStudyRecord : sectionStudyRecordList) {
                            if (sectionStudyRecord.getStudyStatus().equals(1)) {
                                chapterStudied += sectionPercent;
                            }
                        }
                    } else {
                        chapterStudied = 1.0;
                    }
                    chapterStudied = Math.round(chapterStudied * 100) / 100.0;
                    chapterStudyRecord.setStudied(chapterStudied);
                    courseStudied += chapterPercent * chapterStudied;
                }
            } else if (courseStudyRecord.getTestTemplateNumber().equals(0)) {
                courseStudied = 1.0;
            }
            courseStudied = Math.round(courseStudied * 100) / 100.0;
            courseStudyRecord.setStudied(courseStudied);
            courseStudyRecord.setStatus(courseStudyRecord.getStudied().equals(1.0) ? 1 : 0);
        }
        System.out.println(JSONObject.toJSONString(courseStudyRecord));
    }
}
