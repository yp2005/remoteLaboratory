package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.SignalChannel;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 信号通道服务接口定义
 *
 * @Author: yupeng
 */

public interface SignalChannelService {
    /**
     * 添加信号通道信息
     * @param signalChannel
     * @return
     */
    public SignalChannel add(SignalChannel signalChannel) throws BusinessException;

    /**
     * 修改信号通道信息
     * @param signalChannel
     * @return
     */
    public SignalChannel update(SignalChannel signalChannel) throws BusinessException;

    /**
     * 根据条件查询信号通道信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取信号通道信息
     * @param id
     * @return SignalChannel
     */
    public SignalChannel get(Integer id) throws BusinessException;

    /**
     * 修改信号通道信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
