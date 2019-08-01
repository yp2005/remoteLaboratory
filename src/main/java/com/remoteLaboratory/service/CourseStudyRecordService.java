package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.CourseStudyRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.CourseStudyRecordPublicVo;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 课程学习记录服务接口定义
 *
 * @Author: yupeng
 */

public interface CourseStudyRecordService {
    /**
     * 添加课程学习记录信息
     * @param courseStudyRecord
     * @return
     */
    public CourseStudyRecord add(CourseStudyRecord courseStudyRecord) throws BusinessException;

    /**
     * 开始学习
     * @param courseId
     * @param user
     * @return
     */
    public CourseStudyRecordPublicVo startStudy(Integer courseId, User user) throws BusinessException;

    /**
     * 更新学习进度
     * @param id
     * @return
     */
    public void update(Integer id) throws BusinessException;

    /**
     * 修改课程学习记录信息
     * @param courseStudyRecord
     * @return
     */
    public CourseStudyRecord update(CourseStudyRecord courseStudyRecord) throws BusinessException;

    /**
     * 根据条件查询课程学习记录信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据条件查询某个课程的课程学习记录列表
     * @param listInput
     * @param courseId
     * @return ListOutput
     */
    public ListOutput listByCourseId(ListInput listInput, Integer courseId) throws BusinessException;

    /**
     * 根据条件查询某个用户的课程学习记录列表
     * @param listInput
     * @param userId
     * @return ListOutput
     */
    public ListOutput listByUserId(ListInput listInput, Integer userId) throws BusinessException;

    /**
     * 根据id获取课程学习记录信息
     * @param id
     * @return CourseStudyRecord
     */
    public CourseStudyRecord get(Integer id) throws BusinessException;

    /**
     * 根据课程id获取课程学习记录详情
     * @param courseId
     * @param userId
     * @return CourseStudyRecordPublicVo
     */
    public CourseStudyRecordPublicVo getDetailByCourseId(Integer courseId, Integer userId) throws BusinessException;

    /**
     * 根据id获取课程学习记录详情
     * @param id
     * @return CourseStudyRecordPublicVo
     */
    public CourseStudyRecordPublicVo getDetailById(Integer id) throws BusinessException;

    /**
     * 修改课程学习记录信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
