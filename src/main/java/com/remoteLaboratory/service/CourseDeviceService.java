package com.remoteLaboratory.service;

import com.remoteLaboratory.entities.CourseDevice;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.CourseDeviceListAddInput;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 课程设备服务接口定义
 *
 * @Author: yupeng
 */

public interface CourseDeviceService {
    /**
     * 添加课程设备信息
     * @param courseDevice
     * @return
     */
    public CourseDevice add(CourseDevice courseDevice) throws BusinessException;

    /**
     * 批量添加课程设备信息
     * @param courseDeviceListAddInput
     * @return
     */
    public List<CourseDevice> listAdd(CourseDeviceListAddInput courseDeviceListAddInput) throws BusinessException;


    /**
     * 修改课程设备信息
     * @param courseDevice
     * @return
     */
    public CourseDevice update(CourseDevice courseDevice) throws BusinessException;

    /**
     * 删除课程设备信息
     * @param ids
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;

    /**
     * 根据条件查询课程设备信息列表
     * @param listInput
     * @return
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取课程设备信息
     * @param id
     * @return
     */
    public CourseDevice get(Integer id) throws BusinessException;

}
