package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.SysSetting;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 系统设置服务接口定义
 *
 * @Author: yupeng
 */

public interface SysSettingService {
    /**
     * 添加系统设置信息
     * @param sysSetting
     * @return SysSetting
     */
    public SysSetting add(SysSetting sysSetting) throws BusinessException;

    /**
     * 修改系统设置信息
     * @param sysSetting
     * @return SysSetting
     */
    public SysSetting update(SysSetting sysSetting) throws BusinessException;

    /**
     * 修改系统设置信息
     * @param ids
     */
    public void delete(List<String> ids) throws BusinessException;

    /**
     * 根据条件查询系统设置信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取系统设置信息
     * @param id
     * @return SysSetting
     */
    public SysSetting get(String id) throws BusinessException;

    /**
     * 根据keyName获取系统设置信息
     * @param keyName
     * @return SysSetting
     */
    public SysSetting getByKeyName(String keyName) throws BusinessException;
}
