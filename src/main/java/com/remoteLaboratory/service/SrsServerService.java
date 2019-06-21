package com.remoteLaboratory.service;

import com.remoteLaboratory.entities.SrsServer;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import com.remoteLaboratory.vo.SrsServerAgentVo;

import java.util.List;

/**
 * srs服务服务接口定义
 *
 * @Author: yupeng
 */

public interface SrsServerService {
    /**
     * 添加srs服务信息
     * @param srsServer
     * @return
     */
    public SrsServer add(SrsServer srsServer) throws BusinessException;

    /**
     * 修改srs服务信息
     * @param srsServer
     * @return
     */
    public SrsServer update(SrsServer srsServer) throws BusinessException;

    /**
     * 删除srs服务信息
     * @param ids
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;

    /**
     * 根据条件查询srs服务信息列表
     * @param listInput
     * @return
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取srs服务信息
     * @param id
     * @return
     */
    public SrsServer get(Integer id) throws BusinessException;

    /**
     * 根据uniqueKey获取srs服务信息
     * @param uniqueKey
     * @return
     */
    public SrsServerAgentVo getByUniqueKey(String uniqueKey) throws BusinessException;
}
