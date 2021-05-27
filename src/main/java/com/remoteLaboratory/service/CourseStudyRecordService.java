package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.CourseStudyRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.*;

import java.util.List;
import java.util.Map;

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
    CourseStudyRecord add(CourseStudyRecord courseStudyRecord) throws BusinessException;

    /**
     * 开始学习
     * @param courseId
     * @param user
     * @return
     */
    CourseStudyRecordPublicVo startStudy(Integer courseId, User user) throws BusinessException;

    /**
     * 更新学习进度
     * @param courseStudyRecord
     * @return
     */
    void updatePercent(CourseStudyRecord courseStudyRecord) throws BusinessException;

    /**
     * 计算分数
     * @param courseStudyRecord
     * @return
     */
    CourseStudyRecord calculateScore(CourseStudyRecord courseStudyRecord) throws BusinessException;

    /**
     * 修改课程学习记录信息
     * @param courseStudyRecord
     * @return
     */
    CourseStudyRecord update(CourseStudyRecord courseStudyRecord) throws BusinessException;

    /**
     * 根据条件查询课程学习记录信息列表
     * @param listInput
     * @return ListOutput
     */
    ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据条件查询某个课程的课程学习记录列表
     * @param listInput
     * @param courseId
     * @return ListOutput
     */
    ListOutput listByCourseId(ListInput listInput, Integer courseId) throws BusinessException;

    /**
     * 根据条件查询某个用户的课程学习记录列表
     * @param listInput
     * @param userId
     * @return ListOutput
     */
    ListOutput listByUserId(ListInput listInput, Integer userId) throws BusinessException;

    /**
     * 根据id获取课程学习记录信息
     * @param id
     * @return CourseStudyRecord
     */
    CourseStudyRecord get(Integer id) throws BusinessException;

    /**
     * 根据课程id获取课程学习记录详情
     * @param courseId
     * @param userId
     * @return CourseStudyRecordPublicVo
     */
    CourseStudyRecordPublicVo getDetailByCourseId(Integer courseId, Integer userId) throws BusinessException;

    /**
     * 统计成绩分布
     * @param scoreStatisticsInput
     * @return Map
     */
    Map<String, Long> getScoreStatistics(ScoreStatisticsInput scoreStatisticsInput) throws BusinessException;

    /**
     * 根据id获取课程学习记录详情
     * @param id
     * @return CourseStudyRecordPublicVo
     */
    CourseStudyRecordPublicVo getDetailById(Integer id) throws BusinessException;

    /**
     * 修改课程学习记录信息
     * @param ids
     * @param loginUser
     */
    void delete(List<Integer> ids, User loginUser) throws BusinessException;

    /**
     * 根据courseId获取学习课程的班级
     * @param getClassByCourseIdInput
     * @return List<String>
     */
    public List<String> getClassByCourseId(GetClassByCourseIdInput getClassByCourseIdInput) throws BusinessException;

    /**
     * 根据courseId获取学习课程的年级
     * @param getClassByCourseIdInput
     * @return List<String>
     */
    public List<String> getGradeByCourseId(GetClassByCourseIdInput getClassByCourseIdInput) throws BusinessException;
}
