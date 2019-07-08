package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.TestInstance;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import com.remoteLaboratory.vo.TestInstancePublicVo;

import java.util.List;

/**
 * 测验实例服务接口定义
 *
 * @Author: yupeng
 */

public interface TestInstanceService {
    /**
     * 添加测验实例信息
     * @param testInstancePublicVo
     * @return
     */
    public TestInstancePublicVo add(TestInstancePublicVo testInstancePublicVo) throws BusinessException;

    /**
     * 开始测验
     * @param testTemplateId
     * @param user
     * @return
     */
    public TestInstancePublicVo startTest(Integer testTemplateId, User user) throws BusinessException;

    /**
     * 根据条件查询测验实例信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据条件查询某个小节的测验实例信息列表
     * @param listInput
     * @param sectionId
     * @return ListOutput
     */
    public ListOutput listBySectionId(ListInput listInput, Integer sectionId) throws BusinessException;

    /**
     * 根据条件查询某个章的测验实例信息列表
     * @param listInput
     * @param chapterId
     * @return ListOutput
     */
    public ListOutput listByChapterId(ListInput listInput, Integer chapterId) throws BusinessException;

    /**
     * 根据条件查询某个课程的测验实例信息列表
     * @param listInput
     * @param courseId
     * @return ListOutput
     */
    public ListOutput listByCourseId(ListInput listInput, Integer courseId) throws BusinessException;

    /**
     * 根据id获取测验实例信息
     * @param id
     * @return TestInstance
     */
    public TestInstance get(Integer id) throws BusinessException;

    /**
     * 根据userId获取测验实例
     * @param userId
     * @return TestInstance
     */
    public List<TestInstance> getByUserId(Integer userId) throws BusinessException;

    /**
     * 根据id获取测验实例信息
     * @param id
     * @return TestInstance
     */
    public TestInstancePublicVo getDetail(Integer id) throws BusinessException;

    /**
     * 根据课程小节查询测验实例详情
     * @param sectionId
     * @param user
     * @return
     */
    public TestInstancePublicVo getMyBySectionId(Integer sectionId, User user) throws BusinessException;

    /**
     * 提交测验
     * @param id
     * @return TestInstancePublicVo
     */
    public TestInstancePublicVo submit(Integer id, Integer status, User user) throws BusinessException;
}
