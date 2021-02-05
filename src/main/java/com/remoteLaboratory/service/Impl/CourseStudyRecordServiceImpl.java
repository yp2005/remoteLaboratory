package com.remoteLaboratory.service.Impl;

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
import com.remoteLaboratory.vo.ChapterStudyRecordPublicVo;
import com.remoteLaboratory.vo.CourseStudyRecordPublicVo;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
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

    private CourseStudyRecordRepository courseStudyRecordRepository;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    private CourseRepository courseRepository;

    private ChapterRepository chapterRepository;

    private SectionRepository sectionRepository;

    private ChapterStudyRecordRepository chapterStudyRecordRepository;

    private SectionStudyRecordRepository sectionStudyRecordRepository;

    private TestInstanceRepository testInstanceRepository;

    private TestTemplateRepository testTemplateRepository;

    private TestRecordRepository testRecordRepository;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseStudyRecordServiceImpl(CourseStudyRecordRepository courseStudyRecordRepository,
                                        LogRecordRepository logRecordRepository,
                                        ChapterRepository chapterRepository,
                                        SectionRepository sectionRepository,
                                        TestInstanceRepository testInstanceRepository,
                                        ChapterStudyRecordRepository chapterStudyRecordRepository,
                                        SectionStudyRecordRepository sectionStudyRecordRepository,
                                        CourseRepository courseRepository,
                                        TestRecordRepository testRecordRepository,
                                        TestTemplateRepository testTemplateRepository,
                                        JdbcTemplate jdbcTemplate,
                                        CourseService courseService) {
        this.courseStudyRecordRepository = courseStudyRecordRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
        this.chapterRepository = chapterRepository;
        this.sectionRepository = sectionRepository;
        this.chapterStudyRecordRepository = chapterStudyRecordRepository;
        this.sectionStudyRecordRepository = sectionStudyRecordRepository;
        this.courseRepository = courseRepository;
        this.testInstanceRepository = testInstanceRepository;
        this.testTemplateRepository = testTemplateRepository;
        this.testRecordRepository = testRecordRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

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
            courseStudyRecord.setStatus(0);
            courseStudyRecord.setUserId(user.getId());
            courseStudyRecord.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
            courseStudyRecord.setCourseId(course.getId());
            courseStudyRecord.setCourseName(course.getName());
            courseStudyRecord.setCourseMainImg(course.getMainImg());
            courseStudyRecord.setCourseIntroduction(course.getIntroduction());
            courseStudyRecord.setCourseVideoDesc(course.getVideoDesc());
            courseStudyRecord.setIsQuestionnaireFinish(false);
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
        if (courseStudyRecord.getStatus().equals(0)) { // 未完成学习计算学习进度
            List<ChapterStudyRecord> chapterStudyRecordList = this.chapterStudyRecordRepository.findByCourseStudyRecordId(courseStudyRecord.getId());
            Double courseStudied = courseStudyRecord.getTestTemplateSubmitedNumber() / (courseStudyRecord.getTestTemplateNumber() + chapterStudyRecordList.size()) * 1.0;
            if (CollectionUtils.isNotEmpty(chapterStudyRecordList)) {
                Double chapterPercent = 1.0 / chapterStudyRecordList.size();
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
            List<TestInstance> testInstances = this.testInstanceRepository.findByUserIdAndCourseId(courseStudyRecord.getUserId(), courseStudyRecord.getCourseId());
            Double totalScore = 0.0;
            if (CollectionUtils.isNotEmpty(testInstances)) {
                for (TestInstance testInstance : testInstances) {
                    totalScore += testInstance.getScored();
                }
            }
            // 实验报告的平均分作为课程分数
            courseStudyRecord.setScore(Math.round(totalScore / courseStudyRecord.getTestTemplateNumber() * 10) / 10.0);
            courseStudyRecord = this.courseStudyRecordRepository.save(courseStudyRecord);
        }
    }

    @Override
    public void calculateScore(Integer id) throws BusinessException {
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findOne(id);
        if (courseStudyRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        List<TestInstance> testInstances = this.testInstanceRepository.findByUserIdAndCourseId(courseStudyRecord.getUserId(), courseStudyRecord.getCourseId());
        Double score = 0.0;
        if (CollectionUtils.isNotEmpty(testInstances)) {
            for (TestInstance testInstance : testInstances) {
                score += testInstance.getScored();
            }
        }
        courseStudyRecord.setScore(score);
        this.courseStudyRecordRepository.save(courseStudyRecord);
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
    public Map<String, Long> getScoreStatisticsByCourseId(Integer courseId) throws BusinessException {
        String sql = "SELECT CASE WHEN score >= 90 THEN '优' WHEN score >= 80 THEN '良' WHEN score >= 70 THEN '中' WHEN score >= 60 THEN '及格' ELSE '不及格' END AS level , count(score) AS count FROM rl_course_study_record WHERE course_id = ? GROUP BY CASE WHEN score >= 90 THEN '优' WHEN score >= 80 THEN '良' WHEN score >= 70 THEN '中' WHEN score >= 60 THEN '及格' ELSE '不及格' END";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, courseId);
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
}
