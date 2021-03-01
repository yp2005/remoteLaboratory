package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.CourseComment;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 课程评论服务接口定义
 *
 * @Author: yupeng
 */

public interface CourseCommentService {
    /**
     * 添加课程评论信息
     * @param courseComment
     * @return
     */
    public CourseComment add(CourseComment courseComment) throws BusinessException;

    /**
     * 修改课程评论信息
     * @param courseComment
     * @return
     */
    public CourseComment update(CourseComment courseComment) throws BusinessException;

    /**
     * 设置评论首页显示
     * @param courseComment
     * @return
     */
    public CourseComment setMainPageDisplay(CourseComment courseComment) throws BusinessException;

    /**
     * 根据条件查询课程评论信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取课程评论信息
     * @param id
     * @return CourseComment
     */
    public CourseComment get(Integer id) throws BusinessException;

    /**
     * 修改课程评论信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
