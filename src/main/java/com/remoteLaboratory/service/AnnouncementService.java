package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.Announcement;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 公告服务接口定义
 *
 * @Author: yupeng
 */

public interface AnnouncementService {
    /**
     * 添加公告信息
     * @param announcement
     * @return
     */
    public Announcement add(Announcement announcement) throws BusinessException;

    /**
     * 修改公告信息
     * @param announcement
     * @return
     */
    public Announcement update(Announcement announcement) throws BusinessException;

    /**
     * 根据条件查询公告信息列表
     * @param listInput
     * @return AnnouncementListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取公告信息
     * @param id
     * @return Announcement
     */
    public Announcement get(Integer id) throws BusinessException;

    /**
     * 修改公告信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
