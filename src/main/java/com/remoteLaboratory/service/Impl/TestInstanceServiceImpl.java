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
import java.util.List;

/**
 * 测验实例服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class TestInstanceServiceImpl implements TestInstanceService {
    private static Logger log = LoggerFactory.getLogger(TestInstanceServiceImpl.class);

    private TestInstanceRepository testInstanceRepository;

    private TestPartInstanceRepository testPartInstanceRepository;

    private TestExerciseInstanceRepository testExerciseInstanceRepository;

    private TestTemplateService testTemplateService;

    private SectionStudyRecordRepository sectionStudyRecordRepository;

    private CourseStudyRecordService courseStudyRecordService;

    private CourseStudyRecordRepository courseStudyRecordRepository;

    @Autowired
    public TestInstanceServiceImpl(TestInstanceRepository testInstanceRepository,
                                   TestPartInstanceRepository testPartInstanceRepository,
                                   TestExerciseInstanceRepository testExerciseInstanceRepository,
                                   SectionStudyRecordRepository sectionStudyRecordRepository,
                                   CourseStudyRecordService courseStudyRecordService,
                                   CourseStudyRecordRepository courseStudyRecordRepository,
                                   TestTemplateService testTemplateService) {
        this.testInstanceRepository = testInstanceRepository;
        this.testPartInstanceRepository = testPartInstanceRepository;
        this.testExerciseInstanceRepository = testExerciseInstanceRepository;
        this.testTemplateService = testTemplateService;
        this.sectionStudyRecordRepository = sectionStudyRecordRepository;
        this.courseStudyRecordService = courseStudyRecordService;
        this.courseStudyRecordRepository = courseStudyRecordRepository;
    }

    @Override
    public TestInstancePublicVo add(TestInstancePublicVo testInstancePublicVo) throws BusinessException {
        TestInstance testInstance = testInstancePublicVo.voToEntity();
        testInstance = testInstanceRepository.save(testInstance);
        List<TestPartInstancePublicVo> testPartInstancePublicVoList = testInstancePublicVo.getTestPartInstancePublicVoList();
        if (CollectionUtils.isNotEmpty(testPartInstancePublicVoList)) {
            for (TestPartInstancePublicVo testPartInstancePublicVo : testPartInstancePublicVoList) {
                TestPartInstance testPartInstance = testPartInstancePublicVo.voToEntity();
                testPartInstance.setTestInstanceId(testInstance.getId());
                testPartInstance = this.testPartInstanceRepository.save(testPartInstance);
                testPartInstancePublicVo.setId(testPartInstance.getId());
                testPartInstancePublicVo.setUpdateTime(testPartInstance.getUpdateTime());
                testPartInstancePublicVo.setCreateTime(testPartInstance.getCreateTime());
                List<TestExerciseInstance> testExerciseInstanceList = testPartInstancePublicVo.getTestExerciseInstanceList();
                if (CollectionUtils.isNotEmpty(testExerciseInstanceList)) {
                    for (TestExerciseInstance testExerciseInstance : testExerciseInstanceList) {
                        testExerciseInstance.setTestInstanceId(testInstance.getId());
                        testExerciseInstance.setTestPartInstanceId(testPartInstance.getId());
                        testExerciseInstance = this.testExerciseInstanceRepository.save(testExerciseInstance);
                    }
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
        List<TestPartInstancePublicVo> testPartInstancePublicVoList = testInstancePublicVo.getTestPartInstancePublicVoList();
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
        testInstancePublicVo.setUpdateTime(testInstance.getUpdateTime());
        return testInstancePublicVo;
    }

    @Override
    public TestInstancePublicVo startTest(Integer testTemplateId, User user) throws BusinessException {
        TestInstance testInstance = this.testInstanceRepository.findByUserIdAndTestTemplateId(user.getId(), testTemplateId);
        if (testInstance != null) {
            TestInstancePublicVo testInstancePublicVo = new TestInstancePublicVo(testInstance);
            List<TestPartInstance> testPartInstanceList = this.testPartInstanceRepository.findByTestInstanceId(testInstance.getId());
            if (CollectionUtils.isNotEmpty(testPartInstanceList)) {
                List<TestPartInstancePublicVo> testPartInstancePublicVoList = new ArrayList<>();
                for (TestPartInstance testPartInstance : testPartInstanceList) {
                    TestPartInstancePublicVo testPartInstancePublicVo = new TestPartInstancePublicVo(testPartInstance);
                    List<TestExerciseInstance> testExerciseInstanceList = this.testExerciseInstanceRepository.findByTestPartInstanceId(testPartInstance.getId());
                    testPartInstancePublicVo.setTestExerciseInstanceList(testExerciseInstanceList);
                    testPartInstancePublicVoList.add(testPartInstancePublicVo);
                }
                testInstancePublicVo.setTestPartInstancePublicVoList(testPartInstancePublicVoList);
            }
            return testInstancePublicVo;
        } else {
            TestTemplatePublicVo testTemplatePublicVo = this.testTemplateService.getDetail(testTemplateId);
            TestInstancePublicVo testInstancePublicVo = new TestInstancePublicVo(testTemplatePublicVo);
            testInstancePublicVo.setUserId(user.getId());
            testInstancePublicVo.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
            testInstancePublicVo = this.add(testInstancePublicVo);
            SectionStudyRecord sectionStudyRecord = this.sectionStudyRecordRepository.findBySectionIdAndUserId(testInstancePublicVo.getSectionId(), user.getId());
            if(sectionStudyRecord != null) {
                if(sectionStudyRecord.getTestStatus().equals(0)) {
                    sectionStudyRecord.setTestStatus(2); // 设置作业状态为正在进行
                    sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
                }
            }
            else {
                this.courseStudyRecordService.startStudy(testInstancePublicVo.getCourseId(), user);
                sectionStudyRecord = this.sectionStudyRecordRepository.findBySectionIdAndUserId(testInstancePublicVo.getSectionId(), user.getId());
                sectionStudyRecord.setTestStatus(2); // 设置作业状态为正在进行
                sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
            }
            CourseStudyRecord courseStudyRecord = this.courseStudyRecordRepository.findOne(sectionStudyRecord.getCourseStudyRecordId());
            courseStudyRecord.setChapterId(sectionStudyRecord.getChapterId());
            courseStudyRecord.setChapterName(sectionStudyRecord.getChapterName());
            courseStudyRecord.setChapterTitle(sectionStudyRecord.getChapterTitle());
            courseStudyRecord.setSectionId(sectionStudyRecord.getSectionId());
            courseStudyRecord.setSectionName(sectionStudyRecord.getSectionName());
            courseStudyRecord.setSectionTitle(sectionStudyRecord.getSectionTitle());
            this.courseStudyRecordRepository.save(courseStudyRecord);
            return testInstancePublicVo;
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
    public ListOutput listBySectionId(ListInput listInput, Integer sectionId) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("sectionId");
        searchPara.setOp("eq");
        searchPara.setValue(sectionId);
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
    public ListOutput listByChapterId(ListInput listInput, Integer chapterId) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("chapterId");
        searchPara.setOp("eq");
        searchPara.setValue(chapterId);
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
    public TestInstancePublicVo getDetail(Integer id) throws BusinessException {
        TestInstance testInstance = testInstanceRepository.findOne(id);
        if (testInstance == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        TestInstancePublicVo testInstancePublicVo = new TestInstancePublicVo(testInstance);
        List<TestPartInstance> testPartInstanceList = this.testPartInstanceRepository.findByTestInstanceId(id);
        if (CollectionUtils.isNotEmpty(testPartInstanceList)) {
            List<TestPartInstancePublicVo> testPartInstancePublicVoList = new ArrayList<>();
            for (TestPartInstance testPartInstance : testPartInstanceList) {
                TestPartInstancePublicVo testPartInstancePublicVo = new TestPartInstancePublicVo(testPartInstance);
                List<TestExerciseInstance> testExerciseInstanceList = this.testExerciseInstanceRepository.findByTestPartInstanceId(testPartInstance.getId());
                testPartInstancePublicVo.setTestExerciseInstanceList(testExerciseInstanceList);
                testPartInstancePublicVoList.add(testPartInstancePublicVo);
            }
            testInstancePublicVo.setTestPartInstancePublicVoList(testPartInstancePublicVoList);
        }
        return testInstancePublicVo;
    }

    @Override
    public TestInstancePublicVo getMyBySectionId(Integer sectionId, User user) throws BusinessException {
        TestInstance testInstance = testInstanceRepository.findByUserIdAndSectionId(user.getId(), sectionId);
        if (testInstance == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        TestInstancePublicVo testInstancePublicVo = new TestInstancePublicVo(testInstance);
        List<TestPartInstance> testPartInstanceList = this.testPartInstanceRepository.findByTestInstanceId(testInstance.getId());
        if (CollectionUtils.isNotEmpty(testPartInstanceList)) {
            List<TestPartInstancePublicVo> testPartInstancePublicVoList = new ArrayList<>();
            for (TestPartInstance testPartInstance : testPartInstanceList) {
                TestPartInstancePublicVo testPartInstancePublicVo = new TestPartInstancePublicVo(testPartInstance);
                List<TestExerciseInstance> testExerciseInstanceList = this.testExerciseInstanceRepository.findByTestPartInstanceId(testPartInstance.getId());
                testPartInstancePublicVo.setTestExerciseInstanceList(testExerciseInstanceList);
                testPartInstancePublicVoList.add(testPartInstancePublicVo);
            }
            testInstancePublicVo.setTestPartInstancePublicVoList(testPartInstancePublicVoList);
        }
        return testInstancePublicVo;
    }

    @Override
    public TestInstancePublicVo submit(Integer id, Integer status, User user) throws BusinessException {
        TestInstancePublicVo testInstancePublicVo = this.getDetail(id);
        testInstancePublicVo = this.calculateScore(testInstancePublicVo);
        testInstancePublicVo.setStatus(status);
        testInstancePublicVo = this.update(testInstancePublicVo);
        SectionStudyRecord sectionStudyRecord = this.sectionStudyRecordRepository.findBySectionIdAndUserId(testInstancePublicVo.getSectionId(), user.getId());
        if(status.equals(1)) {
            if(sectionStudyRecord != null) {
                if(!sectionStudyRecord.getTestStatus().equals(1)) {
                    sectionStudyRecord.setTestStatus(1); // 状态设置为已完成
                    sectionStudyRecord.setStudied(sectionStudyRecord.getStudied() + 0.5);
                    sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
                    this.courseStudyRecordService.update(sectionStudyRecord.getCourseStudyRecordId());
                }
            }
            else {
                this.courseStudyRecordService.startStudy(testInstancePublicVo.getCourseId(), user);
                sectionStudyRecord = this.sectionStudyRecordRepository.findBySectionIdAndUserId(id, user.getId());
                sectionStudyRecord.setTestStatus(1); // 状态设置为已完成
                sectionStudyRecord.setStudied(sectionStudyRecord.getStudied() + 0.5);
                sectionStudyRecord = this.sectionStudyRecordRepository.save(sectionStudyRecord);
                this.courseStudyRecordService.update(sectionStudyRecord.getCourseStudyRecordId());
            }
        }
        else {
            this.courseStudyRecordService.update(sectionStudyRecord.getCourseStudyRecordId());
        }
        return testInstancePublicVo;
    }

    // 计算分数
    private TestInstancePublicVo calculateScore(TestInstancePublicVo testInstancePublicVo) {
        List<TestPartInstancePublicVo> testPartInstancePublicVoList = testInstancePublicVo.getTestPartInstancePublicVoList();
        Double totalScore = 0.0;
        if (CollectionUtils.isNotEmpty(testPartInstancePublicVoList)) {
            for (TestPartInstancePublicVo testPartInstancePublicVo : testPartInstancePublicVoList) {
                List<TestExerciseInstance> testExerciseInstanceList = testPartInstancePublicVo.getTestExerciseInstanceList();
                Double partScore = 0.0;
                if (CollectionUtils.isNotEmpty(testExerciseInstanceList)) {
                    for (TestExerciseInstance testExerciseInstance : testExerciseInstanceList) {
                        if (testExerciseInstance.getScored() != null) {
                            partScore += testExerciseInstance.getScored();
                        } else if (testExerciseInstance.getType().equals(1)) {
                            if (testExerciseInstance.getAnswer() != null && testExerciseInstance.getAnswer().equals(testExerciseInstance.getCorrectAnswer())) {
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
                totalScore += testPartInstancePublicVo.getScored();
            }
        }
        testInstancePublicVo.setScored(totalScore);
        return testInstancePublicVo;
    }

}
