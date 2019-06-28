package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.Section;
import com.remoteLaboratory.entities.SectionStudyRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 课程节服务接口定义
 *
 * @Author: yupeng
 */

public interface SectionService {
    /**
     * 添加课程节信息
     * @param section
     * @return
     */
    public Section add(Section section) throws BusinessException;

    /**
     * 修改课程节信息
     * @param section
     * @return
     */
    public Section update(Section section) throws BusinessException;

    /**
     * 根据条件查询课程节信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取课程节信息
     * @param id
     * @return Section
     */
    public Section get(Integer id) throws BusinessException;

    /**
     * 开始学习课程小节
     * @param id
     * @param user
     * @return Section
     */
    public Section startStudy(Integer id, User user) throws BusinessException;

    /**
     * 完成课程小节学习
     * @param id
     * @param user
     * @return Section
     */
    public SectionStudyRecord finish(Integer id, User user) throws BusinessException;

    /**
     * 修改课程节信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
