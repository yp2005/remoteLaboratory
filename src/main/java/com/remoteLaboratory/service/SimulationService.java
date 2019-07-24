package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.Simulation;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 仿真实验服务接口定义
 *
 * @Author: yupeng
 */

public interface SimulationService {
    /**
     * 添加仿真实验信息
     * @param simulation
     * @return
     */
    public Simulation add(Simulation simulation) throws BusinessException;

    /**
     * 修改仿真实验信息
     * @param simulation
     * @return
     */
    public Simulation update(Simulation simulation) throws BusinessException;

    /**
     * 根据条件查询仿真实验信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取仿真实验信息
     * @param id
     * @return Simulation
     */
    public Simulation get(Integer id) throws BusinessException;

    /**
     * 修改仿真实验信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
