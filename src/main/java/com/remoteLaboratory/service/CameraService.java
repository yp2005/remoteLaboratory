package com.remoteLaboratory.service;

import com.remoteLaboratory.entities.Camera;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 摄像头服务接口定义
 *
 * @Author: yupeng
 */

public interface CameraService {
    /**
     * 添加摄像头信息
     * @param camera
     * @return
     */
    public Camera add(Camera camera) throws BusinessException;
    
    /**
     * 修改摄像头信息
     * @param camera
     * @return
     */
    public Camera update(Camera camera) throws BusinessException;

    /**
     * 删除摄像头信息
     * @param ids
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;

    /**
     * 根据条件查询摄像头信息列表
     * @param listInput
     * @return
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取摄像头信息
     * @param id
     * @return
     */
    public Camera get(Integer id) throws BusinessException;

}
