package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.TestTemplate;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import com.remoteLaboratory.vo.TestTemplatePublicVo;

import java.util.List;

/**
 * 实验报告模板服务接口定义
 *
 * @Author: yupeng
 */

public interface TestTemplateService {
    /**
     * 添加实验报告模板信息
     * @param testTemplatePublicVo
     * @return
     */
    public TestTemplate add(TestTemplatePublicVo testTemplatePublicVo) throws BusinessException;

    /**
     * 修改实验报告模板信息
     * @param testTemplatePublicVo
     * @return
     */
    public TestTemplate update(TestTemplatePublicVo testTemplatePublicVo) throws BusinessException;

    /**
     * 根据条件查询实验报告模板信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取实验报告模板信息
     * @param id
     * @return TestTemplate
     */
    public TestTemplate get(Integer id) throws BusinessException;

    /**
     * 根据ID查询实验报告模板详情接口
     * @param id
     * @return TestTemplatePublicVo
     */
    public TestTemplatePublicVo getDetail(Integer id) throws BusinessException;

    /**
     * 查询课程问卷调查
     * @param courseId
     * @return TestTemplatePublicVo
     */
    public TestTemplatePublicVo getQuestionnaireByCourseId(Integer courseId) throws BusinessException;

    /**
     * 修改实验报告模板信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
