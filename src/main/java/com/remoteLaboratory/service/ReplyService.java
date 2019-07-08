package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.Reply;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 回复服务接口定义
 *
 * @Author: yupeng
 */

public interface ReplyService {
    /**
     * 添加回复信息
     * @param reply
     * @return
     */
    public Reply add(Reply reply) throws BusinessException;

    /**
     * 修改回复信息
     * @param reply
     * @return
     */
    public Reply update(Reply reply) throws BusinessException;

    /**
     * 根据条件查询回复信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取回复信息
     * @param id
     * @return Reply
     */
    public Reply get(Integer id) throws BusinessException;

    /**
     * 修改回复信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
