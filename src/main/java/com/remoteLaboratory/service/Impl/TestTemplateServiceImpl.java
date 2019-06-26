package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.TestExerciseTemplateRepository;
import com.remoteLaboratory.repositories.TestPartTemplateRepository;
import com.remoteLaboratory.repositories.TestTemplateRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.TestTemplateService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.ExerciseUtil;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import com.remoteLaboratory.vo.TestPartTemplatePublicVo;
import com.remoteLaboratory.vo.TestTemplatePublicVo;
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
 * 测验模板服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class TestTemplateServiceImpl implements TestTemplateService {
    private static Logger log = LoggerFactory.getLogger(TestTemplateServiceImpl.class);

    private TestTemplateRepository testTemplateRepository;

    private TestPartTemplateRepository testPartTemplateRepository;

    private TestExerciseTemplateRepository testExerciseTemplateRepository;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    @Autowired
    public TestTemplateServiceImpl(TestTemplateRepository testTemplateRepository,
                                   CourseService courseService,
                                   TestPartTemplateRepository testPartTemplateRepository,
                                   TestExerciseTemplateRepository testExerciseTemplateRepository,
                                   LogRecordRepository logRecordRepository) {
        this.testTemplateRepository = testTemplateRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
        this.testPartTemplateRepository = testPartTemplateRepository;
        this.testExerciseTemplateRepository = testExerciseTemplateRepository;
    }

    @Override
    public TestTemplatePublicVo add(TestTemplatePublicVo testTemplatePublicVo) throws BusinessException {
        TestTemplate tt = this.testTemplateRepository.findBySectionId(testTemplatePublicVo.getSectionId());
        if(tt != null) {
            throw new BusinessException(Messages.CODE_40010, "课程小节已有测验模板");
        }
        TestTemplate testTemplate = testTemplatePublicVo.voToEntity();
        testTemplate = testTemplateRepository.save(testTemplate);
        List<TestPartTemplatePublicVo> testPartTemplatePublicVoList = testTemplatePublicVo.getTestPartTemplatePublicVoList();
        if(CollectionUtils.isNotEmpty(testPartTemplatePublicVoList)) {
            for(TestPartTemplatePublicVo testPartTemplatePublicVo : testPartTemplatePublicVoList) {
                TestPartTemplate testPartTemplate = testPartTemplatePublicVo.voToEntity();
                testPartTemplate.setTestTemplateId(testTemplate.getId());
                testPartTemplate = this.testPartTemplateRepository.save(testPartTemplate);
                testPartTemplatePublicVo.setId(testPartTemplate.getId());
                testPartTemplatePublicVo.setUpdateTime(testPartTemplate.getUpdateTime());
                testPartTemplatePublicVo.setCreateTime(testPartTemplate.getCreateTime());
                List<TestExerciseTemplate> testExerciseTemplateList = testPartTemplatePublicVo.getTestExerciseTemplateList();
                if(CollectionUtils.isNotEmpty(testExerciseTemplateList)) {
                    for(TestExerciseTemplate testExerciseTemplate : testExerciseTemplateList) {
                        testExerciseTemplate.setTestTemplateId(testTemplate.getId());
                        testExerciseTemplate.setTestPartTemplateId(testPartTemplate.getId());
                        testExerciseTemplate.setType(ExerciseUtil.getType(testExerciseTemplate.getExercisesType()));
                        testExerciseTemplate = this.testExerciseTemplateRepository.save(testExerciseTemplate);
                    }
                }
            }
        }
        testTemplatePublicVo.setId(testTemplate.getId());
        testTemplatePublicVo.setUpdateTime(testTemplate.getUpdateTime());
        testTemplatePublicVo.setCreateTime(testTemplate.getCreateTime());
        return testTemplatePublicVo;
    }

    @Override
    public TestTemplatePublicVo update(TestTemplatePublicVo testTemplatePublicVo) throws BusinessException {
        TestTemplate testTemplate = testTemplatePublicVo.voToEntity();
        testTemplate = testTemplateRepository.save(testTemplate);
        this.testPartTemplateRepository.deleteByTestTemplateId(testTemplate.getId());
        this.testExerciseTemplateRepository.deleteByTestTemplateId(testTemplate.getId());
        List<TestPartTemplatePublicVo> testPartTemplatePublicVoList = testTemplatePublicVo.getTestPartTemplatePublicVoList();
        if(CollectionUtils.isNotEmpty(testPartTemplatePublicVoList)) {
            for(TestPartTemplatePublicVo testPartTemplatePublicVo : testPartTemplatePublicVoList) {
                TestPartTemplate testPartTemplate = testPartTemplatePublicVo.voToEntity();
                testPartTemplate.setTestTemplateId(testTemplate.getId());
                testPartTemplate = this.testPartTemplateRepository.save(testPartTemplate);
                testPartTemplatePublicVo.setId(testPartTemplate.getId());
                testPartTemplatePublicVo.setUpdateTime(testPartTemplate.getUpdateTime());
                testPartTemplatePublicVo.setCreateTime(testPartTemplate.getCreateTime());
                List<TestExerciseTemplate> testExerciseTemplateList = testPartTemplatePublicVo.getTestExerciseTemplateList();
                if(CollectionUtils.isNotEmpty(testExerciseTemplateList)) {
                    for(TestExerciseTemplate testExerciseTemplate : testExerciseTemplateList) {
                        testExerciseTemplate.setTestTemplateId(testTemplate.getId());
                        testExerciseTemplate.setTestPartTemplateId(testPartTemplate.getId());
                        testExerciseTemplate.setType(ExerciseUtil.getType(testExerciseTemplate.getExercisesType()));
                        testExerciseTemplate = this.testExerciseTemplateRepository.save(testExerciseTemplate);
                    }
                }
            }
        }
        testTemplatePublicVo.setId(testTemplate.getId());
        testTemplatePublicVo.setUpdateTime(testTemplate.getUpdateTime());
        testTemplatePublicVo.setCreateTime(testTemplate.getCreateTime());
        return testTemplatePublicVo;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            TestTemplate testTemplate = this.testTemplateRepository.findOne(id);
            if(testTemplate != null) {
                Course course = this.courseService.get(testTemplate.getCourseId());
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                testTemplateRepository.delete(id);
                this.testPartTemplateRepository.deleteByTestTemplateId(testTemplate.getId());
                this.testExerciseTemplateRepository.deleteByTestTemplateId(testTemplate.getId());
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("测验模板");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(testTemplate.getId());
                logRecord.setObjectName(testTemplate.getName());
                logRecords.add(logRecord);
            }
        }
        for(LogRecord logRecord : logRecords) {
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
    public TestTemplatePublicVo getDetailBySectionId(Integer sectionId) throws BusinessException {
        TestTemplate testTemplate = testTemplateRepository.findBySectionId(sectionId);
        if (testTemplate == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        TestTemplatePublicVo testTemplatePublicVo = new TestTemplatePublicVo(testTemplate);
        List<TestPartTemplate> testPartTemplateList = this.testPartTemplateRepository.findByTestTemplateId(testTemplate.getId());
        if(CollectionUtils.isNotEmpty(testPartTemplateList)) {
            List<TestPartTemplatePublicVo> testPartTemplatePublicVoList = new ArrayList<>();
            for(TestPartTemplate testPartTemplate : testPartTemplateList) {
                TestPartTemplatePublicVo testPartTemplatePublicVo = new TestPartTemplatePublicVo(testPartTemplate);
                List<TestExerciseTemplate> testExerciseTemplateList = this.testExerciseTemplateRepository.findByTestPartTemplateId(testPartTemplate.getId());
                testPartTemplatePublicVo.setTestExerciseTemplateList(testExerciseTemplateList);
                testPartTemplatePublicVoList.add(testPartTemplatePublicVo);
            }
            testTemplatePublicVo.setTestPartTemplatePublicVoList(testPartTemplatePublicVoList);
        }
        return testTemplatePublicVo;
    }

    @Override
    public TestTemplatePublicVo getDetail(Integer id) throws BusinessException {
        TestTemplate testTemplate = testTemplateRepository.findOne(id);
        if (testTemplate == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        TestTemplatePublicVo testTemplatePublicVo = new TestTemplatePublicVo(testTemplate);
        List<TestPartTemplate> testPartTemplateList = this.testPartTemplateRepository.findByTestTemplateId(testTemplate.getId());
        if(CollectionUtils.isNotEmpty(testPartTemplateList)) {
            List<TestPartTemplatePublicVo> testPartTemplatePublicVoList = new ArrayList<>();
            for(TestPartTemplate testPartTemplate : testPartTemplateList) {
                TestPartTemplatePublicVo testPartTemplatePublicVo = new TestPartTemplatePublicVo(testPartTemplate);
                List<TestExerciseTemplate> testExerciseTemplateList = this.testExerciseTemplateRepository.findByTestPartTemplateId(testPartTemplate.getId());
                testPartTemplatePublicVo.setTestExerciseTemplateList(testExerciseTemplateList);
                testPartTemplatePublicVoList.add(testPartTemplatePublicVo);
            }
            testTemplatePublicVo.setTestPartTemplatePublicVoList(testPartTemplatePublicVoList);
        }
        return testTemplatePublicVo;
    }

    @Override
    public TestTemplate getBySectionId(Integer sectionId) throws BusinessException {
        TestTemplate testTemplate = testTemplateRepository.findBySectionId(sectionId);
        if (testTemplate == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return testTemplate;
    }

}
