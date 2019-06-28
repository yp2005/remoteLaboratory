package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.UserOnlineTime;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 用户在线时间服务接口定义
 *
 * @Author: yupeng
 */

public interface UserOnlineTimeService {
    /**
     * 添加用户在线时间信息
     * @param userOnlineTime
     * @return
     */
    public UserOnlineTime add(UserOnlineTime userOnlineTime) throws BusinessException;

    /**
     * 修改用户在线时间信息
     * @param userOnlineTime
     * @return
     */
    public UserOnlineTime update(UserOnlineTime userOnlineTime) throws BusinessException;

    /**
     * 根据条件查询用户在线时间信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取用户在线时间信息
     * @param id
     * @return UserOnlineTime
     */
    public UserOnlineTime get(Integer id) throws BusinessException;

    /**
     * 修改用户在线时间信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
