package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.UploadFile;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 上传文件服务接口定义
 *
 * @Author: yupeng
 */

public interface UploadFileService {
    /**
     * 添加上传文件信息
     * @param uploadFile
     * @return
     */
    public UploadFile add(UploadFile uploadFile) throws BusinessException;

    /**
     * 修改上传文件信息
     * @param uploadFile
     * @return
     */
    public UploadFile update(UploadFile uploadFile) throws BusinessException;

    /**
     * 根据条件查询上传文件信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据条件查询某个用户的上传文件信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput listByUserId(ListInput listInput, Integer userId) throws BusinessException;

    /**
     * 根据id获取上传文件信息
     * @param id
     * @return UploadFile
     */
    public UploadFile get(Integer id) throws BusinessException;

    /**
     * 修改上传文件信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
