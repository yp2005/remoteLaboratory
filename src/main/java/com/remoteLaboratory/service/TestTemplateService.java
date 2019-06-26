package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.TestTemplate;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import com.remoteLaboratory.vo.TestTemplatePublicVo;

import java.util.List;

/**
 * 测验模板服务接口定义
 *
 * @Author: yupeng
 */

public interface TestTemplateService {
    /**
     * 添加测验模板信息
     * @param testTemplatePublicVo
     * @return
     */
    public TestTemplatePublicVo add(TestTemplatePublicVo testTemplatePublicVo) throws BusinessException;

    /**
     * 修改测验模板信息
     * @param testTemplatePublicVo
     * @return
     */
    public TestTemplatePublicVo update(TestTemplatePublicVo testTemplatePublicVo) throws BusinessException;

    /**
     * 根据条件查询测验模板信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取测验模板信息
     * @param id
     * @return TestTemplate
     */
    public TestTemplate get(Integer id) throws BusinessException;

    /**
     * 根据课程小结id获取测验模板详情
     * @param sectionId
     * @return TestTemplatePublicVo
     */
    public TestTemplatePublicVo getDetailBySectionId(Integer sectionId) throws BusinessException;

    /**
     * 根据id获取测验模板详情
     * @param id
     * @return TestTemplatePublicVo
     */
    public TestTemplatePublicVo getDetail(Integer id) throws BusinessException;

    /**
     * 根据课程小结id获取测验模板信息
     * @param sectionId
     * @return TestTemplate
     */
    public TestTemplate getBySectionId(Integer sectionId) throws BusinessException;

    /**
     * 修改测验模板信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
