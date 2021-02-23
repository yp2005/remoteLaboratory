package com.remoteLaboratory.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.TestExerciseInstanceRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.TestExerciseTemplateRepository;
import com.remoteLaboratory.repositories.TestInstanceRepository;
import com.remoteLaboratory.service.TestExerciseInstanceService;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
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
 * 实验报告小题实例服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class TestExerciseInstanceServiceImpl implements TestExerciseInstanceService {
    private static Logger log = LoggerFactory.getLogger(TestExerciseInstanceServiceImpl.class);

    @Autowired
    private TestExerciseInstanceRepository testExerciseInstanceRepository;

    @Autowired
    private TestExerciseTemplateRepository testExerciseTemplateRepository;

    @Autowired
    private TestInstanceRepository testInstanceRepository;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private CourseService courseService;

    @Override
    public TestExerciseInstance add(TestExerciseInstance testExerciseInstance) throws BusinessException {
        testExerciseInstance = testExerciseInstanceRepository.save(testExerciseInstance);
        return testExerciseInstance;
    }

    @Override
    public TestExerciseInstance update(TestExerciseInstance testExerciseInstance) throws BusinessException {
        testExerciseInstance = testExerciseInstanceRepository.save(testExerciseInstance);
        return testExerciseInstance;
    }

    @Override
    public TestExerciseInstance answer(TestExerciseInstance testExerciseInstance) throws BusinessException {
        testExerciseInstance = testExerciseInstanceRepository.save(testExerciseInstance);
        TestInstance testInstance = this.testInstanceRepository.findOne(testExerciseInstance.getTestInstanceId());
        testInstance.setTestExerciseInstanceId(testExerciseInstance.getTestInstanceId());
        testInstance = this.testInstanceRepository.save(testInstance);

        if(testInstance.getTestType().equals(2)) { // 问卷调查统计选项
            TestExerciseTemplate testExerciseTemplate = this.testExerciseTemplateRepository.findOne(testExerciseInstance.getTestExerciseTemplateId());
            String[] answers = testExerciseInstance.getAnswer().split(",");
            JSONArray options = JSONArray.parseArray(testExerciseTemplate.getOptions());
            for (String answer : answers) {
                answer = answer.trim();
                for (int i = 0; i < options.size(); i++) {
                    JSONObject option = options.getJSONObject(i);
                    if(option.getString("order").equals(answer)) {
                        option.put("selectNumber", option.getInteger("selectNumber") + 1);
                    }
                }
            }
            testExerciseTemplate.setOptions(options.toJSONString());
            this.testExerciseTemplateRepository.save(testExerciseTemplate);
        }
        return testExerciseInstance;
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
            Page<TestExerciseInstance> list = testExerciseInstanceRepository.findAll(new MySpecification<TestExerciseInstance>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<TestExerciseInstance> list = testExerciseInstanceRepository.findAll(new MySpecification<TestExerciseInstance>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public TestExerciseInstance get(Integer id) throws BusinessException {
        TestExerciseInstance testExerciseInstance = testExerciseInstanceRepository.findOne(id);
        if (testExerciseInstance == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return testExerciseInstance;
    }

}
