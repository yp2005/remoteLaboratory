package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.Subject;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 话题服务接口定义
 *
 * @Author: yupeng
 */

public interface SubjectService {
    /**
     * 添加话题信息
     * @param subject
     * @return
     */
    public Subject add(Subject subject) throws BusinessException;

    /**
     * 修改话题信息
     * @param subject
     * @return
     */
    public Subject update(Subject subject) throws BusinessException;

    /**
     * 根据条件查询话题信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取话题信息
     * @param id
     * @return Subject
     */
    public Subject get(Integer id) throws BusinessException;

    /**
     * 修改话题信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
