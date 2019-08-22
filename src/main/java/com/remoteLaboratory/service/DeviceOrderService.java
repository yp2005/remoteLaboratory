package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.DeviceOrder;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 设备预约接口定义
 *
 * @Author: yupeng
 */

public interface DeviceOrderService {
    /**
     * 添加设备预约信息
     * @param deviceOrder
     * @return
     */
    public DeviceOrder add(DeviceOrder deviceOrder) throws BusinessException;

    /**
     * 修改设备预约信息
     * @param deviceOrder
     * @return
     */
    public DeviceOrder update(DeviceOrder deviceOrder) throws BusinessException;

    /**
     * 根据条件查询设备预约信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取设备预约信息
     * @param id
     * @return DeviceOrder
     */
    public DeviceOrder get(Integer id) throws BusinessException;

    /**
     * 根据userId获取设备预约信息
     * @param userId
     * @return DeviceOrder
     */
    public DeviceOrder getByUserId(Integer userId) throws BusinessException;

    /**
     * 预约设备
     * @param courseId 课程ID
     * @param deviceOrderId 设备预约ID
     * @param user
     * @return DeviceOrder
     */
    public DeviceOrder order(Integer courseId, Integer deviceOrderId, User user) throws BusinessException;

    /**
     * 修改设备预约信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
