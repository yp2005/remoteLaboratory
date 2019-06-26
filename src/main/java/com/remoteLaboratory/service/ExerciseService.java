package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.Exercise;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 习题服务接口定义
 *
 * @Author: yupeng
 */

public interface ExerciseService {
    /**
     * 添加习题信息
     * @param exercise
     * @return
     */
    public Exercise add(Exercise exercise) throws BusinessException;

    /**
     * 修改习题信息
     * @param exercise
     * @return
     */
    public Exercise update(Exercise exercise) throws BusinessException;

    /**
     * 根据条件查询习题信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取习题信息
     * @param id
     * @return Exercise
     */
    public Exercise get(Integer id) throws BusinessException;

    /**
     * 修改习题信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
