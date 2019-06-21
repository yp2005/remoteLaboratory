package com.remoteLaboratory.service;

import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 课程服务接口定义
 *
 * @Author: yupeng
 */

public interface CourseService {
    /**
     * 添加课程信息
     * @param course
     * @return
     */
    public Course add(Course course) throws BusinessException;
    
    /**
     * 修改课程信息
     * @param course
     * @return
     */
    public Course update(Course course) throws BusinessException;

    /**
     * 删除课程信息
     * @param ids
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;

    /**
     * 根据条件查询课程信息列表
     * @param listInput
     * @return
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取课程信息
     * @param id
     * @return
     */
    public Course get(Integer id) throws BusinessException;

}
