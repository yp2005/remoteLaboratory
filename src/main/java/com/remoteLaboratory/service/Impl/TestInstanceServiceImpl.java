package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.bo.SearchPara;
import com.remoteLaboratory.bo.SearchParas;
import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.*;
import com.remoteLaboratory.service.CourseStudyRecordService;
import com.remoteLaboratory.service.TestInstanceService;
import com.remoteLaboratory.service.TestTemplateService;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实验报告实例服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class TestInstanceServiceImpl implements TestInstanceService {
    private static Logger log = LoggerFactory.getLogger(TestInstanceServiceImpl.class);

    @Autowired
    private TestInstanceRepository testInstanceRepository;

    @Autowired
    private TestSubsectionInstanceRepository testSubsectionInstanceRepository;

    @Autowired
    private TestPartInstanceRepository testPartInstanceRepository;

    @Autowired
    private TestExerciseInstanceRepository testExerciseInstanceRepository;

    @Autowired
    private TestTemplateService testTemplateService;

    @Autowired
    private SectionStudyRecordRepository sectionStudyRecordRepository;

    @Autowired
    private CourseStudyRecordService courseStudyRecordService;

    @Autowired
    private CourseStudyRecordRepository courseStudyRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRecordRepository testRecordRepository;

    @Override
    public TestInstancePublicVo add(TestInstancePublicVo testInstancePublicVo) throws BusinessException {
        TestInstance testInstance = testInstancePublicVo.voToEntity();
        testInstance = testInstanceRepository.save(testInstance);
        if (testInstance.getTestType().equals(1)) { // 实验报告
            List<TestSubsectionInstancePublicVo> testSubsectionInstancePublicVoList = testInstancePublicVo.getTestSubsectionInstancePublicVoList();
            if (CollectionUtils.isNotEmpty(testSubsectionInstancePublicVoList)) {
                for (TestSubsectionInstancePublicVo testSubsectionInstancePublicVo : testSubsectionInstancePublicVoList) {
                    TestSubsectionInstance testSubsectionInstance = testSubsectionInstancePublicVo.voToEntity();
                    testSubsectionInstance.setTestInstanceId(testInstance.getId());
                    testSubsectionInstance = this.testSubsectionInstanceRepository.save(testSubsectionInstance);
                    testSubsectionInstancePublicVo.setId(testSubsectionInstance.getId());
                    testSubsectionInstancePublicVo.setUpdateTime(testSubsectionInstance.getUpdateTime());
                    testSubsectionInstancePublicVo.setCreateTime(testSubsectionInstance.getCreateTime());
                    List<TestPartInstancePublicVo> testPartInstancePublicVoList = testSubsectionInstancePublicVo.getTestPartInstancePublicVoList();
                    if (CollectionUtils.isNotEmpty(testPartInstancePublicVoList)) {
                        for (TestPartInstancePublicVo testPartInstancePublicVo : testPartInstancePublicVoList) {
                            TestPartInstance testPartInstance = testPartInstancePublicVo.voToEntity();
                            testPartInstance.setTestInstanceId(testInstance.getId());
                            testPartInstance.setTestSubsectionInstanceId(testSubsectionInstance.getId());
                            testPartInstance = this.testPartInstanceRepository.save(testPartInstance);
                            testPartInstancePublicVo.setId(testPartInstance.getId());
                            testPartInstancePublicVo.setUpdateTime(testPartInstance.getUpdateTime());
                            testPartInstancePublicVo.setCreateTime(testPartInstance.getCreateTime());
                            List<TestExerciseInstance> testExerciseInstanceList = testPartInstancePublicVo.getTestExerciseInstanceList();
                            if (CollectionUtils.isNotEmpty(testExerciseInstanceList)) {
                                for (TestExerciseInstance testExerciseInstance : testExerciseInstanceList) {
                                    testExerciseInstance.setTestInstanceId(testInstance.getId());
                                    testExerciseInstance.setTestSubsectionInstanceId(testSubsectionInstance.getId());
                                    testExerciseInstance.setTestPartInstanceId(testPartInstance.getId());
                                    testExerciseInstance.setClass1(testInstance.getClass1());
                                    testExerciseInstance.setGrade(testInstance.getGrade());
                                    testExerciseInstance = this.testExerciseInstanceRepository.save(testExerciseInstance);
                                }
                            }
                        }
                    }
                }
            }
            TestRecord testRecord = this.testRecordRepository.findByTestTemplateIdAndUserId(testInstance.getTestTemplateId(), testInstance.getUserId());
            testRecord.setTestInstanceId(testInstance.getId());
            testRecord.setStatus(0);
            this.testRecordRepository.save(testRecord);
        } else { // 问卷调查
            List<TestExerciseInstance> testExerciseInstanceList = testInstancePublicVo.getTestExerciseInstanceList();
            if (CollectionUtils.isNotEmpty(testExerciseInstanceList)) {
                for (TestExerciseInstance testExerciseInstance : testExerciseInstanceList) {
                    testExerciseInstance.setTestInstanceId(testInstance.getId());
                    testExerciseInstance = this.testExerciseInstanceRepository.save(testExerciseInstance);
                }
            }
        }
        testInstancePublicVo.setId(testInstance.getId());
        testInstancePublicVo.setUpdateTime(testInstance.getUpdateTime());
        testInstancePublicVo.setCreateTime(testInstance.getCreateTime());
        return testInstancePublicVo;
    }

    private TestInstancePublicVo update(TestInstancePublicVo testInstancePublicVo) throws BusinessException {
        TestInstance testInstance = testInstancePublicVo.voToEntity();
        testInstance = testInstanceRepository.save(testInstance);
        if (testInstance.getTestType().equals(1)) { // 实验报告
            List<TestSubsectionInstancePublicVo> testSubsectionInstancePublicVoList = testInstancePublicVo.getTestSubsectionInstancePublicVoList();
            if (CollectionUtils.isNotEmpty(testSubsectionInstancePublicVoList)) {
                for (TestSubsectionInstancePublicVo testSubsectionInstancePublicVo : testSubsectionInstancePublicVoList) {
                    TestSubsectionInstance testSubsectionInstance = testSubsectionInstancePublicVo.voToEntity();
                    testSubsectionInstance = this.testSubsectionInstanceRepository.save(testSubsectionInstance);
                    testSubsectionInstancePublicVo.setUpdateTime(testSubsectionInstance.getUpdateTime());
                    List<TestPartInstancePublicVo> testPartInstancePublicVoList = testSubsectionInstancePublicVo.getTestPartInstancePublicVoList();
                    if (CollectionUtils.isNotEmpty(testPartInstancePublicVoList)) {
                        for (TestPartInstancePublicVo testPartInstancePublicVo : testPartInstancePublicVoList) {
                            TestPartInstance testPartInstance = testPartInstancePublicVo.voToEntity();
                            testPartInstance = this.testPartInstanceRepository.save(testPartInstance);
                            testPartInstancePublicVo.setUpdateTime(testPartInstance.getUpdateTime());
                            List<TestExerciseInstance> testExerciseInstanceList = testPartInstancePublicVo.getTestExerciseInstanceList();
                            if (CollectionUtils.isNotEmpty(testExerciseInstanceList)) {
                                for (TestExerciseInstance testExerciseInstance : testExerciseInstanceList) {
                                    testExerciseInstance = this.testExerciseInstanceRepository.save(testExerciseInstance);
                                }
                            }
                        }
                    }
                }
            }
        } else { // 问卷调查
            List<TestExerciseInstance> testExerciseInstanceList = testInstancePublicVo.getTestExerciseInstanceList();
            if (CollectionUtils.isNotEmpty(testExerciseInstanceList)) {
                for (TestExerciseInstance testExerciseInstance : testExerciseInstanceList) {
                    testExerciseInstance = this.testExerciseInstanceRepository.save(testExerciseInstance);
                }
            }
        }
        testInstancePublicVo.setUpdateTime(testInstance.getUpdateTime());
        return testInstancePublicVo;
    }

    @Override
    public TestInstancePublicVo startTest(Integer testTemplateId, User user) throws BusinessException {
        TestInstance testInstance = this.testInstanceRepository.findByUserIdAndTestTemplateId(user.getId(), testTemplateId);
        if (testInstance != null) {
            TestInstancePublicVo testInstancePublicVo = new TestInstancePublicVo(testInstance);
            List<TestExerciseInstance> testExerciseInstanceList = this.testExerciseInstanceRepository.findByTestInstanceId(testInstance.getId());
            if (testInstance.getTestType().equals(1)) { // 实验报告
                List<TestSubsectionInstance> testSubsectionInstanceList = this.testSubsectionInstanceRepository.findByTestInstanceId(testInstance.getId());
                List<TestPartInstance> testPartInstanceList = this.testPartInstanceRepository.findByTestInstanceId(testInstance.getId());
                if (CollectionUtils.isNotEmpty(testSubsectionInstanceList)) {
                    List<TestSubsectionInstancePublicVo> testSubsectionInstancePublicVoList = new ArrayList<>();
                    for (TestSubsectionInstance testSubsectionInstance : testSubsectionInstanceList) {
                        TestSubsectionInstancePublicVo testSubsectionInstancePublicVo = new TestSubsectionInstancePublicVo(testSubsectionInstance);
                        List<TestPartInstance> tpil = testPartInstanceList.stream()
                                .filter(testPartInstance -> testPartInstance.getTestSubsectionInstanceId().equals(testSubsectionInstance.getId()))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(tpil)) {
                            List<TestPartInstancePublicVo> testPartInstancePublicVoList = new ArrayList<>();
                            for (TestPartInstance testPartInstance : tpil) {
                                TestPartInstancePublicVo testPartInstancePublicVo = new TestPartInstancePublicVo(testPartInstance);
                                testPartInstancePublicVo.setTestExerciseInstanceList(testExerciseInstanceList.stream()
                                        .filter(testExerciseInstance -> testExerciseInstance.getTestPartInstanceId().equals(testPartInstance.getId()))
                                        .collect(Collectors.toList()));
                                testPartInstancePublicVoList.add(testPartInstancePublicVo);
                            }
                            testSubsectionInstancePublicVo.setTestPartInstancePublicVoList(testPartInstancePublicVoList);
                        }
                        testSubsectionInstancePublicVoList.add(testSubsectionInstancePublicVo);
                    }
                    testInstancePublicVo.setTestSubsectionInstancePublicVoList(testSubsectionInstancePublicVoList);
                }
            } else {
                testInstancePublicVo.setTestExerciseInstanceList(testExerciseInstanceList);
            }
            return testInstancePublicVo;
        } else {
            TestTemplatePublicVo testTemplatePublicVo = this.testTemplateService.getDetail(testTemplateId);
            TestInstancePublicVo testInstancePublicVo = new TestInstancePublicVo(testTemplatePublicVo);
            testInstancePublicVo.setUserId(user.getId());
            testInstancePublicVo.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
            testInstancePublicVo.setUserKey(user.getUserKey());
            StringBuilder class1 = new StringBuilder();
            class1.append(user.getCollege())
                    .append("->")
                    .append(user.getMajor())
                    .append("->")
                    .append(user.getGrade())
                    .append("->")
                    .append(user.getClass1());
            testInstancePublicVo.setClass1(class1.toString());
            testInstancePublicVo.setGrade(user.getGrade());
            return this.add(testInstancePublicVo);
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
            Page<TestInstance> list = testInstanceRepository.findAll(new MySpecification<TestInstance>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<TestInstance> list = testInstanceRepository.findAll(new MySpecification<TestInstance>(listInput.getSearchParas()));
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
    public TestInstance get(Integer id) throws BusinessException {
        TestInstance testInstance = testInstanceRepository.findOne(id);
        if (testInstance == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return testInstance;
    }

    @Override
    public List<TestInstance> getByUserId(Integer userId) throws BusinessException {
        return this.testInstanceRepository.findByUserId(userId);
    }

    @Override
    public List<TestInstance> getByUserIdAndCourseId(Integer userId, Integer courseId) throws BusinessException {
        return this.testInstanceRepository.findByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public TestInstancePublicVo getDetail(Integer id) throws BusinessException {
        TestInstance testInstance = testInstanceRepository.findOne(id);
        if (testInstance == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        TestInstancePublicVo testInstancePublicVo = new TestInstancePublicVo(testInstance);
        List<TestExerciseInstance> testExerciseInstanceList = this.testExerciseInstanceRepository.findByTestInstanceId(id);
        if (testInstance.getTestType().equals(1)) { // 实验报告
            List<TestSubsectionInstance> testSubsectionInstanceList = this.testSubsectionInstanceRepository.findByTestInstanceId(id);
            List<TestPartInstance> testPartInstanceList = this.testPartInstanceRepository.findByTestInstanceId(id);
            if (CollectionUtils.isNotEmpty(testSubsectionInstanceList)) {
                List<TestSubsectionInstancePublicVo> testSubsectionInstancePublicVoList = new ArrayList<>();
                for (TestSubsectionInstance testSubsectionInstance : testSubsectionInstanceList) {
                    TestSubsectionInstancePublicVo testSubsectionInstancePublicVo = new TestSubsectionInstancePublicVo(testSubsectionInstance);
                    List<TestPartInstance> tpil = testPartInstanceList.stream()
                            .filter(testPartInstance -> testPartInstance.getTestSubsectionInstanceId().equals(testSubsectionInstance.getId()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(tpil)) {
                        List<TestPartInstancePublicVo> testPartInstancePublicVoList = new ArrayList<>();
                        for (TestPartInstance testPartInstance : tpil) {
                            TestPartInstancePublicVo testPartInstancePublicVo = new TestPartInstancePublicVo(testPartInstance);
                            testPartInstancePublicVo.setTestExerciseInstanceList(testExerciseInstanceList.stream()
                                    .filter(testExerciseInstance -> testExerciseInstance.getTestPartInstanceId().equals(testPartInstance.getId()))
                                    .collect(Collectors.toList()));
                            testPartInstancePublicVoList.add(testPartInstancePublicVo);
                        }
                        testSubsectionInstancePublicVo.setTestPartInstancePublicVoList(testPartInstancePublicVoList);
                    }
                    testSubsectionInstancePublicVoList.add(testSubsectionInstancePublicVo);
                }
                testInstancePublicVo.setTestSubsectionInstancePublicVoList(testSubsectionInstancePublicVoList);
            }
        } else {
            testInstancePublicVo.setTestExerciseInstanceList(testExerciseInstanceList);
        }
        return testInstancePublicVo;
    }

    @Override
    public TestInstancePublicVo submit(Integer id, Integer status, User user) throws BusinessException {
        TestInstancePublicVo testInstancePublicVo = this.getDetail(id);
        testInstancePublicVo.setStatus(status);
        if (user == null) {
            user = this.userRepository.findOne(testInstancePublicVo.getUserId());
        }
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findByCourseIdAndUserId(testInstancePublicVo.getCourseId(), user.getId());
        if (testInstancePublicVo.getTestType().equals(1)) { // 实验报告
            if (status.equals(1)) { // 交卷
                testInstancePublicVo.setSubmitTime(new Date());
            } else if (status.equals(2)) { // 阅卷
                testInstancePublicVo.setFinishTime(new Date());
            }
            testInstancePublicVo = this.calculateScore(testInstancePublicVo);
            testInstancePublicVo = this.update(testInstancePublicVo);
            TestRecord testRecord = this.testRecordRepository.findByTestTemplateIdAndUserId(testInstancePublicVo.getTestTemplateId(), testInstancePublicVo.getUserId());
            testRecord.setStatus(status);
            this.testRecordRepository.save(testRecord);
            if (status.equals(1)) { // 交卷
                courseStudyRecord.setTestTemplateSubmitedNumber(courseStudyRecord.getTestTemplateSubmitedNumber() + 1);
            } else if (status.equals(2)) { // 阅卷
                courseStudyRecord.setTestTemplateFinishedNumber(courseStudyRecord.getTestTemplateFinishedNumber() + 1);
            }
            this.courseStudyRecordService.updatePercent(courseStudyRecord);
        } else { // 问卷调查
            this.testInstanceRepository.save(testInstancePublicVo.voToEntity());
            courseStudyRecord.setIsQuestionnaireFinish(true);
            this.courseStudyRecordRepository.save(courseStudyRecord);
        }
        return testInstancePublicVo;
    }

    // 计算分数
    private TestInstancePublicVo calculateScore(TestInstancePublicVo testInstancePublicVo) {
        Double totalScore = 0.0;
        List<TestSubsectionInstancePublicVo> testSubsectionInstancePublicVoList = testInstancePublicVo.getTestSubsectionInstancePublicVoList();
        if (CollectionUtils.isNotEmpty(testSubsectionInstancePublicVoList)) {
            for (TestSubsectionInstancePublicVo testSubsectionInstancePublicVo : testSubsectionInstancePublicVoList) {
                if (testSubsectionInstancePublicVo.getType().equals(4)) { // 实验报告根据答题计算分数
                    List<TestPartInstancePublicVo> testPartInstancePublicVoList = testSubsectionInstancePublicVo.getTestPartInstancePublicVoList();
                    if (CollectionUtils.isNotEmpty(testPartInstancePublicVoList)) {
                        Double subsectionScore = 0.0;
                        for (TestPartInstancePublicVo testPartInstancePublicVo : testPartInstancePublicVoList) {
                            List<TestExerciseInstance> testExerciseInstanceList = testPartInstancePublicVo.getTestExerciseInstanceList();
                            Double partScore = 0.0;
                            if (CollectionUtils.isNotEmpty(testExerciseInstanceList)) {
                                for (TestExerciseInstance testExerciseInstance : testExerciseInstanceList) {
                                    if (testExerciseInstance.getScored() != null) {
                                        partScore += testExerciseInstance.getScored();
                                    } else if (testExerciseInstance.getType().equals(1)) {
                                        if (StringUtils.isNotEmpty(testExerciseInstance.getAnswer()) && testExerciseInstance.getAnswer().equals(testExerciseInstance.getCorrectAnswer())) {
                                            testExerciseInstance.setScored(testExerciseInstance.getScore());
                                            partScore += testExerciseInstance.getScored();
                                            testExerciseInstance.setStatus(1);
                                        } else {
                                            testExerciseInstance.setScored(0.0);
                                            testExerciseInstance.setStatus(0);
                                        }
                                    }
                                }
                            }
                            testPartInstancePublicVo.setScored(partScore);
                            subsectionScore += testPartInstancePublicVo.getScored();
                        }
                        testSubsectionInstancePublicVo.setScored(subsectionScore);
                    }
                }
                if (testSubsectionInstancePublicVo.getScored() != null) {
                    totalScore += testSubsectionInstancePublicVo.getScored();
                }
            }
        }
        testInstancePublicVo.setScored(totalScore);
        return testInstancePublicVo;
    }

    @Override
    public TestSubsectionInstance grade(TestSubsectionInstance testSubsectionInstance) throws BusinessException {
        testSubsectionInstance = testSubsectionInstanceRepository.save(testSubsectionInstance);
        TestInstancePublicVo testInstancePublicVo = this.getDetail(testSubsectionInstance.getTestInstanceId());
        testInstancePublicVo = this.calculateScore(testInstancePublicVo);
        testInstancePublicVo = this.update(testInstancePublicVo);
        CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findByCourseIdAndUserId(testInstancePublicVo.getCourseId(), testInstancePublicVo.getUserId());
        this.courseStudyRecordService.updatePercent(courseStudyRecord);
        return testSubsectionInstance;
    }
}
