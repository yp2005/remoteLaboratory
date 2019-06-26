package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.TestExerciseInstance;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 测验小题实例服务接口定义
 *
 * @Author: yupeng
 */

public interface TestExerciseInstanceService {
    /**
     * 添加测验小题实例信息
     * @param testExerciseInstance
     * @return
     */
    public TestExerciseInstance add(TestExerciseInstance testExerciseInstance) throws BusinessException;

    /**
     * 修改测验小题实例信息
     * @param testExerciseInstance
     * @return
     */
    public TestExerciseInstance update(TestExerciseInstance testExerciseInstance) throws BusinessException;

    /**
     * 答题
     * @param testExerciseInstance
     * @return
     */
    public TestExerciseInstance answer(TestExerciseInstance testExerciseInstance) throws BusinessException;

    /**
     * 根据条件查询测验小题实例信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取测验小题实例信息
     * @param id
     * @return TestExerciseInstance
     */
    public TestExerciseInstance get(Integer id) throws BusinessException;
}
