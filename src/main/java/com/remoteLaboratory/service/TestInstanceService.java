package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.TestInstance;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import com.remoteLaboratory.vo.TestInstancePublicVo;

import java.util.List;

/**
 * 实验报告实例服务接口定义
 *
 * @Author: yupeng
 */

public interface TestInstanceService {
    /**
     * 添加实验报告实例信息
     * @param testInstancePublicVo
     * @return
     */
    public TestInstancePublicVo add(TestInstancePublicVo testInstancePublicVo) throws BusinessException;

    /**
     * 开始答题
     * @param testTemplateId
     * @param user
     * @return
     */
    public TestInstancePublicVo startTest(Integer testTemplateId, User user) throws BusinessException;

    /**
     * 根据条件查询实验报告实例信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据条件查询某个课程的实验报告实例信息列表
     * @param listInput
     * @param courseId
     * @return ListOutput
     */
    public ListOutput listByCourseId(ListInput listInput, Integer courseId) throws BusinessException;

    /**
     * 根据id获取实验报告实例信息
     * @param id
     * @return TestInstance
     */
    public TestInstance get(Integer id) throws BusinessException;

    /**
     * 根据userId获取实验报告实例
     * @param userId
     * @return TestInstance
     */
    public List<TestInstance> getByUserId(Integer userId) throws BusinessException;

    /**
     * 根据userId、courseId获取实验报告实例
     * @param userId
     * @param courseId
     * @return TestInstance
     */
    public List<TestInstance> getByUserIdAndCourseId(Integer userId, Integer courseId) throws BusinessException;

    /**
     * 根据id获取实验报告实例信息
     * @param id
     * @return TestInstance
     */
    public TestInstancePublicVo getDetail(Integer id) throws BusinessException;

    /**
     * 提交实验报告
     * @param id
     * @return TestInstancePublicVo
     */
    public TestInstancePublicVo submit(Integer id, Integer status, User user) throws BusinessException;
}
