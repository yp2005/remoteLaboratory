package com.remoteLaboratory.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.*;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.CourseStudyRecordService;
import com.remoteLaboratory.service.TestTemplateService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.ExerciseUtil;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实验报告模板服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class TestTemplateServiceImpl implements TestTemplateService {
    private static Logger log = LoggerFactory.getLogger(TestTemplateServiceImpl.class);

    @Autowired
    private TestTemplateRepository testTemplateRepository;

    @Autowired
    private TestSubsectionTemplateRepository testSubsectionTemplateRepository;

    @Autowired
    private TestPartTemplateRepository testPartTemplateRepository;

    @Autowired
    private TestExerciseTemplateRepository testExerciseTemplateRepository;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseStudyRecordRepository courseStudyRecordRepository;

    @Autowired
    private CourseStudyRecordService courseStudyRecordService;

    @Autowired
    private TestRecordRepository testRecordRepository;

    @Override
    public TestTemplate add(TestTemplatePublicVo testTemplatePublicVo) throws BusinessException {
        TestTemplate testTemplate = testTemplatePublicVo.voToEntity();
        testTemplate = this.testTemplateRepository.save(testTemplate);
        if (testTemplate.getTestType().equals(1)) { // 实验报告
            List<TestSubsectionTemplatePublicVo> testSubsectionTemplateList = testTemplatePublicVo.getTestSubsectionTemplatePublicVoList();
            if (CollectionUtils.isNotEmpty(testSubsectionTemplateList)) {
                for (TestSubsectionTemplatePublicVo testSubsectionTemplatePublicVo : testSubsectionTemplateList) {
                    TestSubsectionTemplate testSubsectionTemplate = testSubsectionTemplatePublicVo.voToEntity();
                    testSubsectionTemplate.setTestTemplateId(testTemplate.getId());
                    testSubsectionTemplate = this.testSubsectionTemplateRepository.save(testSubsectionTemplate);
                    List<TestPartTemplatePublicVo> testPartTemplatePublicVoList = testSubsectionTemplatePublicVo.getTestPartTemplatePublicVoList();
                    if (CollectionUtils.isNotEmpty(testPartTemplatePublicVoList)) {
                        for (TestPartTemplatePublicVo testPartTemplatePublicVo : testPartTemplatePublicVoList) {
                            TestPartTemplate testPartTemplate = testPartTemplatePublicVo.voToEntity();
                            testPartTemplate.setTestTemplateId(testTemplate.getId());
                            testPartTemplate.setTestSubsectionTemplateId(testSubsectionTemplate.getId());
                            testPartTemplate = this.testPartTemplateRepository.save(testPartTemplate);
                            List<TestExerciseTemplate> testExerciseTemplateList = testPartTemplatePublicVo.getTestExerciseTemplateList();
                            if (CollectionUtils.isNotEmpty(testExerciseTemplateList)) {
                                for (TestExerciseTemplate testExerciseTemplate : testExerciseTemplateList) {
                                    testExerciseTemplate.setTestTemplateId(testTemplate.getId());
                                    testExerciseTemplate.setTestSubsectionTemplateId(testSubsectionTemplate.getId());
                                    testExerciseTemplate.setTestPartTemplateId(testPartTemplate.getId());
                                    testExerciseTemplate.setType(ExerciseUtil.getType(testExerciseTemplate.getExercisesType()));
                                    JSONArray options = JSONArray.parseArray(testExerciseTemplate.getOptions());
                                    for (int i = 0; i < options.size(); i++) {
                                        options.getJSONObject(i).put("selectNumber", 0);
                                    }
                                    testExerciseTemplate.setOptions(options.toJSONString());
                                }
                                this.testExerciseTemplateRepository.save(testExerciseTemplateList);
                            }
                        }
                    }
                }
            }
            if(testTemplatePublicVo.getId() != null) {
                List<CourseStudyRecord> courseStudyRecordList = this.courseStudyRecordRepository.findByCourseIdAndStatus(testTemplate.getCourseId(), 0);
                if(CollectionUtils.isNotEmpty(courseStudyRecordList)) {
                    for (CourseStudyRecord courseStudyRecord : courseStudyRecordList) {
                        TestRecord testRecord = new TestRecord();
                        testRecord.setUserId(courseStudyRecord.getUserId());
                        testRecord.setUserName(courseStudyRecord.getUserName());
                        testRecord.setCourseStudyRecordId(courseStudyRecord.getId());
                        testRecord.setTestTemplateId(testTemplate.getId());
                        testRecord.setTestTemplateName(testTemplate.getName());
                        testRecord.setStatus(0);
                        testRecord = this.testRecordRepository.save(testRecord);
                        courseStudyRecord.setTestTemplateNumber(courseStudyRecord.getTestTemplateNumber() + 1);
                        this.courseStudyRecordService.updatePercent(courseStudyRecord);
                    }
                }
            }
        } else { // 问卷调查
            List<TestExerciseTemplate> testExerciseTemplateList = testTemplatePublicVo.getTestExerciseTemplateList();
            if (CollectionUtils.isNotEmpty(testExerciseTemplateList)) {
                for (TestExerciseTemplate testExerciseTemplate : testExerciseTemplateList) {
                    testExerciseTemplate.setTestTemplateId(testTemplate.getId());
                    testExerciseTemplate.setType(1);
                }
                this.testExerciseTemplateRepository.save(testExerciseTemplateList);
            }
        }
        return testTemplate;
    }

    @Override
    public TestTemplate update(TestTemplatePublicVo testTemplatePublicVo) throws BusinessException {
        this.testSubsectionTemplateRepository.deleteByTestTemplateId(testTemplatePublicVo.getId());
        this.testPartTemplateRepository.deleteByTestTemplateId(testTemplatePublicVo.getId());
        this.testExerciseTemplateRepository.deleteByTestTemplateId(testTemplatePublicVo.getId());
        return this.add(testTemplatePublicVo);
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            TestTemplate testTemplate = this.testTemplateRepository.findOne(id);
            if (testTemplate != null) {
                Course course = this.courseService.get(testTemplate.getCourseId());
                if (!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                if(course.getStatus().equals(1)) {
                    throw new BusinessException(Messages.CODE_40010, "课程进行中不能执行删除操作");
                }
                this.testTemplateRepository.delete(id);
                this.testSubsectionTemplateRepository.deleteByTestTemplateId(id);
                this.testPartTemplateRepository.deleteByTestTemplateId(id);
                this.testExerciseTemplateRepository.deleteByTestTemplateId(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("实验报告实验报告模板");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(testTemplate.getId());
                logRecord.setObjectName(testTemplate.getName());
                logRecords.add(logRecord);
            } else {
                throw new BusinessException(Messages.CODE_20001);
            }
        }
        this.logRecordRepository.save(logRecords);
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
            Page<TestTemplate> list = testTemplateRepository.findAll(new MySpecification<TestTemplate>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<TestTemplate> list = testTemplateRepository.findAll(new MySpecification<TestTemplate>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public TestTemplate get(Integer id) throws BusinessException {
        TestTemplate testTemplate = testTemplateRepository.findOne(id);
        if (testTemplate == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return testTemplate;
    }

    @Override
    public TestTemplatePublicVo getQuestionnaireByCourseId(Integer courseId) throws BusinessException {
        TestTemplate testTemplate = this.testTemplateRepository.findQuestionnaireByCourseId(courseId);
        if (testTemplate != null) {
            TestTemplatePublicVo testTemplatePublicVo = new TestTemplatePublicVo(testTemplate);
            testTemplatePublicVo.setTestExerciseTemplateList(this.testExerciseTemplateRepository.findByTestTemplateId(testTemplate.getId()));
            return testTemplatePublicVo;
        } else {
            return null;
        }
    }

    @Override
    public TestTemplatePublicVo getDetail(Integer id) throws BusinessException {
        TestTemplate testTemplate = testTemplateRepository.findOne(id);
        if (testTemplate == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        List<TestExerciseTemplate> testExerciseTemplateList = this.testExerciseTemplateRepository.findByTestTemplateId(testTemplate.getId());
        TestTemplatePublicVo testTemplatePublicVo = new TestTemplatePublicVo(testTemplate);
        if (testTemplate.getTestType().equals(1)) { // 实验报告
            List<TestSubsectionTemplate> testSubsectionTemplateList = this.testSubsectionTemplateRepository.findByTestTemplateId(testTemplate.getId());
            List<TestPartTemplate> testPartTemplateList = this.testPartTemplateRepository.findByTestTemplateId(testTemplate.getId());
            if (CollectionUtils.isNotEmpty(testSubsectionTemplateList)) {
                List<TestSubsectionTemplatePublicVo> testSubsectionTemplatePublicVoList = new ArrayList<>();
                for (TestSubsectionTemplate testSubsectionTemplate : testSubsectionTemplateList) {
                    TestSubsectionTemplatePublicVo testSubsectionTemplatePublicVo = new TestSubsectionTemplatePublicVo(testSubsectionTemplate);
                    List<TestPartTemplate> tptl = testPartTemplateList.stream()
                            .filter(testPartTemplate -> testPartTemplate.getTestSubsectionTemplateId().equals(testSubsectionTemplate.getId()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(tptl)) {
                        List<TestPartTemplatePublicVo> testPartTemplatePublicVoList = new ArrayList<>();
                        for (TestPartTemplate testPartTemplate : tptl) {
                            TestPartTemplatePublicVo testPartTemplatePublicVo = new TestPartTemplatePublicVo(testPartTemplate);
                            testPartTemplatePublicVo.setTestExerciseTemplateList(testExerciseTemplateList.stream()
                                    .filter(testExerciseTemplate -> testExerciseTemplate.getTestPartTemplateId().equals(testPartTemplate.getId()))
                                    .collect(Collectors.toList()));
                            testPartTemplatePublicVoList.add(testPartTemplatePublicVo);
                        }
                        testSubsectionTemplatePublicVo.setTestPartTemplatePublicVoList(testPartTemplatePublicVoList);
                    }
                    testSubsectionTemplatePublicVoList.add(testSubsectionTemplatePublicVo);
                }
                testTemplatePublicVo.setTestSubsectionTemplatePublicVoList(testSubsectionTemplatePublicVoList);
            }
        } else { // 问卷调查
            testTemplatePublicVo.setTestExerciseTemplateList(testExerciseTemplateList);
        }
        return testTemplatePublicVo;
    }
}
