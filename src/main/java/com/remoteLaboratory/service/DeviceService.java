package com.remoteLaboratory.service;

import com.remoteLaboratory.entities.Device;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 设备服务接口定义
 *
 * @Author: yupeng
 */

public interface DeviceService {
    /**
     * 添加设备信息
     * @param device
     * @return
     */
    public Device add(Device device) throws BusinessException;
    
    /**
     * 修改设备信息
     * @param device
     * @return
     */
    public Device update(Device device) throws BusinessException;

    /**
     * 删除设备信息
     * @param ids
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;

    /**
     * 根据条件查询设备信息列表
     * @param listInput
     * @return
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取设备信息
     * @param id
     * @return
     */
    public Device get(Integer id) throws BusinessException;

}
